package io.github.yonecircle.watchtransform;

import org.springframework.stereotype.Component;

////////////////////////////////////////////////////////////////////////////////////
//内部のステータスとエラー内容を一元管理
////////////////////////////////////////////////////////////////////////////////////
@Component
public class StatusHolder {
    
    private WXStatus currentStatus = WXStatus.WAITING;  //ステータス
    private String errorMessage = null;                 //エラーメッセージ
    private Throwable cause = null;                     //エラー原因詳細

    //ステータスgetter, setter
    public WXStatus getStatus(){
        return this.currentStatus;
    }
    public void setStatus(WXStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    //エラーメッセージgetter, setter
    public String getErrorMessage() {
        return this.errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //エラー原因詳細getter, setter
    public Throwable getCause() {
        return this.cause;
    }
    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
