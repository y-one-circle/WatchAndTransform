package io.github.yonecircle.watchtransform.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.exception.SystemException;

@Service
public class XENDMove {
    ///////////////////////////////////////////////////////////////////////////////////////
    //XENDGeneratorで生成されたXENDファイルを指定のパスに移動する
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendMove(Path sourcePath, Path targetPath) throws SystemException {

    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException ioEx){
        throw new SystemException("XENDファイルの移動に失敗しました", ioEx);
        }
        
    }   
}
