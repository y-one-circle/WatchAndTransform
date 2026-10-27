package io.github.yonecircle.watchtransform;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.exception.ValidationException;

@Service 
public class WXExecuteRequestValidator {
    //nullまたは空チェック
    public void validateNotNullOrEmpty(WXExecuteRequest dto) throws ValidationException {

        ArrayList<String> valiMsgList = new ArrayList<>();

        if (dto.getEndfileFolderPath() == null || dto.getEndfileFolderPath().isEmpty()) {
            valiMsgList.add("監視フォルダのパスは入力されていません");
        }
        if (dto.getTxtFolderPath() == null || dto.getTxtFolderPath().isEmpty()) {
            valiMsgList.add("テキストフォルダのパスは入力されていません");
        }
        if (dto.getTempFolderPath() == null || dto.getTempFolderPath().isEmpty()) {
            valiMsgList.add("一時フォルダのパスは入力されていません");
        }
        if (dto.getReturnCode() == null || dto.getReturnCode().isEmpty()) {
            valiMsgList.add("リターンコードが入力されていません");
        }

        if (valiMsgList.isEmpty()) {
            return ;
        } else {
            throw new ValidationException(String.join(" ", valiMsgList));
        }
    }

    //nullチェック
    public void validateNotNull(WXExecuteRequest dto) throws ValidationException {

        ArrayList<String> valiMsgList = new ArrayList<>();
        
        if (dto.getEndfileFolderPath() == null) {
            valiMsgList.add("監視フォルダのパスに何も入力されていません");
        }
        if (dto.getTxtFolderPath() == null) {
            valiMsgList.add("テキストフォルダのパスに何も入力されていません");
        }
        if (dto.getTempFolderPath() == null) {
            valiMsgList.add("一時フォルダのパスに何も入力されていません");
        }
        if (dto.getReturnCode() == null) {
            valiMsgList.add("リターンコードに何も入力されていません");
        }

        if (valiMsgList.isEmpty()) {
            return ;
        } else {
            throw new ValidationException(String.join(" ", valiMsgList));
        }

    }
}
