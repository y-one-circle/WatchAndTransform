package io.github.yonecircle.watchtransform;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.service.TextCopy;

public class TextCopyTest {
    
    private TextCopy textCopy;

    @BeforeEach
    Path[] setUp(@TempDir Path tempDir) throws IOException, SystemException {

        // 各テストの前にインスタンスを作成
        textCopy = new TextCopy();

        //テスト準備
        //フォルダ作成
        Path srcDir = tempDir.resolve("src");
        Path destDir = tempDir.resolve("dest");
        Files.createDirectories(srcDir);
        Files.createDirectories(destDir);
        //ファイル作成
        Path srcFile = srcDir.resolve("Text.txt");
        Files.writeString(srcFile, "Hoge");
        //コピー先パス作成
        Path destFile = destDir.resolve("Text.txt");
        //Path型配列を返す
        Path [] arrayPaths = {srcFile, destFile};
        return arrayPaths;
    }
    /*
    ①arrayPathsをフィールドとして持っておき、 @BeforeEach setUp()で帰ってきたPathをarrayPathsに格納する
    ②各テストメソッドの中でsetUp()を毎回呼んで、返り値としてarrayPathsを受け取る
    */

    @Test
    @DisplayName("コピーされたファイルが存在するか")
    void testExist (Path srcFile, Path destFile) throws IOException, SystemException {

        //テスト実行
        textCopy.copy(srcFile, destFile);

        //検証
        assertTrue(Files.exists(destFile), "コピー先にファイルが存在しません");

        //tempdir/src/src.txt
        //tempdir/dest/dest.txt
    }

    @Test
    @DisplayName("コピーしたファイルの中身が元ファイルと一致しているか")
    void testCopyContents (@TempDir Path tempDir) {

    }
}
