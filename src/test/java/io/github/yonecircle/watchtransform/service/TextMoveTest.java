package io.github.yonecircle.watchtransform.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TextMoveTest {

    TextMove textMove = new TextMove();
    private Path srcFile;
    private Path destFile;
    private Path tempDir;

    ////////////////////////////////////////////////////////////////////////////////////
    //各テストで使用するファイル作成
    //return:無し
    //Note:arrayPathsをフィールドとして持っておき、 @BeforeEach setUp()で帰ってきたPathをarrayPathsに格納する。
    //「各テストメソッドの中でsetUp()を毎回呼んで、返り値としてarrayPathsを受け取る」にしなかったのは前処理忘れ、
    //可読性の低下につながるから。
    ////////////////////////////////////////////////////////////////////////////////////
    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {

        this.tempDir = tempDir;
        //フォルダ作成
        Path srcDir = tempDir.resolve("srcDir");
        Files.createDirectory(srcDir);        
        Path destDir = tempDir.resolve("destDir");
        Files.createDirectory(destDir);
        //フィールドにそれぞれのPathを代入
        this.srcFile = srcDir.resolve("Text.txt");
        this.destFile = destDir.resolve("Text.txt");
        //コピー元ファイル作成
        Files.writeString(srcFile, "Hoge");
    }      

    @Test
    @DisplayName("正常系：移動先パスにファイルが存在し、移動元パスにファイルが存在しないか")
    public void should_moveFile_when_moveIsExcuted() throws SystemException {
        //ファイル移動実行
        textMove.textMove(srcFile, destFile);
        //検証
        assertAll(
            () -> assertFalse(Files.exists(srcFile), "移動元パスにファイルが存在します"),
            () -> assertTrue(Files.exists(destFile), "移動先パスにファイルが存在しません")
        );
    }

    @Test
    @DisplayName("異常系：移動元パスにファイルが存在しない時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_moveSourceFileIsMissing() {
        //存在しないsourceFileパスを作成
        Path nonExistFile = tempDir.resolve("nonExist.txt");
        assertThrows(SystemException.class, ()-> {textMove.textMove(nonExistFile, destFile);}, 
                    "ソースファイルが存在しない時にSystemExeptionを投げれていません");
    }

    @Test
    @DisplayName("異常系：移動先ディレクトリが存在しない時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_moveTargetDirIsMissing() {
        //存在しない移動先パスを作成
        Path nonExistDir = tempDir.resolve("nonExistDir/Text.txt");
        assertThrows(SystemException.class, ()-> {textMove.textMove(srcFile, nonExistDir);}, 
                    "移動先ディレクトリが存在しない時にSystemExeptionを投げれていません");
    }
}
