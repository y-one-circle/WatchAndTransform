package io.github.yonecircle.watchtransform.service;

import java.nio.file.Path;

import io.github.yonecircle.watchtransform.exception.SystemException;

import java.io.IOException;
import java.nio.file.Files;

public class TextCopy {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Psthオブジェクトを受け取ってコピーする
    //return:無し
    //Note:入出力エラー発生時にはServiceProcessクラスにエラーメッセージとThrowableを投げる
    ///////////////////////////////////////////////////////////////////////////////////////
    public void copy(Path copysourcePath, Path copytargetPath)throws SystemException{
        //例外処理テスト用
        //throw new WXException("コピーに失敗しました", null);/*

        try{
            Files.copy(copysourcePath, copytargetPath);
        } catch(IOException ioEx){
            throw new SystemException("テキストファイルのコピーに失敗しました", ioEx);
        }
        
    }
}