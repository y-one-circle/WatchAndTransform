package io.github.yonecircle.watchtransform;

import org.springframework.stereotype.Component;

@Component
public class StatusHolder {
    private WXStatus currentStatus = WXStatus.WAITING;

    public WXStatus getStatus(){
        return currentStatus;
    }

    public void setStatus(WXStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
