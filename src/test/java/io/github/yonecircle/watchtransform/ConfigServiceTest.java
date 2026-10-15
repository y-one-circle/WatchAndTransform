package io.github.yonecircle.watchtransform;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
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
    @DisplayName ("正常系：ファイルが存在しない時、空のPropertiesを返すか")
    public void should_returnNullProperties_when_fileIsNotExsiting() throws Exception {
        //ダミーパス作成
        Path dummyDir = Paths.get("Hoge");
        ConfigService configService = new ConfigService(dummyDir);
        //実施
        Properties returnedProperties = configService.loadProperties();
        //検証
        assertTrue(returnedProperties.isEmpty(), "返されたPropetiesの中身が空でありません");
    }
    @Test
    @DisplayName("異常系：プロパティファイルの読み込みに失敗した時、SystemExceptionを投げるか")
    public void should_throwSystemException_when_failedLoadPropertyFile() throws Exception {
        //ダミーパス作成
        Path dummyDir = tempDir;
        ConfigService configService = new ConfigService(dummyDir);
        //検証
        assertThrows(SystemException.class, () -> configService.loadProperties());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //saveProperties()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：saveProperties()で渡された値を正しく保存するか")
    public void  shloud_saveCorrectValue_when_calledSaveProperties() throws Exception {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("D:\\Hoge\\endfileFolder");
        dto.setTxtFolderPath("D:\\Hoge\\txtFolderPath");
        dto.setTempFolderPath("D:\\Hoge\\tempFolder");
        dto.setReturnCode("9");
        dto.setSuffixMode("1");
        //プロパティファイルのパス作成
        Path configPath = tempDir.resolve("test.properties"); 
        //実施
        ConfigService configService = new ConfigService(configPath);
        configService.saveProperties(dto);
        //検証
        Properties actualProp = new Properties();
        try (InputStream inputStream = Files.newInputStream(configPath)) {
            actualProp.load(inputStream);
        }
        assertAll("プロパティファイルの中身が期待値ではありません", 
        () -> assertEquals("D:/Hoge/endfileFolder" ,actualProp.getProperty("endfileFolderPath")),
        () -> assertEquals("D:/Hoge/txtFolderPath" ,actualProp.getProperty("txtFolderPath")),
        () -> assertEquals("D:/Hoge/tempFolder" ,actualProp.getProperty("tempFolderPath")),
        () -> assertEquals("9" ,actualProp.getProperty("returnCode"))
        );
    }
    @Test 
    @DisplayName ("異常系：読み込み失敗時にSystemExceptionを投げるか")
    public void shoud_throwSystemException_when_failSaveProperties() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("D:\\Hoge\\endfileFolder");
        dto.setTxtFolderPath("D:\\Hoge\\txtFolderPath");
        dto.setTempFolderPath("D:\\Hoge\\tempFolder");
        dto.setReturnCode("9");
        dto.setSuffixMode("1");
        //ダミーパスの作成
        Path dummyDir = tempDir;
        //実施
        ConfigService configService = new ConfigService(dummyDir);
        //検証
        assertThrows(SystemException.class, () -> configService.saveProperties(dto));
    }
}