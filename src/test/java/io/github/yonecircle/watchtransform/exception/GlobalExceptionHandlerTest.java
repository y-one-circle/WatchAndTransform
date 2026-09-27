package io.github.yonecircle.watchtransform.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.yonecircle.watchtransform.WXStatus;
import io.github.yonecircle.watchtransform.dto.StatusResponse;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();


    @Test
    @DisplayName("ValidationExceptionを受け取ってHTTPステータスとレスポンスボディが正しいResponseEintityを返すか")
    public void should_returnCorrectResponseEintity_when_acceptValidationException() {
        //ValidationExceptionを作成して該当メソッドに渡す
        ValidationException validationEx = new ValidationException("バリデーション例外が発生");
        ResponseEntity<StatusResponse> response = globalExceptionHandler.handleValidationException(validationEx);
        //検証
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTPステータスコードが一致しません");
        assertEquals(WXStatus.VALIDATION_ERROR, response.getBody().getStatus(), "レスポンスボディのstatusが一致しません");
        assertEquals("バリデーション例外が発生",response.getBody().getMessage(), "レスポンスボディのmessageが一致しません");
    }

    @Test
    @DisplayName("SystemExceptionを受け取ってHTTPステータスとレスポンスボディが正しいResponseEintityを返すか")
    public void should_returnCorrectResponseEintity_when_acceptSystemException() {
        //SystemExceptionを作成して該当メソッドに渡す
        SystemException systemEx = new SystemException("システム例外が発生", new IOException("原因"));
        ResponseEntity<StatusResponse> response = globalExceptionHandler.handleSystemException(systemEx);
        //検証
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTPステータスコードが一致しません");
        assertEquals(WXStatus.SYSTEM_ERROR, response.getBody().getStatus(), "レスポンスボディのstatusが一致しません");
        assertEquals("システム例外が発生",response.getBody().getMessage(), "レスポンスボディのmessageが一致しません");
    }
    /*
    テストメソッドの命名規則「should_結果_when_条件」
    */
}
