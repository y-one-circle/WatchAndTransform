package io.github.yonecircle.watchtransform.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.yonecircle.watchtransform.WXStatus;
import io.github.yonecircle.watchtransform.dto.StatusResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    //プロパティファイルのロードに失敗した時のエラーハンドル
    @ExceptionHandler(WXException.class)
    public ResponseEntity<StatusResponse> handleFailLoadProperties(WXException wxEx) {
        
        StatusResponse res = new StatusResponse();
        res.setStatus(WXStatus.ERROR);
        res.setMessage(wxEx.getMessage());

        return ResponseEntity.internalServerError().body(res);
    }

    //プロパティファイルのセーブに失敗した時のエラーハンドル
    @ExceptionHandler(WXException.class)
    public ResponseEntity<StatusResponse> handleFailSaveProperties(WXException wxEx) {

        StatusResponse res = new StatusResponse();
        res.setStatus(WXStatus.ERROR);
        res.setMessage(wxEx.getMessage());

        return ResponseEntity.internalServerError().body(res);
    }
}
