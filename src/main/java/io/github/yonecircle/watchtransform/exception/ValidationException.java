package io.github.yonecircle.watchtransform.exception;

//入力ミスカスタム例外クラス
//RuntimeExceptionクラス（非checked例外クラスを継承）
public class ValidationException extends RuntimeException {
    ////////////////////////////////////////////////////////////////////////////////////
    //メッセージとエラー詳細を格納する
    //return:無し
    //Note:ユーザが解決できる例外
    ////////////////////////////////////////////////////////////////////////////////////
    public ValidationException(String message) {
        super(message);
    }
}
