package io.github.yonecircle.watchtransform.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.exception.SystemException;

@Service
public class TextMove {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってファイルを移動させる
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void textMove(Path sourcePath, Path targetPath) throws SystemException {

    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException ioEx){
            throw new SystemException("編集したファイルの移動に失敗しました", ioEx);
        }
    }
}
