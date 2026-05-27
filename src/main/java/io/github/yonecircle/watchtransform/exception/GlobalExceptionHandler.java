package io.github.yonecircle.watchtransform.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.yonecircle.watchtransform.WXStatus;
import io.github.yonecircle.watchtransform.dto.StatusResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    //Validationエラーハンドル
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StatusResponse> handleValidationException(ValidationException validationEx) {
        
        StatusResponse res = new StatusResponse();
        res.setStatus(WXStatus.SYSTEM_ERROR);
        res.setMessage(validationEx.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    //Systemエラーハンドル
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<StatusResponse> handleSystemException(SystemException systemEx) {

        StatusResponse res = new StatusResponse();
        res.setStatus(WXStatus.SYSTEM_ERROR);
        res.setMessage(systemEx.getMessage());

        return ResponseEntity.internalServerError().body(res);
    }
}
