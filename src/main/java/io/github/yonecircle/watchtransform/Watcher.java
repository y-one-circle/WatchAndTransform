package io.github.yonecircle.watchtransform;

import java.io.IOException;
//import java.io.InterruptedIOException;
//import java.nio.file.FileSystem;
import java.nio.file.Path;
//import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
//import java.nio.file.Watchable;

import org.springframework.stereotype.Component;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.nio.file.FileSystems;

import static java.nio.file.StandardWatchEventKinds.*;
//import static java.nio.file.WatchEvent.*;

//ほぼ参照  https://qiita.com/opengl-8080/items/91a8ac36ab3d8600a529
@Component
public class Watcher {
    ///////////////////////////////////////////////////////////////////////////////////////
    //ディレクトリを監視してファイル生成されたら、生成されてファイルのパスを返す
    //return:Path
    //Note:例外処理未実装
    ///////////////////////////////////////////////////////////////////////////////////////
    public Path watcher(Path watchTargetPath) throws SystemException {
    System.out.println("[Watcher] start watching: " + watchTargetPath);//デバッグ

    try {
        //↓ファイル監視のためのOSのリソース確保に失敗する可能性あり
        WatchService watcher = FileSystems.getDefault().newWatchService();
            watchTargetPath.register(watcher, ENTRY_CREATE); //作成に対して監視
        while (true) {
        System.out.println("[Watcher] waiting for event...");//デバッグ
        //↓イベントが来るまで無限待機なのでスレッドを止められるとInterruptedException発生可能性あり
        WatchKey key = watcher.take(); 

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();

            //基本的にENTRY_CREATEしか来ないが念のため
            if (kind != ENTRY_CREATE) {
                continue;
            }
            Path fileName = (Path) event.context();

            //.endファイルだけを対象にする
            if (fileName.toString().endsWith(".end")) {
                System.out.println("kind=" + kind + ", file=" + fileName);
                return watchTargetPath.resolve(fileName);
            }
        }
        key.reset();
    }
    } catch (IOException | InterruptedException e) {
        throw new SystemException("endファイル監視に失敗しました", e);
    }
}
}
