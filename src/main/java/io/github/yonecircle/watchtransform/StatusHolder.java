package io.github.yonecircle.watchtransform;

import org.springframework.stereotype.Component;

////////////////////////////////////////////////////////////////////////////////////
//非同期処理内のステータスとエラー内容を一元管理
////////////////////////////////////////////////////////////////////////////////////
@Component
public class StatusHolder {
    
    private volatile StatusSnapshot statusSnapshot = new StatusSnapshot
        (WXStatus.WAITING, null, null);

    private volatile Thread currentThread = null;

    
    //ステータス、エラーメッセージ、エラー原因詳細getter
    public StatusSnapshot getSnapshot() {
        return this.statusSnapshot;
    }

    //ステータス、エラーメッセージ、エラー原因詳細setter
    public void setSnapshot(StatusSnapshot statusSnapshot) {
        this.statusSnapshot = statusSnapshot;
    }

    //スレッドgetter, setter
    public Thread getThread() {
        return this.currentThread;
    }
    public void setThread(Thread thread) {
        this.currentThread = thread;
    }
}
