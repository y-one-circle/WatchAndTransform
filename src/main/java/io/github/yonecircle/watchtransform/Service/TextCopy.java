package io.github.yonecircle.watchtransform.Service;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

public class TextCopy {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってコピーする
    //return:無し
    //Note:例外処理未実装
    ///////////////////////////////////////////////////////////////////////////////////////
    public void copy(Path copysourcePath, Path copytargetPath){
        try{
            Files.copy(copysourcePath, copytargetPath);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}