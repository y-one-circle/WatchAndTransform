package io.github.yonecircle.watchtransform.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.exception.SystemException;

@Service
public class TextCopy {
    ///////////////////////////////////////////////////////////////////////////////////////
    //Pathオブジェクトを受け取ってコピーする
    //return:無し
    //Note:入出力エラー発生時にはServiceProcessクラスにエラーメッセージとThrowableを投げる
    ///////////////////////////////////////////////////////////////////////////////////////
    public void copy(Path copysourcePath, Path copytargetPath)throws SystemException{

        try{
            Files.copy(copysourcePath, copytargetPath);
        } catch(IOException ioEx){
            throw new SystemException("テキストファイルのコピーに失敗しました", ioEx);
        }
        
    }
}