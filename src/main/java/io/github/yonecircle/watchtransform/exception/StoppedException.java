package io.github.yonecircle.watchtransform.exception;

//ユーザストップ例外クラス
//RuntimeExceptionクラス（非checked例外クラスを継承）
public class StoppedException extends RuntimeException {
    ////////////////////////////////////////////////////////////////////////////////////
    //メッセージを格納する
    //return:無し
    //Note:ユーザが停止させた例外
    ////////////////////////////////////////////////////////////////////////////////////
    public StoppedException(String message) {
        super(message);
    } 
}
