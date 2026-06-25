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

    private TextCopy textCopy = new TextCopy();
    private Path srcFile;
    private Path destFile;
    private Path tempDir;

    ////////////////////////////////////////////////////////////////////////////////////
    //各テストで使用するファイル作成
    //return:無し
    //Note:arrayPathsをフィールドとして持っておき、 @BeforeEach setUp()で帰ってきたPathをarrayPathsに格納する。
    //「各テストメソッドの中でsetUp()を毎回呼んで、返り値としてarrayPathsを受け取る」にしなかったのは前処理忘れ、
    //可読性の低下につながるから。
    ////////////////////////////////////////////////////////////////////////////////////
    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {

        //テスト準備
        this.tempDir = tempDir;
        //ファイル作成
        Path srcFile = tempDir.resolve("Text.txt");
        Files.writeString(srcFile, "Hoge");
        //コピー先パス作成
        Path destFile = tempDir.resolve("Text.txt");
        //フィールドにそれぞれのPathを代入
        this.srcFile = srcFile;
        this.destFile = destFile;
        //tempdir/src.txt
        //tempdir/dest.txt
    }
    /*
    テストメソッドの命名規則「should_結果_when_条件」
    */


    @Test
    @DisplayName("正常系：コピーされたファイルが存在するか")
    public void should_createDestinationFile_when_copyIsExcuted() throws SystemException {
        //コピー実行
        textCopy.copy(srcFile, destFile);
        //検証
        assertTrue(Files.exists(destFile), "コピー先にファイルが存在しません");
    }

    @Test
    @DisplayName("正常系：コピーしたファイルの中身が元ファイルと一致しているか")
    public void should_beSameContensWithSourceFile_when_copyIsExcuted() throws SystemException, IOException {
        //コピー実行
        textCopy.copy(srcFile, destFile);
        //検証
        assertEquals(Files.readString(srcFile), Files.readString(destFile), 
        "コピーしたファイルの中身が元ファイルと一致していません");
    }

    @Test
    @DisplayName("異常系：ソースファイルが存在しない時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_sourceFileIsMissing() {
        //存在しないsourceFileパスを作成
        Path nonExistFile = tempDir.resolve("nonExist.txt");
        assertThrows(SystemException.class, ()-> {textCopy.copy(nonExistFile, destFile);});
    }
    //追加テスト
    //コピー先ディレクトリが存在しない
    //上書き動作の確認
    //自己コピー
}

