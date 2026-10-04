package io.github.yonecircle.watchtransform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.StoppedException;

import java.nio.file.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class WatcherTest {

    private Path tempDir;
    private  Watcher watcher;
    
    ////////////////////////////////////////////////////////////////////////////////////
    //各テストで使用するファイル作成
    //return:無し
    //Note:arrayPathsをフィールドとして持っておき、 @BeforeEach setUp()で帰ってきたPathをarrayPathsに格納する。
    //「各テストメソッドの中でsetUp()を毎回呼んで、返り値としてarrayPathsを受け取る」にしなかったのは前処理忘れ、
    //可読性の低下につながるから。
    ////////////////////////////////////////////////////////////////////////////////////
    @BeforeEach 
    public void setUp(@TempDir Path tempDir) {
        this.tempDir = tempDir;
    }

    @Test
    @DisplayName("正常系：.endファイルが作成されたらそのPathを返すか")
    public  void should_returnThePath_when_createdEndFile () throws Exception {
        watcher = new Watcher();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //Callableの実装
        Callable<Path> task = () -> watcher.watcher(tempDir);
        //ExecutorServiceにタスクを渡す
        Future<Path> future = executor.submit(task);
        //監視が開始されるまで待つ
        Thread.sleep(300);
        //ファイル作成
        Path endFile = tempDir.resolve("Hoge.end");
        Files.createFile(endFile);
        //Path取得（3秒だけ待つ）
        Path actPath = future.get(3, TimeUnit.SECONDS);
        //検証
        assertEquals(tempDir.resolve("Hoge.end"), actPath);
        //Thread開放
        executor.shutdownNow();
    }

    @Test
    @DisplayName ("異常系：割り込まれたらStoppedExceptionを投げるか")
    public void should_throwStoppedException_when_interrupted() throws Exception {
        watcher = new Watcher();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //Callableの実装
        Callable<Path> task = () -> watcher.watcher(tempDir);
        //ExecutorServiceにタスクを渡す
        Future<Path> future = executor.submit(task);
        //監視が開始されるまで待つ
        Thread.sleep(300);
        //監視しているThreadにinterrupt()を呼ぶ
        executor.shutdownNow();
        //
        Throwable cause = null;
        try {
            Path actPath = future.get(3, TimeUnit.SECONDS);
        } catch(ExecutionException e) {
            cause = e.getCause();
        }
        assertInstanceOf(StoppedException.class, cause);
    }
}