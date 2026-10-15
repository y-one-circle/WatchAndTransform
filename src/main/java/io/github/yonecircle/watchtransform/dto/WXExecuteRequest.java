package io.github.yonecircle.watchtransform.dto;

////////////////////////////////////////////////////////////////////////////////////
//パス情報、コード、モードを伝える入力用DTO
////////////////////////////////////////////////////////////////////////////////////
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
        this.endfileFolderPath = endfileFolderPath;
    }
    //TxtFolderPath=====================================================================================================================
    public String getTxtFolderPath() {
        return txtFolderPath;
    }
    public void setTxtFolderPath(String txtFolderPath) {
        this.txtFolderPath = txtFolderPath;
    }
    //TempFolderPath=====================================================================================================================
    public String getTempFolderPath() {
        return tempFolderPath;
    }
    public void setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
    }
    //ReturnCode=====================================================================================================================
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
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
