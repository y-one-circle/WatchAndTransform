package io.github.yonecircle.watchtransform;

import org.springframework.stereotype.Component;

////////////////////////////////////////////////////////////////////////////////////
//非同期処理内のステータスとエラー内容を一元管理
////////////////////////////////////////////////////////////////////////////////////
@Component
public class StatusHolder {
    
    private WXStatus currentStatus = WXStatus.WAITING;  //ステータス
    private String message = null;                 //メッセージ
    private Throwable cause = null;                     //エラー原因詳細
    private Thread currentThread = null;                 //スレッド

    //ステータスgetter, setter
    public WXStatus getStatus(){
        return this.currentStatus;
    }
    public void setStatus(WXStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    //エラーメッセージgetter, setter
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }

    //エラー原因詳細getter, setter
    public Throwable getCause() {
        return this.cause;
    }
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

        //スレッドgetter, setter
    public Thread getThread() {
        return this.currentThread;
    }
    public void setThread(Thread thread) {
        this.currentThread = thread;
    }
}
