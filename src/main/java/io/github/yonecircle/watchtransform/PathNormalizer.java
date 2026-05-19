package io.github.yonecircle.watchtransform;
import java.nio.file.Path;
import java.nio.file.Paths;

//ユーザ入力Path→ロジックPathに変換する正規化クラス
public class PathNormalizer {

    public Path pathnormalizer(String pathString) {
        //"を削除
        String pathStringWithout = pathString.replace("\"", "");
        //String→Path
        Path normalizedPath = Paths.get(pathStringWithout);
        return normalizedPath;
    } 
}