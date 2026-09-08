package io.github.yonecircle.watchtransform;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.exception.SystemException;

@Service
public class ConfigService {
    //このアプリを起動したディレクトリを取得して、その中に wx.properties というファイルのパスを作る
    private static final Path CONFIG_PATH =
        Paths.get(System.getProperty("user.dir"), "wx.properties");

    ////////////////////////////////////////////////////////////////////////////////////
    //Propertyファイルを読み込む
    //return:Properties
    //Note:PropertyオブジェクトにInputStreamでキーバリュー情報を流し込む
    ////////////////////////////////////////////////////////////////////////////////////
    public Properties loadProperties() throws SystemException {
        Properties props = new Properties();
        if (Files.exists(CONFIG_PATH)) {
            //try-with-resources構文
            //try の中で使うリソース（今回は InputStream）を宣言すると、 
            //tryブロックを抜けると自動的にin.close()を呼ぶ
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                props.load(input);
            } catch (IOException ioEx) {
                throw new SystemException("プロパティファイルの読み込みに失敗しました", ioEx);
            }
        }
        return props;
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //Propertyファイルに保存する
    //return:無し
    //Note:PropertyオブジェクトにInputStreamでキーバリュー情報を流し込む
    ////////////////////////////////////////////////////////////////////////////////////
    public void saveProperties(WXExecuteRequest req) throws SystemException {
        Properties props = new Properties();

        props.setProperty("endfileFolderPath", normalizeForProperties(req.getEndfileFolderPath()));
        props.setProperty("txtFolderPath", normalizeForProperties(req.getTxtFolderPath()));
        props.setProperty("tempFolderPath", normalizeForProperties(req.getTempFolderPath()));
        props.setProperty("returnCode", normalizeForProperties(req.getReturnCode()));

        try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
            props.store(output, "wx config");
        } catch (IOException ioEx) {
            throw new SystemException("プロパティファイルの保存に失敗しました", ioEx);
        }
    } 

    ////////////////////////////////////////////////////////////////////////////////////
    // "\\"を"//"に置き換える
    //return:Path
    ////////////////////////////////////////////////////////////////////////////////////
    private String normalizeForProperties(String path) {
        return path.replace("\\", "/");
    }
}
