package com.example.demo;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ServiceProcess {

    //テスト用メインメソッド
    public static void main(String[] args){
    
        ServiceProcess SP = new ServiceProcess();
        Path copysource_object= SP.resolveTxtPathFromEndFile(endfileName, textFolderPath);
        Path copytarget_object = SP.resolveCopytargetPath(endfileName,tempFolderPath);

        TextCopy TP = new TextCopy();
        TP.copytext(copysource_object, copytarget_object);
    }

    //フィールド変数（仮）上位クラスからの情報
    static String textFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\TextFolder";
    static String endFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\EndFileFolder";
    static String tempFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\temp";
    static String endfileName = "Hoge.end";

    //インスタンスメソッド 
    //拡張子を除くメソッド
    public String remove_extension(String absoPath){
        File file = new File(absoPath);
        String basename = file.getName();
        String wotext = basename.substring(0, basename.lastIndexOf("."));
        return wotext;
    }
    //検知したendファイルからコピーすべきtxtファイルのPathを返す
    Path resolveTxtPathFromEndFile(String fileName, String txtfolderPath){
        String fileName_without_extention = remove_extension(fileName);
        String txtfilePath_String = txtfolderPath + "\\" + fileName_without_extention + ".txt";
        //確認用
        System.out.println(txtfilePath_String);
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //検知したendファイルからtxtファイルのコピー先のPathを返す
    Path resolveCopytargetPath(String fileName, String tempfolderPath){
        String fileName_without_extention = remove_extension(fileName);
        String txtfilePath_String = tempfolderPath + "\\" + fileName_without_extention + ".txt";
        //確認用
        System.out.println(txtfilePath_String);
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
}
