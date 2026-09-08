package io.github.yonecircle.watchtransform.service;

import java.nio.file.Path;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;

public class XENDMoveTest {
    private XENDMove xENDMove = new XENDMove();
    private Path srcFile;   //tempFolderPath
    private Path destFile;  //endfileFolder
    private Path tempDir;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {

        this.tempDir = tempDir;
        //コピー元ファイル作成
        Path srcDir = tempDir.resolve("scrDir");
        Files.createDirectory(srcDir);
        Path srcFile = srcDir.resolve("Text.txt");
        Files.writeString(srcFile, "999");
        this.srcFile = srcFile;
        //コピー先パス作成
        Path destDir = tempDir.resolve("destDir");
        Files.createDirectory(destDir);
        Path destFile = destDir.resolve("Text.txt");
        this.destFile = destFile;
    }

    @Test
    @DisplayName("正常系：移動先パスにファイルが存在し、移動元パスにファイルが存在しないか")
    public void should_moveFile_when_moveIsExcuted() throws SystemException {
        //ファイル移動実行
        xENDMove.xendMove(srcFile, destFile);
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
        assertThrows(SystemException.class, ()-> {xENDMove.xendMove(nonExistFile, destFile);}, 
                    "ソースファイルが存在しない時にSystemExeptionを投げれていません");
    }

    @Test
    @DisplayName("異常系：移動先ディレクトリが存在しない時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_moveTargetDirIsMissing() {
        //存在しない移動先パスを作成
        Path nonExistDir = tempDir.resolve("nonExistDir/Text.txt");
        assertThrows(SystemException.class, ()-> {xENDMove.xendMove(srcFile, nonExistDir);}, 
                    "移動先ディレクトリが存在しない時にSystemExeptionを投げれていません");
    }
}
