package io.github.yonecircle.watchtransform.exception;

//サーバ異常カスタム例外クラス
//Exceptionクラス（checked例外クラスを継承）
public class SystemException extends Exception {
    ////////////////////////////////////////////////////////////////////////////////////
    //メッセージとエラー詳細を格納する
    //return:無し
    //Note:ユーザが解決できない例外
    ////////////////////////////////////////////////////////////////////////////////////
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}