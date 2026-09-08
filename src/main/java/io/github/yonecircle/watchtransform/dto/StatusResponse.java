package io.github.yonecircle.watchtransform.dto;

import org.springframework.stereotype.Component;

import io.github.yonecircle.watchtransform.WXStatus;

////////////////////////////////////////////////////////////////////////////////////
//ステータスとエラー内容を伝える入力用DTO
////////////////////////////////////////////////////////////////////////////////////
@Component
public class StatusResponse {
    
    private WXStatus status;
    private String message;

    public void setStatus (WXStatus status) {
        this.status = status;
    }
    public WXStatus getStatus() {
        return this.status;
    }    

    public void setMessage (String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
