package io.github.yonecircle.watchtransform;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import io.github.yonecircle.watchtransform.Service.TextCopy;
import io.github.yonecircle.watchtransform.Service.TextEditor;
import io.github.yonecircle.watchtransform.Service.TextMove;
import io.github.yonecircle.watchtransform.Service.XENDGenerator;
import io.github.yonecircle.watchtransform.Service.XENDPaste;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service

public class ServiceProcess {

    private final StatusHolder statusHolder;
    private final Watcher watcher;

    public ServiceProcess(StatusHolder statusHolder, Watcher watcher) {
        this.statusHolder = statusHolder;
        this.watcher = watcher;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //監視→WX実行までを非同期でまとめて行うメソッド
    //return:無し
    //Notes:Controllerはこのメソッドを呼んで即returnする
    ////////////////////////////////////////////////////////////////////////////////////
    @Async
    public void watchAndExecute(Path endFileDir, Path resultFileDir, Path tempFileDir, String returnCode, String suffixMode) {
        try {
            // 監視開始
            statusHolder.setStatus(WXStatus.WATCHING);
            Path detectedEndFilePath = watcher.watcher(endFileDir);

            // 変換処理開始
            statusHolder.setStatus(WXStatus.PROCESSING);
            execute(detectedEndFilePath, endFileDir, resultFileDir, tempFileDir, returnCode, suffixMode);

            //完了
            statusHolder.setStatus(WXStatus.COMPLETED);

        } catch (Exception e) {
            statusHolder.setStatus(WXStatus.ERROR);
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //WX実行のサービスにパスを渡して実行するメソッド
    //return:無し
    ////////////////////////////////////////////////////////////////////////////////////
    public void execute(Path endFilePath, Path endFileDir, Path resultFileDir, Path tempFileDir, String returnCode, String suffixMode) {

        //endFilePathからendFileNameを取り出す
        String endfileName = endFilePath.getFileName().toString();

        //TextCopy
        Path copySource= this.resolveTxtPathFromEndFile(endfileName, resultFileDir);
        Path copyTarget = this.resolveCopytargetPath(endfileName, tempFileDir);
        TextCopy TC = new TextCopy();
        TC.copy(copySource, copyTarget);

        //TextEdit
        TextEditor TE = new  TextEditor();
        TE.edit(copyTarget);

        /*
        TextRenamePaste
        SuffixModeによって処理が変わる
        SuffixMode=0:WX処理完了後に"_WX"Suffixをテキストファイルにつける
        SuffixMode=1:WX処理完了後にSuffixをテキストファイルから取り除く
        */
        Path text_renametarget_Object;
        if ("0".equals(suffixMode)) {
            text_renametarget_Object = this.resolveRenameTargetPathSuffixMode0(resultFileDir, this.removeExtension(endfileName));
        } else {
            text_renametarget_Object = this.resolveRenameTargetPathSuffixMode1(resultFileDir, this.removeExtension(endfileName));
        }
        TextMove TP = new TextMove();
        TP.textMove(copyTarget, text_renametarget_Object, suffixMode);

        //XENDファイルの生成
        Path xendGenerateTarget = this.resolveGenerateTargetXendPath(tempFileDir, this.removeExtension(endfileName));
        XENDGenerator XG = new XENDGenerator();
        XG.xendGenerator(xendGenerateTarget, returnCode);

        //XENDファイルのペースト
        Path xendPasteTarget = this.resolvePasteTargetXendPath(endFileDir, this.removeExtension(endfileName));
        XENDPaste XP = new XENDPaste();
        XP.xendPaste(xendGenerateTarget, xendPasteTarget);
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //以下パス解決のための補助メソッド群
    ////////////////////////////////////////////////////////////////////////////////////
    //拡張子を除くメソッド
    public String removeExtension(String absoPath){
        File file = new File(absoPath);
        String fileName = file.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        return baseName;
    }
    //検知したendファイルからコピーすべきtxtファイルのPathを返す
    Path resolveTxtPathFromEndFile(String fileName, Path txtfolderPath){
        String fileName_without_extention = removeExtension(fileName);
        String txtfolderPath_String = txtfolderPath.toString();
        String txtfilePath_String = txtfolderPath_String + "\\" + fileName_without_extention + ".txt";
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //検知したendファイルからtxtファイルのコピー先のPathを返す
    Path resolveCopytargetPath(String fileName, Path tempfolderPath){
        String fileName_without_extention = removeExtension(fileName);
        String tempfolderPath_String = tempfolderPath.toString();
        String txtfilePath_String = tempfolderPath_String + "\\" + fileName_without_extention + ".txt";
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=0
    Path resolveRenameTargetPathSuffixMode0(Path textFolderPath, String fileName){
        String textFolderPath_String = textFolderPath.toString();
        String renameTargetTextPath_String = textFolderPath_String + "\\" + fileName +"_Suffix.txt";
        Path renameTargetTextPath_Object = Paths.get(renameTargetTextPath_String);
        return renameTargetTextPath_Object;
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=1
    Path resolveRenameTargetPathSuffixMode1(Path textFolderPath, String fileName){
        String textFolderPath_String = textFolderPath.toString();
        int lastIndex = fileName.lastIndexOf("_");
        String fileName_withoutSuffix = fileName.substring(0, lastIndex);
        String renameTargetTextPath_String = textFolderPath_String + "\\" + fileName_withoutSuffix +".txt";
        Path renameTargetTextPath_Object = Paths.get(renameTargetTextPath_String);
        return renameTargetTextPath_Object;
    }
    //FFENDファイルの生成先パスオブジェクトを返す
    Path resolveGenerateTargetXendPath(Path tempFileFolder, String fileName){
        String tempFileFolder_String = tempFileFolder.toString();
        String generateTargetPath_String = tempFileFolder_String + "\\" + fileName + ".XEND";
        Path generateTargetPath_PathObject = Paths.get(generateTargetPath_String);
        return generateTargetPath_PathObject;
    }
    //FFENDファイルのペースト先パスオブジェクトを返す
    Path resolvePasteTargetXendPath(Path endFolderPath, String FileName){
        String endFolderPath_String = endFolderPath.toString();
        String pasteTargetXENDPath_String = endFolderPath_String + "\\" + FileName + ".XEND";
        Path pasteTargetXENDPath_PathObject = Paths.get(pasteTargetXENDPath_String);
        return pasteTargetXENDPath_PathObject;
    }
}