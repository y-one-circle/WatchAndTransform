package com.example.demo;
import java.io.File;

public class TextCopy {
    //テスト用メインメソッド
    public static void main(String[] args){
    TextCopy textcopy = new TextCopy();
    }

//フィールド変数（仮）
    String textfilePath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\TextFolder";
    String endfilePath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\EndFileFolder";

//インスタンスメソッド
    //拡張子を除くメソッド
    public String remove_extension(String absoPath){
        File file = new File(absoPath);
        String basename = file.getName();
        String wotext = basename.substring(0, basename.lastIndexOf("."));
        return wotext;
    }

}