package io.github.yonecircle.watchtransform.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.WXException;

import java.io.IOException;

public class XENDGenerator {
    ///////////////////////////////////////////////////////////////////////////////////////
    //REturnCodeが記載されたXENDファイルを指定のPathに生成する
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendGenerator(Path generateTargetPath, String ReturnCode){
//例外処理テスト用
        //throw new WXException("XENDファイルの生成に失敗しました", null);/* 

    try{
    //空のXENDファイル作成
    Files.createFile(generateTargetPath);
    //ReturnCodeを書き込み
    Files.writeString(generateTargetPath, ReturnCode, StandardCharsets.UTF_8);
    }catch(IOException ioEx){
        throw new WXException("XENDファイルの生成に失敗しました", ioEx);
        }
    }
}
