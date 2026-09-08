package io.github.yonecircle.watchtransform;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceProcessTest {

    private StatusHolder statusHolder;
    private Watcher watcher;
    private ServiceProcess serviceProcess = new ServiceProcess(statusHolder, watcher);
    Path tempDir;
    Path endFileDir;
    Path textFileDir;
    Path tempFileDir;
    String endfileNameSuffix0 = "Hoge.end";
    String endfileNameSuffix1 = "Hoge_Suffix.end";

    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {

        this.tempDir = tempDir;
        //endFileDir作成
        Path endFileDir = tempDir.resolve("endFileDir");
        Files.createDirectory(endFileDir);
        this.endFileDir = endFileDir;
        //textFileDir作成
        Path textFileDir = tempDir.resolve("textFileDir");
        Files.createDirectory(textFileDir);
        this.textFileDir = textFileDir;
        //tempFileDir作成
        Path tempFileDir = tempDir.resolve("tempFileDir");
        Files.createDirectory(tempFileDir);
        this.tempFileDir = tempFileDir;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //以下パス解決のための補助メソッド群のテスト系
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("拡張子を除いた文字列を返すか")
    public void should_removeExtension () {
        String expectedString = "Hoge";
        assertEquals(serviceProcess.removeExtension(endfileNameSuffix0), expectedString,
        "拡張子が適切に取り除かれていません");
    }

    @Test
    @DisplayName("endファイル名からコピーすべきtxtファイルのPathを返すか")
    public void should_resolveTxtPathFromEndFile () {
        Path expected = Paths.get(textFileDir.toString() + "/Hoge.txt");
        Path actual = serviceProcess.resolveTxtPathFromEndFile(endfileNameSuffix0,textFileDir);
        assertEquals(expected, actual, "コピーすべきtxtファイルのPathが期待値と異なります");
    }

    @Test
    @DisplayName("endファイルからtxtファイルのコピー先のPathを返すか")
    public void should_resolveCopytargetPath () {
        Path expected = Paths.get(tempFileDir.toString() + "/Hoge.txt");
        Path actual = serviceProcess.resolveCopytargetPath(endfileNameSuffix0,tempFileDir);
        assertEquals(expected, actual, "コピー先txtファイルのPathが期待値と異なります");
    }
    @Test
    @DisplayName("RenameしてPasteするファイルのPathを返すか SuffixMode=0")
    public void should_resolveRenameTargetPathSuffixMode0 () {
        Path expected = Paths.get(textFileDir.toString() + "/Hoge_Suffix.txt");
        Path actual = serviceProcess.resolveRenameTargetPathSuffixMode0(textFileDir,endfileNameSuffix0);
        assertEquals(expected, actual, "RenameしてPasteするファイルのPathが期待値と異なります SuffixMode=0");
    }
    @Test
    @DisplayName("RenameしてPasteするファイルのPathを返すか SuffixMode=1")
    public void should_resolveRenameTargetPathSuffixMode1 () {
        Path expected = Paths.get(textFileDir.toString() + "/Hoge.txt");
        Path actual = serviceProcess.resolveRenameTargetPathSuffixMode1(textFileDir,"Hoge_Suffix.end");
        assertEquals(expected, actual, "RenameしてPasteするファイルが期待値と異なります SuffixMode=1");
    }
    @Test
    @DisplayName("FFENDファイルの生成先Pathを返すか")
    public void should_resolveGenerateTargetXendPath () {
        Path expected = Paths.get(tempFileDir.toString() + "/Hoge.XEND");
        Path actual = serviceProcess.resolveGenerateTargetXendPath(tempFileDir,endfileNameSuffix0);
        assertEquals(expected, actual, "FFENDファイルの生成先Pathが期待値と異なります");
    }
    @Test
    @DisplayName("FFENDファイルのペースト先Pathを返すか")
    public void should_resolvePasteTargetXendPath () {
        Path expected = Paths.get(endFileDir.toString() + "/Hoge.XEND");
        Path actual = serviceProcess.resolvePasteTargetXendPath(endFileDir,endfileNameSuffix0);
        assertEquals(expected, actual, "FFENDファイルのペースト先Pathが期待値と異なります");
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //transform()SuffixMode=0のテスト
    //transform()は各ServiceインスタンスにPathを渡しているだけのクラスなので
    //最終成果物であるSample_Suffix.txtとSample.XENDの存在確認のみをおこなう
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("Sample_Suffix.txtとSample.XENDが作成されるか")
    public void should_createTransformedTextAndXENDFile_when_Suffix0 () throws SystemException, IOException {
        //Path作成
        Path endFilePath = endFileDir.resolve(endfileNameSuffix0);
        Path transformedText = Paths.get(textFileDir.toString() + "/Hoge_Suffix.txt");
        Path xendFile = Paths.get(endFileDir.toString() + "/Hoge.XEND");
        //テキストファイル作成
        Path textFilePath = textFileDir.resolve("Hoge.txt");
        String sampleString = """
            Name Samaple.txt;
            DataNumber 7;
            DataList 7;
        	    1 alpha 0.4500
	            2 beta 0.0500
	            3 gamma 0.2500
	            4 delta 0.4000
	            5 epsilon 0.5250
	            6 zeta 0.1000
	            7 eta 0.5750;""";
        Files.writeString(textFilePath, sampleString);
        //実行
        serviceProcess.transform(endFilePath, endFileDir, textFileDir, tempFileDir, "0", "0");
        //検証
        assertAll(
            () -> assertTrue(Files.exists(transformedText)),
            () -> assertTrue(Files.exists(xendFile))
        );
    }

        ////////////////////////////////////////////////////////////////////////////////////
    //transform()SuffixMode=1のテスト
    //transform()は各ServiceインスタンスにPathを渡しているだけのクラスなので
    //最終成果物であるSample.txtとSample_Suffix.XENDの存在確認のみをおこなう
    ////////////////////////////////////////////////////////////////////////////////////
    @Test
    @DisplayName("Sample.txtとSample_Suffix.XENDが作成されるか")
    public void should_createTransformedTextAndXENDFile_when_Suffix1 () throws SystemException, IOException {
        //Path作成
        Path endFilePath = endFileDir.resolve(endfileNameSuffix1);
        Path transformedText = Paths.get(textFileDir.toString() + "/Hoge.txt");
        Path xendFile = Paths.get(endFileDir.toString() + "/Hoge_Suffix.XEND");
        //テキストファイル作成
        Path textFilePath = textFileDir.resolve("Hoge_Suffix.txt");
        String sampleString = """
            Name Samaple.txt;
            DataNumber 7;
            DataList 7;
        	    1 alpha 0.4500
	            2 beta 0.0500
	            3 gamma 0.2500
	            4 delta 0.4000
	            5 epsilon 0.5250
	            6 zeta 0.1000
	            7 eta 0.5750;""";
        Files.writeString(textFilePath, sampleString);
        //実行
        serviceProcess.transform(endFilePath, endFileDir, textFileDir, tempFileDir, "1", "1");
        //検証
        assertAll(
            () -> assertTrue(Files.exists(transformedText)),
            () -> assertTrue(Files.exists(xendFile))
        );
    }

    /*
    テストメソッドの命名規則「should_結果_when_条件」
    */
}
