package io.github.yonecircle.watchtransform.exception;

//入力ミスエラークラス
public class ValidationException extends WXBaseException {
    public ValidationException(String message) {
        super(message);
    }
}
