package io.github.yonecircle.watchtransform.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;

public class XENDGeneratorTest {
    
    private XENDGenerator xENDGenerator = new XENDGenerator();
    private Path generateTargetPath;
    private String ReturnCode = "0";

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        this.generateTargetPath = tempDir.resolve("Sample.XEND");
    }

    @Test
    @DisplayName("正常系：作成されたファイルが存在するか")
    public void should_createTargetFile_when_createIsExcuted() throws SystemException {
        //ファイル作成実行
        xENDGenerator.xendGenerator(generateTargetPath, ReturnCode);
        //検証
        assertTrue(Files.exists(generateTargetPath), "ターゲットパス先にファイルが存在しません");
    }

    @Test
    @DisplayName("正常系：作成したファイルのReturnCodeが元データと一致しているか")
    public void should_beSameReturnCodeWithOriginalData_when_copyIsExcuted() throws SystemException, IOException {
        //ファイル作成実行
        xENDGenerator.xendGenerator(generateTargetPath, ReturnCode);
        //検証
        assertEquals(ReturnCode, Files.readString(generateTargetPath), 
        "作成したファイルのReturnCodeが元データと一致していません");
    }

    @Test
    @DisplayName("異常系：既にファイルが存在する時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_fileIsAlredyExisiting() throws IOException {
        //ターゲットファイルをあらかじめ作成
        Files.createFile(generateTargetPath);
        //ファイル作成実行
        assertThrows(SystemException.class, ()-> {xENDGenerator.xendGenerator(generateTargetPath, ReturnCode);}, 
                    "ソースファイルが存在しない時にSystemExeptionを投げれていません");
    }
}

