package com.example.demo;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import com.example.demo.Service.TextCopy;
import com.example.demo.Service.TextEditor;
import com.example.demo.Service.TextRenamePaste;
import com.example.demo.Service.XENDGenerator;
import com.example.demo.Service.XENDPaste;

import org.springframework.stereotype.Service;

@Service  //SplingBootに認識されるようになった
public class ServiceProcess {

    //テスト用メインメソッド
    //public static void main(String[] args){
    public void execute(Path endFilePath, Path endFolderPath, Path textFolderPath, Path tempFolderPath, String returnCode, String suffixMode) {

        ServiceProcess SP = new ServiceProcess();

        //endFilePathからendFileNameを取り出す
        String endfileName = endFilePath.getFileName().toString();

        //TextCopy
        Path copysource_object= SP.resolveTxtPathFromEndFile(endfileName, textFolderPath);
        Path copytarget_object = SP.resolveCopytargetPath(endfileName, tempFolderPath);
        TextCopy TP = new TextCopy();
        TP.copytext(copysource_object, copytarget_object);

        //TextEdit
        TextEditor TE = new  TextEditor();
        TE.textedit(copytarget_object);

        //TextRenamePaste
        Path text_renametarget_Object;
        if ("0".equals(suffixMode)) {
            text_renametarget_Object = SP.resolveRenameTargetPathSuffixMode0(textFolderPath, SP.remove_extension(endfileName));
        } else {
            text_renametarget_Object = SP.resolveRenameTargetPathSuffixMode1(textFolderPath, SP.remove_extension(endfileName));
        }
        TextRenamePaste TRP = new TextRenamePaste();
        TRP.textrenamepaste(copytarget_object, text_renametarget_Object, suffixMode);

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
    //static String textFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\textFileFolder";
    //static String endFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\endFileFolder";
    //static String tempFolderPath = "C:\\Users\\Device2\\workspace\\WatchAndTransform\\tempFileFolder";
    //static String endfileName = "Hoge.end";
    //static String returnCode = "0";

    //インスタンスメソッド 
    //拡張子を除くメソッド
    public String remove_extension(String absoPath){
        File file = new File(absoPath);
        String basename = file.getName();
        String wotext = basename.substring(0, basename.lastIndexOf("."));
        return wotext;
    }
    //検知したendファイルからコピーすべきtxtファイルのPathを返す
    Path resolveTxtPathFromEndFile(String fileName, Path txtfolderPath){
        String fileName_without_extention = remove_extension(fileName);
        String txtfolderPath_String = txtfolderPath.toString();
        String txtfilePath_String = txtfolderPath_String + "\\" + fileName_without_extention + ".txt";
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //検知したendファイルからtxtファイルのコピー先のPathを返す
    Path resolveCopytargetPath(String fileName, Path tempfolderPath){
        String fileName_without_extention = remove_extension(fileName);
        String tempfolderPath_String = tempfolderPath.toString();
        String txtfilePath_String = tempfolderPath_String + "\\" + fileName_without_extention + ".txt";
        System.out.println(txtfilePath_String); //確認用
        Path txtfilePath_Object = Paths.get(txtfilePath_String);
        return txtfilePath_Object;
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=0
    Path resolveRenameTargetPathSuffixMode0(Path textFolderPath, String fileName){
        String textFolderPath_String = textFolderPath.toString();
        String renameTargetTextPath_String = textFolderPath_String + "\\" + fileName +"_Suffix.txt";
        Path renameTargetTextPath_Object = Paths.get(renameTargetTextPath_String);
        return renameTargetTextPath_Object;
    }
    //RenameしてPasteするファイルのパスオブジェクトを返す SuffixMode=1
    Path resolveRenameTargetPathSuffixMode1(Path textFolderPath, String fileName){
        String textFolderPath_String = textFolderPath.toString();
        int lastIndex = fileName.lastIndexOf("_");
        String fileName_withoutSuffix = fileName.substring(0, lastIndex);
        String renameTargetTextPath_String = textFolderPath_String + "\\" + fileName_withoutSuffix +".txt";
        Path renameTargetTextPath_Object = Paths.get(renameTargetTextPath_String);
        return renameTargetTextPath_Object;
    }
    //FFENDファイルの生成先パスオブジェクトを返す
    Path resolve_generateTargetXENDPath(Path tempFileFolder, String fileName){
        String tempFileFolder_String = tempFileFolder.toString();
        String generateTargetPath_String = tempFileFolder_String + "\\" + fileName + ".XEND";
        Path generateTargetPath_PathObject = Paths.get(generateTargetPath_String);
        return generateTargetPath_PathObject;
    }
    //FFENDファイルのペースト先パスオブジェクトを返す
    Path resolve_pasteTargetXENDPath(Path endFolderPath, String FileName){
        String endFolderPath_String = endFolderPath.toString();
        String pasteTargetXENDPath_String = endFolderPath_String + "\\" + FileName + ".XEND";
        Path pasteTargetXENDPath_PathObject = Paths.get(pasteTargetXENDPath_String);
        return pasteTargetXENDPath_PathObject;
    }
}