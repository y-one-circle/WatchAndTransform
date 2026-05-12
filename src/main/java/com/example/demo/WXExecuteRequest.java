package com.example.demo;

public class WXExecuteRequest {
    
    private String endfileFolderPath;
    private String txtFolderPath;
    private String tempFolderPath;
    private String returnCode;
    private String suffixMode;

    //EndfileFolderPath=====================================================================================================================
    public String getEndfileFolderPath() {
        return endfileFolderPath;
    }

    public void setEndfileFolderPath(String endfileFolderPath) {
        //nullチェック
        if (endfileFolderPath == null) {
            this.endfileFolderPath = null;
            return; //nullだったらすぐ返す
        } 
        //空文字チェック
        if  (endfileFolderPath.isEmpty()) {
            this.endfileFolderPath = "";
            return; //空文字だったらすぐ返す
        }
        /*
        String normalizedEndFileFolderPtah = endfileFolderPath.replace("\"", "");  //"を消す
        char lastChar = normalizedEndFileFolderPtah.charAt(normalizedEndFileFolderPtah.length() -1);    //最後の文字が"であるかチェック
                if (lastChar != '\\') { //最後の文字が"でないなら付け足す
                    normalizedEndFileFolderPtah = normalizedEndFileFolderPtah + "\\";
                }
        */
        this.endfileFolderPath = endfileFolderPath;
    }

    //TxtFolderPath=====================================================================================================================
    public String getTxtFolderPath() {
        return txtFolderPath;
    }

    public void setTxtFolderPath(String txtFolderPath) {
        //nullチェック
        if (txtFolderPath == null) {
            this.txtFolderPath = null;
            return; //nullだったらすぐ返す
        }
        //空文字チェック
        if (txtFolderPath.isEmpty()) {
            this.txtFolderPath = "";
            return; //空文字だったらすぐ返す
        }
        /* 
        String normalizedTxtFolderPath = txtFolderPath.replace("\"", "");    //"を消す
        char lastChar = normalizedTxtFolderPath.charAt(normalizedTxtFolderPath.length() -1);    ////最後の文字が"であるかチェック
            if (lastChar != '\\') { //最後の文字が"でないなら付け足す
                normalizedTxtFolderPath = normalizedTxtFolderPath + "\\";
            }
        */
        this.txtFolderPath = txtFolderPath;
    }
    //TempFolderPath=====================================================================================================================
    public String getTempFolderPath() {
        return tempFolderPath;
    }

    public void setTempFolderPath(String tempFolderPath) {
        //nullチェック
        if (tempFolderPath == null) {
            this.tempFolderPath = null;
            return; //nullだったらすぐ返す
        }
        //空文字チェック
        if (tempFolderPath.isEmpty()) {
            this.tempFolderPath = "";
            return; //空文字だったらすぐ返す
        }
        /* 
        String normalizedTempFolderPath = tempFolderPath.replace("\"", "");    //"を消す
        char lastChar = normalizedTempFolderPath.charAt(normalizedTempFolderPath.length() -1);    ////最後の文字が"であるかチェック
            if(lastChar != '\\') { //最後の文字が"でないなら付け足す
                normalizedTempFolderPath = normalizedTempFolderPath + "\\";
            }
        */
        this.tempFolderPath = tempFolderPath;
    }
    //ReturnCode=====================================================================================================================
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        //nullチェック
        if (returnCode == null) {
            this.returnCode = null;
            return; //nullだったらすぐ返す
        }
        //空文字チェック
        if (returnCode.isEmpty()) {
            this.returnCode = "";
            return; //空文字だったらすぐ返す
        }
        this.returnCode = returnCode;
    }
    //SuffixMode=====================================================================================================================
    public String getSuffixMode() {
        return suffixMode;
    }

    public void setSuffixMode(String suffixMode) {
        this.suffixMode = suffixMode;
    }
}
