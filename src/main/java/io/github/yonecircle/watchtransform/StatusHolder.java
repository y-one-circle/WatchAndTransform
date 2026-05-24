package io.github.yonecircle.watchtransform;

import org.springframework.stereotype.Component;

////////////////////////////////////////////////////////////////////////////////////
//内部のステータスとエラー内容を一元管理
////////////////////////////////////////////////////////////////////////////////////
@Component
public class StatusHolder {
    //ステータス
    private WXStatus currentStatus = WXStatus.WAITING;
    //エラーメッセージ
    private String errorMessage = null;


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
}
