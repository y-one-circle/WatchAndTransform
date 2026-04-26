package com.example.demo;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import com.example.demo.Service.TextCopy;
import com.example.demo.Service.TextEditor;
import com.example.demo.Service.TextRenamePaste;
import com.example.demo.Service.XENDGenerator;
import com.example.demo.Service.XENDPaste;

public class ServiceProcess {

    //テスト用メインメソッド
    public static void main(String[] args){
        
        ServiceProcess SP = new ServiceProcess();

        //TextCopy
        Path copysource_object= SP.resolveTxtPathFromEndFile(endfileName, textFolderPath);
        Path copytarget_object = SP.resolveCopytargetPath(endfileName,tempFolderPath);
        TextCopy TP = new TextCopy();
        TP.copytext(copysource_object, copytarget_object);

        //TextEdit
        TextEditor TE = new  TextEditor();
        TE.textedit(copytarget_object);

        //TextRenamePaste
        Path text_renametarget_Object = SP.resolveRenameTargetPath(textFolderPath, SP.remove_extension(endfileName));
        TextRenamePaste TRP = new TextRenamePaste();
        TRP.textrenamepaste(copytarget_object, text_renametarget_Object);

        //XENDファイルの生成
        Path XEND_generateTarget_Object = SP.resolve_generateTargetXENDPath(tempFolderPath, SP.remove_extension(endfileName));
        XENDGenerator XG = new XENDGenerator();
        XG.xendgenerator(XEND_generateTarget_Object, returnCode);

        //XENDファイルのペースト
        Path XEND_pasteTargetPath_Object = SP.resolve_pasteTargetXENDPath(endFolderPath, SP.remove_extension(endfileName));
        XENDPaste XP = new XENDPaste();
        XP.xendpaste(XEND_generateTarget_Object, XEND_pasteTargetPath_Object);
    }

    //フィールド変数（仮）上位クラスからの情報
    static String textFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\textFileFolder";
    static String endFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\endFileFolder";
    static String tempFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\tempFileFolder";
    static String endfileName = "Hoge.end";
    static String returnCode = "0";

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
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //検知したendファイルからtxtファイルのコピー先のPathを返す
    Path resolveCopytargetPath(String fileName, String tempfolderPath){
        String fileName_without_extention = remove_extension(fileName);
        String txtfilePath_String = tempfolderPath + "\\" + fileName_without_extention + ".txt";
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す
    Path resolveRenameTargetPath(String textFolderPath, String fileName){
        String renameTargetTextPath_String = textFolderPath + "\\" + fileName +"_Suffix.txt";
        Path renameTargetTextPath_Object = Paths.get(renameTargetTextPath_String);
        return renameTargetTextPath_Object;
    }
    //FFENDファイルの生成先パスオブジェクトを返す
    Path resolve_generateTargetXENDPath(String tempFileFolder, String fileName){
        String generateTargetPath_String = tempFileFolder + "\\" + fileName + ".XEND";
        Path generateTargetPath_PathObject = Paths.get(generateTargetPath_String);
        return generateTargetPath_PathObject;
    }
    //FFENDファイルのペースト先パスオブジェクトを返す
    Path resolve_pasteTargetXENDPath(String endFolderPath, String FileName){
        String pasteTargetXENDPath_String = endFolderPath + "\\" + FileName + ".XEND";
        Path pasteTargetXENDPath_PathObject = Paths.get(pasteTargetXENDPath_String);
        return pasteTargetXENDPath_PathObject;
    }
}
