package com.example.demo;
import java.io.File;

public class ServiceProcess {

    //テスト用メインメソッド
    public static void main(String[] args){
    
        ServiceProcess SP = new ServiceProcess();

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
    //検知したendファイルからコピーすべきtxtファイルのPathを特定
    String resolveTxtPathFromEndFile(String fileName, String folderPath){

    }
}
