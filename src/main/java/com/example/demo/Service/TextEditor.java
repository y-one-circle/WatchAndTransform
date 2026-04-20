package com.example.demo.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TextEditor {
    public void textedit(Path textfilePath){
        //ハードコーディング
        String sw_name = "Name";
        String suffix = "_Suffix";
        String sw_datanumber = "DataNumber";
        String sw_datalist = "DataList";
        int newDataNum = 0;
        int datalistRow = 0;
        //テキストファイルをメモリに一括読み込み
        try{
            List<String>lines = Files.readAllLines(textfilePath, StandardCharsets.UTF_8);
            //Listを走査
            for(int i= 0 ; i < lines.size(); i++){
            String line = lines.get(i);   
                //"Name"がある行の最後の空白を消してSuffixを付け足す
                if (line.contains(sw_name)){
                    String newline_name = line.stripTrailing() + suffix;
                    lines.set(i, newline_name);
                }
                //"DataNumber"がある行の値の数字を半分にする
                else if (line.contains(sw_datanumber)){
                    //変数を正規表現に入れるのでquoteで記号無効化
                    Pattern p_dn = Pattern.compile(Pattern.quote(sw_datanumber) + "\\s(\\d+)");
                    Matcher m_dn = p_dn.matcher(line);
                    if (m_dn.find()){
                        String datanumStr = m_dn.group(1);
                        int originalDataNum = Integer.parseInt(datanumStr);
                        System.out.println("データ数は" + originalDataNum);
                        //DataNumが偶数なら単純に半分
                        if (originalDataNum % 2 ==0) {
                            newDataNum = originalDataNum / 2; //
                        }
                        //DataNumが奇数なら商＋１
                        if (originalDataNum % 2 ==1) {
                            newDataNum = originalDataNum / 2 + 1;
                        }
                    System.out.println("新データ数は" + newDataNum);  //デバッグ  
                    String newLine_newDataNumn = m_dn.replaceFirst(sw_datanumber + " " + newDataNum);
                    lines.set(i, newLine_newDataNumn);
                    }
                    else if(line.contains(sw_datalist)) {
                        datalistRow = i; //「DataList」という文字列のある行数を記録
                        //変数を正規表現に入れるのでquoteで記号無効化
                        Pattern p_dl = Pattern.compile(Pattern.quote(sw_datalist) + "\\s(\\d+)");
                        Matcher m_dl = p_dl.matcher(line);

                        if(m_dl.find()) {
                            String newLine_newdatalist = m_dl.replaceFirst(sw_datalist + " " + newDataNum);
                            lines.set(i, newLine_newdatalist);
                             System.out.println("行数は" + datalistRow);//消す
                        }
                    }
                }
                // //「DefectDataList」という文字列の下の行から新欠陥数分だけ下に下がって、それ以降の行は全て削除する
                int deleteFromHere = datalistRow + newDataNum + 1;
                List <String>subList = lines.subList(0, deleteFromHere);
                //subListの最後の行の最後に;を付け足す
                int LastRow_subLsit = subList.size() - 1;
                String newLastRow = subList.get(LastRow_subLsit) + ";";
                 subList.set(LastRow_subLsit, newLastRow);

            }
            //全行をファイルに書き込む
            Files.write(textfilePath, lines, StandardCharsets.UTF_8);
        }catch(IOException e){
            e.printStackTrace();
        }
    
    }
    }
