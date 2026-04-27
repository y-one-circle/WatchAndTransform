package com.example.demo;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.nio.file.Watchable;

import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.WatchEvent.*;

//ほぼ参照https://qiita.com/opengl-8080/items/91a8ac36ab3d8600a529
@Component
public class Watcher {
    public Path watcher(Path watchTargetPath) throws IOException, InterruptedException {
    System.out.println("[Watcher] start watching: " + watchTargetPath);//デバッグ
        WatchService watcher = FileSystems.getDefault().newWatchService();
            watchTargetPath.register(watcher, ENTRY_CREATE); //作成に対して監視
        while (true) {
        System.out.println("[Watcher] waiting for event...");//デバッグ
        WatchKey key = watcher.take(); //イベントが起きるまで待機

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
}
}
