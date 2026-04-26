package com.example.demo.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class XENDGenerator {
    public void xendgenerator(Path generateTargetPath, String ReturnCode){
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
