package io.github.yonecircle.watchtransform;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.exception.ValidationException;

public class WXExecuteRequestValidatorTest {

    private  WXExecuteRequestValidator validator;
    
    @BeforeEach 
    public void setUp() {
        this.validator = new WXExecuteRequestValidator();
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //validateNotNullOrEmpty()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：全フィールドが有効の時、例外を投げないか")
    public void should_throwNOException_when_allFieldsValid_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        assertDoesNotThrow(() -> {validator.validateNotNullOrEmpty(dto);});
    }
    @Test
    @DisplayName ("異常系：監視フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_endFolderPathIsNull_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(null);
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("監視フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：テキストフォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_textFolderPathIsNull_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath(null);
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("テキストフォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：一時フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_tempFolderPathIsNull_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath(null);
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("一時フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：リターンコードがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_returnCodeIsNull_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode(null);
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("リターンコードが入力されていません", ex.getMessage());
    }

    @Test
    @DisplayName ("異常系：監視フォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_endFolderPathIsEmpty_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("監視フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：テキストフォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_textFolderPathIsEmpty_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("テキストフォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：一時フォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_tempFolderPathIsEmpty_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("一時フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：リターンコードが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_returnCodeIsEmpty_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("リターンコードが入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：複数フィールドが無効の時、結合されたメッセージのValidationExceptionを投げるか")
    public void should_throwValidationExceptionWithCombinedMessage_when_multipleFieldsInvalid_onValidateNotNullOrEmpty() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath(null);
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNullOrEmpty(dto);});
        assertEquals("テキストフォルダのパスは入力されていません リターンコードが入力されていません", ex.getMessage());
    }


    ///////////////////////////////////////////////////////////////////////////////////
    //validateNotNull()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：全フィールドが有効の時、例外を投げないか")
    public void should_throwNOException_when_allFieldsValid_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        assertDoesNotThrow(() -> {validator.validateNotNull(dto);});
    }
    @Test
    @DisplayName ("異常系：監視フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_endFolderPathIsNull_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(null);
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNull(dto);});
        assertEquals("監視フォルダのパスに何も入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：テキストフォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_textFolderPathIsNull_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath(null);
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNull(dto);});
        assertEquals("テキストフォルダのパスに何も入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：一時フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_tempFolderPathIsNull_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath(null);
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNull(dto);});
        assertEquals("一時フォルダのパスに何も入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：リターンコードがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_returnCodeIsNull_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode(null);
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNull(dto);});
        assertEquals("リターンコードに何も入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：複数フィールドが無効の時、結合されたメッセージのValidationExceptionを投げるか")
    public void should_throwValidationExceptionWithCombinedMessage_when_multipleFieldsInvalid_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(null);
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath(null);
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");

        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {validator.validateNotNull(dto);});
        assertEquals("監視フォルダのパスに何も入力されていません 一時フォルダのパスに何も入力されていません", ex.getMessage());
    }
    @Test 
    @DisplayName ("正常系：空文字の場合は例外を投げないか")
    public void should_throwNOException_when_allFieldsEmpty_onValidateNotNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("");
        dto.setTxtFolderPath("");
        dto.setTempFolderPath("");
        dto.setReturnCode("");
        dto.setSuffixMode("");
        
        //検証
        assertDoesNotThrow(() -> {validator.validateNotNull(dto);});
    }
}
