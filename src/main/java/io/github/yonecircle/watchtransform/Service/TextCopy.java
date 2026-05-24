package io.github.yonecircle.watchtransform.Service;

import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.WXException;

import java.io.IOException;
import java.nio.file.Files;

public class TextCopy {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってコピーする
    //return:無し
    //Note:入出力エラー発生時にはServiceProcessクラスにエラーメッセージとThrowableを投げる
    ///////////////////////////////////////////////////////////////////////////////////////
    public void copy(Path copysourcePath, Path copytargetPath)throws Exception{
        //例外処理テスト用
        //throw new WXException("コピーに失敗しました", null);/*

        try{
            Files.copy(copysourcePath, copytargetPath);
        } catch(IOException ioEx){
            throw new WXException("テキストファイルのコピーに失敗しました", ioEx);
        }
        
    }
}