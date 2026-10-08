package io.github.yonecircle.watchtransform;

import io.github.yonecircle.watchtransform.exception.ValidationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.yonecircle.watchtransform.dto.StatusResponse;
import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class WXControllerTest {
    @Mock
    ServiceProcess serviceProcess;
    @Mock 
    ConfigService configService;
    @Mock
    StatusHolder statusHolder;
    @InjectMocks 
    WXController wxcontroller;

    ////////////////////////////////////////////////////////////////////////////////////
    //index()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：index()が呼ばれた時、他クラスに経由せず\"index\"を返すか")
    public void should_returnIndex_without_touchingOtherClass() {
        //検証
        assertEquals("index", wxcontroller.index(), "\"index\"を返していません");
        Mockito.verifyNoInteractions(serviceProcess, configService, statusHolder);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //execute()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    //nullまたは空チェック
    @Test
    @DisplayName ("異常系：監視フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_endFolderPathIsNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(null);
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("監視フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：監視フォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_endFolderPathIsEmptiy() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("監視フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：テキストフォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_textFolderPathIsNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath(null);
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("テキストフォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：テキストフォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_textFolderPathIsEmptiy() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("テキストフォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：一時フォルダのパスがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_tempFolderPathIsNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath(null);
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("一時フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：一時フォルダのパスが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_tempFolderPathIsEmptiy() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("一時フォルダのパスは入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：リターンコードがnullの時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_returnCodeIsNull() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode(null);
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("リターンコードが入力されていません", ex.getMessage());
    }
    @Test
    @DisplayName ("異常系：リターンコードが空の時、ValidationExceptionを投げるか")
    public void should_throwValidationException_when_returnCodeIsEmptiy() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("");
        dto.setSuffixMode("0");
        //検証
        ValidationException ex = assertThrows(ValidationException.class, () -> {wxcontroller.execute(dto);});
        assertEquals("リターンコードが入力されていません", ex.getMessage());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //getStatus()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：中身が正しいステータスを返すか")
    public void should_returnCorrectStatus() {
        //スタブの設定
        Mockito.when(statusHolder.getStatus()).thenReturn(WXStatus.PROCESSING);
        Mockito.when(statusHolder.getMessage()).thenReturn("getStatus()のテスト中");
        //実行
        StatusResponse actual = wxcontroller.getStatus();
        //検証
        assertAll (
            "ステータスかメッセージが期待値でありません", 
            () -> assertEquals(WXStatus.PROCESSING, actual.getStatus(), "ステータスが期待値でありません"),
            () -> assertEquals("getStatus()のテスト中", actual.getMessage(), "メッセージが期待値でありません")
        );
    }

}
