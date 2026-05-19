package io.github.yonecircle.watchtransform.Service;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

public class TextMove {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってファイルを移動させる
    //return:無し
    //Note:例外処理未実装
    ///////////////////////////////////////////////////////////////////////////////////////
    public void textMove(Path sourcePath, Path targetPath, String suffixMode){
    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException e){
        e.printStackTrace();
        }
    }
}
