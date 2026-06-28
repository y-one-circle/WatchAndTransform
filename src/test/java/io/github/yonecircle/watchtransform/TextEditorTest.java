package io.github.yonecircle.watchtransform;

import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.service.TextEditor;

public class TextEditorTest {
    
    private TextEditor textEditor = new TextEditor();
    private Path textFilePath;
    private String sampleDataOddNumber = """
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
    private String sampleDataEvenNumber = """
            Name Samaple.txt;
            DataNumber 8;
            DataList 8;
            	1 alpha 0.4500
                2 beta 0.0500
	            3 gamma 0.2500
	            4 delta 0.4000
	            5 epsilon 0.5250
	            6 zeta 0.1000
	            7 eta 0.5750
                8 theta 0.7800;""";

    ////////////////////////////////////////////////////////////////////////////////////
    //各テストで使用するサンプルデータファイル作成
    //return:無し
    //Note:
    ////////////////////////////////////////////////////////////////////////////////////
    @BeforeEach
    void trueFileCreate(@TempDir Path tempDir) throws IOException {
        //空ファイル作成
        textFilePath = tempDir.resolve("Sample.txt");
        textFilePath = Files.createFile(textFilePath);
        //サンプルデータ書き込み
        Files.writeString(textFilePath, sampleDataOddNumber, StandardCharsets.UTF_8);
    }
    
    @Test
    @DisplayName("正常系：Nameが存在する行の最後の空白を消してSuffixを付け足しているか")
    void should_addSuffix_on_lineWithName() throws SystemException, IOException {
        //実行
        textEditor.edit(textFilePath);
        //検証
            List<String> lines = Files.readAllLines(textFilePath, StandardCharsets.UTF_8);
            //Listを走査
            for (String line : lines) {
                if (line.contains("Name")) {
                    assertTrue(line.endsWith("_Suffix"), "Nameが存在する行の最後にSuffixがついていません");
                }
            }
    }

    @Test
    @DisplayName("正常系：DataNumberとDataListがある行の値の数字が半分になっていること（奇数）")
    void should_divideNumInHalf_on_lineWithDataNumberOrDataList() throws SystemException, IOException {
        //実行
        textEditor.edit(textFilePath);
        //検証
        List<String> lines = Files.readAllLines(textFilePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.contains("DataNumber") || line.contains("DataList")) {
                    
                }
            }
    }

    
/*
テストメソッドの命名規則「should_結果_when_条件」
*/
}
