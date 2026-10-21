package io.github.yonecircle.watchtransform.service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.exception.SystemException;

@Service
public class XENDGenerator {
    ///////////////////////////////////////////////////////////////////////////////////////
    //returnCodeが記載されたXENDファイルを指定のPathに生成する
    //return:無し
    ///////////////////////////////////////////////////////////////////////////////////////
    public void xendGenerator(Path generateTargetPath, String returnCode) throws SystemException {

    try{
        //空のXENDファイル作成
        Files.createFile(generateTargetPath);
        //ReturnCodeを書き込み
        Files.writeString(generateTargetPath, returnCode, StandardCharsets.UTF_8);
    }catch(IOException ioEx){
        throw new SystemException("XENDファイルの生成に失敗しました", ioEx);
        }
    }
}
