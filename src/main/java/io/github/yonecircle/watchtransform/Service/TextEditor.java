package io.github.yonecircle.watchtransform.service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.ArrayList;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.util.regex.Matcher;

public class TextEditor {
    ///////////////////////////////////////////////////////////////////////////////////////
    //ファイルを編集して、ファイル名にSuffixを付ける
    //return:無し
    //Note:suffixを外部入力化
    ///////////////////////////////////////////////////////////////////////////////////////
    public void edit(Path textfilePath) throws SystemException {

        try{
            //読み込み
            List<String> lines = Files.readAllLines(textfilePath, StandardCharsets.UTF_8);
            //変換
            List<String> taransformedLines = transform(lines);
            //編集後のsubListをファイルに書き戻す
            Files.write(textfilePath, taransformedLines, StandardCharsets.UTF_8);
        }catch(IOException ioEx){
            throw new SystemException("テキストファイルの読み込みに失敗しました" ,ioEx);
        }
    }

    List<String> transform(List<String> lines) {
        HalveResult halveResult = null;

        List<String> resultList = new ArrayList<>();
        //Listを走査
        for(int i = 0; i < lines.size(); i++){

            String line = lines.get(i);
            //"Name"で始まる行
            if (line.startsWith("Name")) {
                resultList.add(addSuffixToNameLine(line));
             //"DataNumber"で始まる行
            } else if (line.startsWith("DataNumber")) {
                halveResult = halveNumber(line, "DataNumber", i);
                resultList.add(halveResult.line);
            } else if (line.startsWith("DataList")) {
                halveResult = halveNumber(line, "DataList", i);
                resultList.add(halveResult.line);
            } else {
                resultList.add(line);
            }
        }
        resultList = trimLines(resultList, halveResult);
        return resultList;
    } 
//==================================================================================================
    //最後の空白を消してSuffixを付け足す
    String addSuffixToNameLine(String line) {
         String newline_name = line.stripTrailing();
         newline_name = newline_name.replace(".", "_Suffix.");
         return newline_name;
    }

    //数字を半分にする
    HalveResult halveNumber(String line, String keyword, int datalistRow) {
        
        String newLine_newDataNum = null;
        int newDataNum = 0;
        boolean trimed = false;

        Pattern dataNumberPattern = Pattern.compile(Pattern.quote(keyword) + "\\s(\\d+)");
            Matcher dataNumberMatcher = dataNumberPattern.matcher(line);
            if (dataNumberMatcher.find()){
                String datanumStr = dataNumberMatcher.group(1);
                int originalDataNum = Integer.parseInt(datanumStr);
                //元データ数が1より大きければデータ数を削減する
                if (originalDataNum > 1) {
                    trimed = true;
                }
                //引数が偶数なら単純に半分
                if (originalDataNum % 2 == 0) {
                    newDataNum = originalDataNum / 2;
                }
                //引数が奇数なら商＋１
                if (originalDataNum % 2 == 1) {
                    newDataNum = originalDataNum / 2 + 1;
                }
                newLine_newDataNum = dataNumberMatcher.replaceFirst(keyword + " " + newDataNum);
                    }
        return new HalveResult (newLine_newDataNum, newDataNum, datalistRow, trimed);
    }


    //「DataList」という文字列の下の行から新データ数分だけ下に下がって、それ以降の行は全て削除する
    List<String> trimLines(List<String> resultList, HalveResult halveResult) {
        int deleteFromHere = halveResult.datalistRow + halveResult.newDataNum + 1;
        List<String> subresultList = resultList.subList(0, deleteFromHere);

        //trimedをしていたらsubListの最後の行の最後に;を付け足す
        if (halveResult.trimed) {
            int lastIndex = subresultList.size() - 1;
            String newLastRow = subresultList.get(lastIndex) + ";";
            subresultList.set(lastIndex, newLastRow);
        }
        return subresultList;
    }

    //内部クラス
    //TextEditorクラスのフィールドや状態に依存していないためstaticクラスとして実装
    private static class HalveResult{
        String line;
        int newDataNum;
        int datalistRow;
        boolean trimed;

        HalveResult(String line, int newDataNum, int datalistRow ,boolean trimed) {
            this.line = line;
            this.newDataNum = newDataNum;
            this.datalistRow = datalistRow;
            this.trimed = trimed;
        }
    }
}