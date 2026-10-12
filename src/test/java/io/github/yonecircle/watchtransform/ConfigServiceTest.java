package io.github.yonecircle.watchtransform;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;

public class ConfigServiceTest {

    private Path tempDir;

    //一時ディレクトリ作成
    @BeforeEach 
    public void setUp(@TempDir Path tempDir) {
        this.tempDir = tempDir;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //loadProperties()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：プロパティファイルが存在する時、その内容を格納したPropertiesを返すか")
    public void should_returnProperties_when_propertiesFileIsExsiting() throws Exception {
        //プロパティファイルのパス作成
        Path configPath = tempDir.resolve("test.properties"); 
        //プロパティファイルの作成
        Properties props = new Properties();
        props.setProperty("endfileFolderPath", "TestEndfileFolderPath");
        props.setProperty("txtFolderPath", "TestTxtFolderPath");
        props.setProperty("tempFolderPath", "TestTempFolderPath");
        props.setProperty("returnCode", "TestReturnCode");
        //書き出し
        try (OutputStream output = Files.newOutputStream(configPath)) {
            props.store(output, "test config");
        }
        //実施
        ConfigService configService = new ConfigService(configPath);
        Properties returnedProperties = configService.loadProperties();
        //検証
        assertAll("返されたPropertiesの中身が期待値ではありません", 
            () -> assertEquals("TestEndfileFolderPath", returnedProperties.getProperty("endfileFolderPath")),
            () -> assertEquals("TestTxtFolderPath", returnedProperties.getProperty("txtFolderPath")),
            () -> assertEquals("TestTempFolderPath", returnedProperties.getProperty("tempFolderPath")), 
            () -> assertEquals("TestReturnCode", returnedProperties.getProperty("returnCode"))
        );
    }
    @Test
    @DisplayName("異常系：プロパティファイルの読み込みに失敗した時、SystemExceptionを投げるか")
    public void should_throwSystemException_when_failedLoadPropertyFile() throws Exception {
        //フォルダパス作成
        Path dummyDir = tempDir;
        ConfigService configService = new ConfigService(dummyDir);
        //検証
        assertThrows(SystemException.class, () -> configService.loadProperties());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //saveProperties()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
}
