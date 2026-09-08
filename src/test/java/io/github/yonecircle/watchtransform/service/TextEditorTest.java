package io.github.yonecircle.watchtransform.service;

import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.yonecircle.watchtransform.exception.SystemException;

public class TextEditorTest {
    
    private TextEditor textEditor = new TextEditor();
    private Path textFilePath;

    //奇数List
    private static List<String> sampleListOddNumber = List.of(
        "Name SamapleOddNumber.txt;",
        "DataNumber 7;",
        "DataList 7;",
        "    1 alpha 0.4500",
        "    2 beta 0.0500",
        "    3 gamma 0.2500",
        "    4 delta 0.4000",
        "    5 epsilon 0.5250",
        "    6 zeta 0.1000",
        "    7 eta 0.5750;"
    );
    private static List<String> expectedListOddNumber = List.of(
        "Name SamapleOddNumber_Suffix.txt;",
        "DataNumber 4;",
        "DataList 4;",
        "    1 alpha 0.4500",
        "    2 beta 0.0500",
        "    3 gamma 0.2500",
        "    4 delta 0.4000;"
    );

    //偶数List
    private static List<String> sampleListEvenNumber = List.of(
        "Name SamapleEvenNumber.txt;",
        "DataNumber 8;",
        "DataList 8;",
        "    1 alpha 0.4500",
        "    2 beta 0.0500",
        "    3 gamma 0.2500",
        "    4 delta 0.4000",
        "    5 epsilon 0.5250",
        "    6 zeta 0.1000",
        "    7 eta 0.5750",
        "    8 theta 0.9200;"
    );
    private static List<String> expectedListEvenNumber = List.of(
        "Name SamapleEvenNumber_Suffix.txt;",
        "DataNumber 4;",
        "DataList 4;",
        "    1 alpha 0.4500",
        "    2 beta 0.0500",
        "    3 gamma 0.2500",
        "    4 delta 0.4000;"
    );

    //最小値List
    private static List<String> sampleListMinimum = List.of(
        "Name SamapleEvenNumber.txt;",
        "DataNumber 1;",
        "DataList 1;",
        "    1 alpha 0.4500;"
    );
    private static List<String> expectedListMinimum = List.of(
        "Name SamapleEvenNumber_Suffix.txt;",
        "DataNumber 1;",
        "DataList 1;",
        "    1 alpha 0.4500;"
    );

    //データ供給メソッド
    static Stream<Arguments> provideSmapleData() {
        return Stream.of(
            Arguments.of(sampleListOddNumber, expectedListOddNumber),
            Arguments.of(sampleListEvenNumber, expectedListEvenNumber),
            Arguments.of(sampleListMinimum, expectedListMinimum)
        );
    }



    ////////////////////////////////////////////////////////////////////////////////////
    //各テストで使用するサンプルデータファイル作成
    //return:無し
    //Note:
    ////////////////////////////////////////////////////////////////////////////////////
    void trueFileCreate(@TempDir Path tempDir) throws IOException {
        //空ファイル作成
        textFilePath = tempDir.resolve("Sample.txt");
        textFilePath = Files.createFile(textFilePath);
        //サンプルデータ書き込み
        Files.write(textFilePath, sampleListOddNumber, StandardCharsets.UTF_8);
    }
    
    @ParameterizedTest
    @MethodSource("provideSmapleData")
    @DisplayName("正常系：Listが期待どおりに変換されているか")
    void should_transformList(List<String> sampleList, List<String> expectedList) {
        //act
        List<String> actualList = textEditor.transform(sampleList);
        //assert
        assertEquals(expectedList, actualList, "Listが期待どおりに変換されていません");
    }

    @Test
    @DisplayName("異常系：テキストファイルが存在しない時にSystemExeptionを投げるか")
    public void should_throwSystemException_when_textFileIsMissing(@TempDir Path tempDir) {
        //存在しないsourceFileパスを作成
        Path nonExistFile = tempDir.resolve("nonExist.txt");
        assertThrows(SystemException.class, ()-> {textEditor.edit(nonExistFile);}, 
                    "ソースファイルが存在しない時にSystemExeptionを投げれていません");
    }





/*
テストメソッドの命名規則「should_結果_when_条件」
*/

    /* 
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
                */
}
