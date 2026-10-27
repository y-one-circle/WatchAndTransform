package io.github.yonecircle.watchtransform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.exception.StoppedException;
import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.exception.ValidationException;
import io.github.yonecircle.watchtransform.service.TextCopy;
import io.github.yonecircle.watchtransform.service.TextEditor;
import io.github.yonecircle.watchtransform.service.TextMove;
import io.github.yonecircle.watchtransform.service.XENDGenerator;
import io.github.yonecircle.watchtransform.service.XENDMove;

@Service

public class ServiceProcess {

    private final StatusHolder statusHolder;
    private final Watcher watcher;
    private final TextCopy textCopy;
    private final TextEditor textEditor;
    private final TextMove textMove;
    private final XENDGenerator xendGenerator;
    private final XENDMove xendMove;

    public ServiceProcess
        (StatusHolder statusHolder, Watcher watcher,
        TextCopy textCopy, TextEditor textEditor, TextMove textMove, XENDGenerator xendGenerator, XENDMove xendMove) {
        this.statusHolder = statusHolder;
        this.watcher = watcher;
        this.textCopy = textCopy;
        this.textEditor = textEditor;
        this.textMove = textMove;
        this.xendGenerator = xendGenerator;
        this.xendMove = xendMove;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //監視→WX実行までを非同期でまとめて行うメソッド
    //return:無し
    //Notes:Controllerはこのメソッドを呼んで即returnする
    //Note:非同期処理の一番上のため、エラー発生時にはこのメソッドで対処する
    ////////////////////////////////////////////////////////////////////////////////////
    @Async
    public void watchAndTransform
        (Path endFileDir, Path resultFileDir, Path tempFileDir, String returnCode, String suffixMode) {

        try {
            Thread currentthread = Thread.currentThread();
            statusHolder.setThread(currentthread);
            
            //StatusHolderのメッセージと詳細原因のクリア
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.WAITING, null, null));

            // ==============================================
            // Guard: 監視ディレクトリの存在チェック、テキストディレクトリ, 一時ディレクトリの存在チェック
            // 存在しない、またはディレクトリでない場合はreturn
            // ==============================================
            validateDirectory(endFileDir, "監視ディレクトリ");
            validateDirectory(resultFileDir, "テキストファイルディレクトリ");
            validateDirectory(tempFileDir, "一時ディレクトリ");
            // 監視開始
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.WATCHING, endFileDir.toString(), null));
            Path detectedEndFilePath = watcher.watcher(endFileDir);

            // 変換処理開始
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.PROCESSING, null, null));
            transform(detectedEndFilePath, endFileDir, resultFileDir, tempFileDir, returnCode, suffixMode);

            //完了
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.COMPLETED, null, null));

        } catch (ValidationException validationEx) {
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.VALIDATION_ERROR, validationEx.getMessage(), null));
        } catch (StoppedException stoppedEx) {
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.STOPPED, stoppedEx.getMessage(), null));
        } catch (SystemException systemEx) {
            statusHolder.setSnapshot(new StatusSnapshot(WXStatus.SYSTEM_ERROR, systemEx.getMessage(), systemEx.getCause()));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //WX実行のサービスにパスを渡して実行するメソッド
    //return:無し
    //Note:エラー発生時にはwatchAndTransformメソッドにエラーメッセージとThrowableを投げる
    ////////////////////////////////////////////////////////////////////////////////////
    public void transform
        (Path endFilePath, Path endFileDir, Path resultFileDir, Path tempFileDir, String returnCode, String suffixMode) 
        throws SystemException {

        //endFilePathからendFileNameを取り出す
        String endfileName = endFilePath.getFileName().toString();

        // ==============================================
        // Guard: SuffixMode=1はファイル名の"_"を前提にしているため
        // ==============================================
        if (!"0".equals(suffixMode) && !removeExtension(endfileName).endsWith("_Suffix")) {
            throw new ValidationException("SuffixMode=1が選択されていますが、ファイル名に _Suffix が含まれていません：" + endfileName);
        }

        //TextCopy
        Path copySource= this.resolveTxtPathFromEndFile(endfileName, resultFileDir);
        Path copyTarget = this.resolveCopytargetPath(endfileName, tempFileDir);
        this.textCopy.copy(copySource, copyTarget);

        //TextEdit
        this.textEditor.edit(copyTarget);

        /*
        TextRenamePaste
        SuffixModeによって処理が変わる
        SuffixMode=0:WX処理完了後に"_WX"Suffixをテキストファイルにつける
        SuffixMode=1:WX処理完了後にSuffixをテキストファイルから取り除く
        */
        Path text_renametarget_Object;
        if ("0".equals(suffixMode)) {
            text_renametarget_Object = this.resolveRenameTargetPathSuffixMode0(resultFileDir, endfileName);
        } else {
            text_renametarget_Object = this.resolveRenameTargetPathSuffixMode1(resultFileDir, endfileName);
        }
        this.textMove.textMove(copyTarget, text_renametarget_Object);

        //XENDファイルの生成
        Path xendGenerateTarget = this.resolveGenerateTargetXendPath(tempFileDir, endfileName);
        this.xendGenerator.xendGenerator(xendGenerateTarget, returnCode);

        //XENDファイルの移動
        Path xendPasteTarget = this.resolvePasteTargetXendPath(endFileDir, endfileName);
        this.xendMove.xendMove(xendGenerateTarget, xendPasteTarget);
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //ディレクトリが存在するか確認するメソッド
    //throw:ValidationException
    ////////////////////////////////////////////////////////////////////////////////////
    private void validateDirectory(Path path, String pathName) {
        if (!Files.isDirectory(path)) {
            throw new ValidationException(pathName + "が見つかりませんでした：" + path);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //以下パス解決のための補助メソッド群
    ////////////////////////////////////////////////////////////////////////////////////
    //拡張子を除くメソッド
    public String removeExtension(String absoPath){
        Path path = Paths.get(absoPath);
        String fileName = path.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        return baseName;
    }
    //検知したendファイルからコピーすべきtxtファイルのPathを返す
    Path resolveTxtPathFromEndFile(String fileName, Path txtfolderPath){
        return txtfolderPath.resolve(removeExtension(fileName) + ".txt");
    }
    //検知したendファイルからtxtファイルのコピー先のPathを返す
    Path resolveCopytargetPath(String fileName, Path tempfolderPath){
        return tempfolderPath.resolve(removeExtension(fileName) + ".txt");
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=0
    Path resolveRenameTargetPathSuffixMode0(Path textFolderPath, String fileName){
        return textFolderPath.resolve(removeExtension(fileName) + "_Suffix.txt");
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=1
    Path resolveRenameTargetPathSuffixMode1(Path textFolderPath, String fileName){
        fileName = removeExtension(fileName);
        int lastIndex = fileName.lastIndexOf("_");
        String fileName_withoutSuffix = fileName.substring(0, lastIndex);
        return textFolderPath.resolve(fileName_withoutSuffix + ".txt");
    }
    //FFENDファイルの生成先パスオブジェクトを返す
    Path resolveGenerateTargetXendPath(Path tempFileFolder, String fileName){
        return tempFileFolder.resolve(removeExtension(fileName) + ".XEND");
    }
    //FFENDファイルのペースト先パスオブジェクトを返す
    Path resolvePasteTargetXendPath(Path endFolderPath, String fileName){
        return endFolderPath.resolve(removeExtension(fileName) + ".XEND");
    }
}