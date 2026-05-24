package io.github.yonecircle.watchtransform.Service;
import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.WXException;

import java.nio.file.Files;
import java.io.IOException;

public class TextMove {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってファイルを移動させる
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void textMove(Path sourcePath, Path targetPath, String suffixMode){
    //例外処理テスト用
        //throw new WXException("編集したファイルの移動に失敗しました", null);/*

    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException ioEx){
            throw new WXException("編集したファイルの移動に失敗しました", ioEx);
        }
    }
}
