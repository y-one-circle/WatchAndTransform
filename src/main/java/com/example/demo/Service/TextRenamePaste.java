package com.example.demo.Service;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

public class TextRenamePaste {
    public void textrenamepaste(Path sourcePath, Path targetPath){
    try{
        Files.move(sourcePath, targetPath);
    }catch(IOException e){
        e.printStackTrace();
        }
    }
}
