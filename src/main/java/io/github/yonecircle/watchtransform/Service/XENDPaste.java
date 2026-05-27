package io.github.yonecircle.watchtransform.Service;
import java.nio.file.Files;
import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.io.IOException;

public class XENDPaste {
    ///////////////////////////////////////////////////////////////////////////////////////
    //XENDGeneratorで生成されたXENDファイルを指定のパスに移動する
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendPaste(Path sourcePath, Path targetPath) throws SystemException {
    //例外処理テスト用
        //throw new WXException("XENDファイルの移動に失敗しました", null);/*
    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException ioEx){
        throw new SystemException("XENDファイルの移動に失敗しました", ioEx);
        }
        
    }   
}
