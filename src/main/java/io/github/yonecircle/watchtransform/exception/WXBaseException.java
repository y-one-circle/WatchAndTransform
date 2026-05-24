package io.github.yonecircle.watchtransform.exception;

//WXのカスタム例外クラスのベース
public class WXBaseException extends RuntimeException {
    public WXBaseException (String message) {
        super(message);
    }

    public WXBaseException (String message, Throwable cause) {
        super(message, cause);
    }
}
