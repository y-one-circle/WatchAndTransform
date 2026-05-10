package com.example.demo.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class XENDGenerator {
    ///////////////////////////////////////////////////////////////////////////////////////
    //REturnCodeが記載されたXENDファイルを指定のPathに生成する
    //return:無し
    //Note:例外処理未実装
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendGenerator(Path generateTargetPath, String ReturnCode){
    try{
    //空のXENDファイル作成
    Files.createFile(generateTargetPath);
    //ReturnCodeを書き込み
    Files.writeString(generateTargetPath, ReturnCode, StandardCharsets.UTF_8);
    }catch(IOException e){
        e.printStackTrace();
        }
    }
}
