package io.github.yonecircle.watchtransform.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ValidationException valiEx = new ValidationException("バリデーション例外が発生");
        ResponseEntity<StatusResponse> response = globalExceptionHandler.handleValidationException(valiEx);
        //検証
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTPSステータスコードが一致しません");
        assertEquals(WXStatus.VALIDATION_ERROR, response.getBody().getStatus(), "レスポンスボディのstatusが一致しません");
        assertEquals("バリデーション例外が発生",response.getBody().getMessage(), "レスポンスボディのmessageが一致しません");
    }

    @Test
    @DisplayName("SystemExceptionを受け取ってHTTPステータスとレスポンスボディが正しいResponseEintityを返すか")
    public void should_returnCorrectResponseEintity_when_acceptSystemException() {
        //SystemExceptionを作成して該当メソッドに渡す
        SystemException sysEx = new SystemException()
    }
    /*
    テストメソッドの命名規則「should_結果_when_条件」
    */
}