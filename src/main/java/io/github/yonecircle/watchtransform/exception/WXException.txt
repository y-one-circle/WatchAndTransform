package io.github.yonecircle.watchtransform.exception;

//WX独自例外クラス
public class WXException extends RuntimeException {
    //エラーメッセージを受け取るコンストラクタ
    public WXException (String message, Throwable cause) {
        super (message, cause);
    }
}