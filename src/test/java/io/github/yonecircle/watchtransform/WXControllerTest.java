package io.github.yonecircle.watchtransform;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.nio.file.Paths;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import io.github.yonecircle.watchtransform.dto.StatusResponse;
import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
public class WXControllerTest {
    @Mock
    ServiceProcess serviceProcess;
    @Mock 
    ConfigService configService;
    @Mock
    StatusHolder statusHolder;
    @Mock
    WXExecuteRequestValidator requestValidator;
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
    //executionStatus()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：executionStatus()が呼ばれた時、他クラスに経由せず\"executionStatus\"を返すか")
    public void should_returnExecutionStatus_without_touchingOtherClass() {
        //検証
        assertEquals("executionStatus", wxcontroller.executionStatus(), "\"executionStatus\"を返していません");
        Mockito.verifyNoInteractions(serviceProcess, configService, statusHolder);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //execute()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    
    //watchAndTransform()を呼んでHTTPステータス200を即返すか
    @Test
    @DisplayName ("正常系：watchAndTransform()を呼んでHTTPステータス200を即返すか")
    public  void should_returnStatus200_after_callingwatchAndTransform() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("C:/end");
        dto.setTxtFolderPath("C:/txt");
        dto.setTempFolderPath("C:/temp");
        dto.setReturnCode("TestCode");
        dto.setSuffixMode("0");
        //実行
        ResponseEntity<Void> response = wxcontroller.execute(dto);
        //検証
        assertAll("watchAndTransform()が正常に呼ばれていないかHTTPステータス200を返していません", 
            () -> Mockito.verify(serviceProcess).watchAndTransform(
                Paths.get("C:/end"), Paths.get("C:/txt"), Paths.get("C:/temp"), "TestCode", "0"
            ),
            () -> assertEquals(ResponseEntity.ok().build(), response)
        );
    }
    @Test 
    @DisplayName ("異常系：ValidationExceptionが発生した時、watchAndTransformが呼ばれないか")
    public void should_notInvokeWatchAndTransform_when_validationExeptionOccured() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        //スタブの設定
        Mockito.doThrow(new ValidationException("テストメッセージ")).when(requestValidator).validateNotNullOrEmpty(dto);
        //実施
        assertThrows(ValidationException.class, ()-> wxcontroller.execute(dto)) ;
        //検証
        verifyNoInteractions(serviceProcess);
    }
    


    ////////////////////////////////////////////////////////////////////////////////////
    //loadConfig()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：プロパティファイルが存在する時、その内容が格納されたDTOを返すか") 
    public void should_returnDTO_with_propsInfo_when_propsIsExsiting() throws Exception {
        //Propertiesの準備
        Properties props = new Properties();
        props.setProperty("endfileFolderPath", "C:/end");
        props.setProperty("txtFolderPath", "C:/txt");
        props.setProperty("tempFolderPath", "C:/tmp");
        props.setProperty("returnCode", "0");
        //スタブの設定
        Mockito.when(configService.loadProperties()).thenReturn(props);
        //実施
        WXExecuteRequest dto = wxcontroller.loadConfig();
        //検証
        assertAll("DTOの内容が期待値ではありません", 
            () -> assertEquals("C:/end", dto.getEndfileFolderPath()),
            () -> assertEquals("C:/txt", dto.getTxtFolderPath()),
            () -> assertEquals("C:/tmp", dto.getTempFolderPath()),
            () -> assertEquals("0", dto.getReturnCode())
        );
    }
    @Test
    @DisplayName ("正常系：プロパティファイルが存在しない時、空文字が格納されたDTOを返すか") 
    public void should_returnEmptyDTO__when_propsIsNotExsiting() throws Exception {
        //Propertiesの準備
        Properties props = new Properties();
        //スタブの設定
        Mockito.when(configService.loadProperties()).thenReturn(props);
        //実施
        WXExecuteRequest dto = wxcontroller.loadConfig();
        //検証
        assertAll("DTOの内容が空ではありません", 
            () -> assertEquals("", dto.getEndfileFolderPath()),
            () -> assertEquals("", dto.getTxtFolderPath()),
            () -> assertEquals("", dto.getTempFolderPath()),
            () -> assertEquals("", dto.getReturnCode())
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //getStatus()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：中身が正しいステータスを返すか")
    public void should_returnCorrectStatus() {
        //スタブの設定
        Mockito.when(statusHolder.getSnapshot()).thenReturn(new StatusSnapshot(WXStatus.PROCESSING, "getStatus()のテスト中", null));
        //実行
        StatusResponse actual = wxcontroller.getStatus();
        //検証
        
        assertAll (
            "ステータスかメッセージが期待値でありません", 
            () -> assertEquals(WXStatus.PROCESSING, actual.getStatus(), "ステータスが期待値でありません"),
            () -> assertEquals("getStatus()のテスト中", actual.getMessage(), "メッセージが期待値でありません")
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //saveConfig()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：DTOの中身がnullでない時、saveConfigを呼ぶか")
    public void should_invokeSaveProperties_when_contentsOfDTOIsNotNull() throws SystemException {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath("TestPath");
        dto.setTxtFolderPath("TestPath");
        dto.setTempFolderPath("TestPath");
        dto.setReturnCode("TestCode");
        //実施
        wxcontroller.saveConfig(dto);
        //検証
        Mockito.verify(configService).saveProperties(dto);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //stopWatching()のテスト
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName ("正常系：threadがあればinterrupt()を呼ぶか") 
    public void should_callIntterrupt_when_threadIsExsiting() {
        //Threadの準備
        Thread thread = Mockito.mock(Thread.class);
        //スタブの設定
        Mockito.when(statusHolder.getThread()).thenReturn(thread);
        //実施
        wxcontroller.stopWatching();
        //検証
        Mockito.verify(thread).interrupt();
    }
    @Test
    @DisplayName ("異常系：threadがなければValidationExceptionを投げるか") 
    public void should_throwValidationException_when_threadIsNotExsiting() {
        //Threadの準備
        Thread thread = null;
        //スタブの設定
        Mockito.when(statusHolder.getThread()).thenReturn(thread);
        //実施と検証
        assertThrows(ValidationException.class, () -> wxcontroller.stopWatching()
        , "threadが存在しないときにValidationExceptionが投げれられていません");
        
    }
    @Test 
    @DisplayName ("異常系：ValidationExceptionが発生した時、savePropertiesが呼ばれないか")
    public void should_notInvokesaveProperties_when_validationExeptionOccured() {
        //DTOの準備
        WXExecuteRequest dto = new WXExecuteRequest();
        //スタブの設定
        Mockito.doThrow(new ValidationException("テストメッセージ")).when(requestValidator).validateNotNull(dto);
        //実施
        assertThrows(ValidationException.class, ()-> wxcontroller.saveConfig(dto)) ;
        //検証
        verifyNoInteractions(configService);
    }
}
