package com.example.demo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    //このアプリを起動したディレクトリを取得して、その中に wx.properties というファイルのパスを作る
    private static final Path CONFIG_PATH =
        Paths.get(System.getProperty("user.dir"), "wx.properties");

    //設定ファイルを読み込むメソッド
    public Properties loadproperties(){
        Properties props = new Properties();
        if (Files.exists(CONFIG_PATH)) {
            /*
            try-with-resources構文
            try の中で使うリソース（今回は InputStream）を宣言すると、 
            tryブロックを抜けると自動的にin.close()を呼ぶ
            */
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                props.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }
    //設定ファイルを保存するメソッド
    public void saveproperties(WXExecuteRequest req) {
        Properties props = new Properties();

        props.setProperty("endfileFolderPath", normalizeForProperties(req.getEndfileFolderPath()));
        props.setProperty("txtFolderPath", normalizeForProperties(req.getTxtFolderPath()));
        props.setProperty("tempFolderPath", normalizeForProperties(req.getTempFolderPath()));
        props.setProperty("returnCode", normalizeForProperties(req.getReturnCode()));

        try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
            props.store(output, "wx config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    // \\を//に置き換えるメソッド
    private String normalizeForProperties(String path) {
        return path.replace("\\", "/");
    }
}
