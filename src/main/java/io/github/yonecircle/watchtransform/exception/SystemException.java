package io.github.yonecircle.watchtransform.exception;

//サーバ異常エラークラス
public class SystemException extends WXBaseException {
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}