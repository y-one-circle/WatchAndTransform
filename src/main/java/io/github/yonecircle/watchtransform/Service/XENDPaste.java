package io.github.yonecircle.watchtransform.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class XENDPaste {
    ///////////////////////////////////////////////////////////////////////////////////////
    //XENDGeneratorで生成されたXENDファイルを指定のパスに移動する
    //return:無し
    //Note:例外処理未実装
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendPaste(Path sourcePath, Path targetPath){
    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException e){
        e.printStackTrace();
        }
    }   
}
