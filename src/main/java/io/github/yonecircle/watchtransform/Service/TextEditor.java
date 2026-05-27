package io.github.yonecircle.watchtransform.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.util.regex.Pattern;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.util.regex.Matcher;

public class TextEditor {
    ///////////////////////////////////////////////////////////////////////////////////////
    //ファイルを編集して、ファイル名にSuffixを付ける
    //return:無し
    //Note:suffixを外部入力化
    ///////////////////////////////////////////////////////////////////////////////////////
    public void edit(Path textfilePath) throws SystemException {

        //ハードコーディング
        String KEYWORD_NAME = "Name";
        String suffix = "_Suffix";
        String KEYWORD_DATA_NUMBER = "DataNumber";
        String sw_datalist = "DataList";
        int newDataNum = 0;
        int datalistRow = 0;

        //例外処理テスト用
        //throw new WXException("テキストファイルの読み込みに失敗しました", null);/*

        //テキストファイルをメモリに一括読み込み
        try{
            List<String> lines = Files.readAllLines(textfilePath, StandardCharsets.UTF_8);

            //Listを走査
            for(int i = 0; i < lines.size(); i++){
                String line = lines.get(i);

                //"Name"がある行の最後の空白を消してSuffixを付け足す
                if (line.contains(KEYWORD_NAME)){
                    String newline_name = line.stripTrailing() + suffix;
                    lines.set(i, newline_name);
                }
                //"DataNumber"がある行の値の数字を半分にする
                else if (line.contains(KEYWORD_DATA_NUMBER)){
                    Pattern dataNumberPattern = Pattern.compile(Pattern.quote(KEYWORD_DATA_NUMBER) + "\\s(\\d+)");
                    Matcher dataNumberMatcher = dataNumberPattern.matcher(line);
                    if (dataNumberMatcher.find()){
                        String datanumStr = dataNumberMatcher.group(1);
                        int originalDataNum = Integer.parseInt(datanumStr);
                        System.out.println("データ数は" + originalDataNum);
                        //DataNumが偶数なら単純に半分
                        if (originalDataNum % 2 == 0) {
                            newDataNum = originalDataNum / 2;
                        }
                        //DataNumが奇数なら商＋１
                        if (originalDataNum % 2 == 1) {
                            newDataNum = originalDataNum / 2 + 1;
                        }
                        System.out.println("新データ数は" + newDataNum);
                        String newLine_newDataNum = dataNumberMatcher.replaceFirst(KEYWORD_DATA_NUMBER + " " + newDataNum);
                        lines.set(i, newLine_newDataNum);
                    }
                }
                //"DataList"がある行の値を新データ数に書き換える（KEYWORD_DATA_NUMBERと同列のelse ifに修正）
                else if (line.contains(sw_datalist)){
                    datalistRow = i; //「DataList」という文字列のある行数を記録
                    Pattern p_dl = Pattern.compile(Pattern.quote(sw_datalist) + "\\s(\\d+)");
                    Matcher m_dl = p_dl.matcher(line);
                    if(m_dl.find()) {
                        String newLine_newdatalist = m_dl.replaceFirst(sw_datalist + " " + newDataNum);
                        lines.set(i, newLine_newdatalist);
                        System.out.println("行数は" + datalistRow);
                    }
                }
            }

            //「DataList」という文字列の下の行から新データ数分だけ下に下がって、それ以降の行は全て削除する
            int deleteFromHere = datalistRow + newDataNum + 1;
            List<String> subList = lines.subList(0, deleteFromHere);

            //subListの最後の行の最後に;を付け足す
            int lastIndex = subList.size() - 1;
            String newLastRow = subList.get(lastIndex) + ";";
            subList.set(lastIndex, newLastRow);

            //編集後のsubListをファイルに書き戻す
            Files.write(textfilePath, subList, StandardCharsets.UTF_8);

        }catch(IOException ioEx){
            throw new SystemException("テキストファイルの読み込みに失敗しました" ,ioEx);
        }
        //ここにコメントブロック
    }
}