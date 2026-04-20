package com.example.demo.Service;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

public class TextCopy {
//Ptahオブジェクトからテキストファイルをコピー
    public void copytext(Path copysourcePath, Path copytargetPath){
        try{
            Files.copy(copysourcePath, copytargetPath);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}