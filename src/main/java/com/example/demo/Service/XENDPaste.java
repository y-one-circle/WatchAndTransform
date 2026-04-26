package com.example.demo.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class XENDPaste {
    public void xendpaste(Path sourcePath, Path targetPath){
    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException e){
        e.printStackTrace();
        }
    }   
}
