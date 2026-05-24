package io.github.yonecircle.watchtransform.exception;

//業務ルール違反例外クラス
public class BusinessException extends WXBaseException {
    public BusinessException(String message) {
        super(message);
    }
}
