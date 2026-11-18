package io.github.yonecircle.watchtransform.dto;

public class SettingSaveRequest {

    private String settingName;
    private String endfileFolderPath;
    private String txtFolderPath;
    private String tempFolderPath;
    private String returnCode;
    private Boolean suffixMode;
    public String getSettingName() {
        return settingName;
    }
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
    public String getEndfileFolderPath() {
        return endfileFolderPath;
    }
    public void setEndfileFolderPath(String endfileFolderPath) {
        this.endfileFolderPath = endfileFolderPath;
    }
    public String getTxtFolderPath() {
        return txtFolderPath;
    }
    public void setTxtFolderPath(String txtFolderPath) {
        this.txtFolderPath = txtFolderPath;
    }
    public String getTempFolderPath() {
        return tempFolderPath;
    }
    public void setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
    }
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public Boolean getSuffixMode() {
        return suffixMode;
    }
    public void setSuffixMode(Boolean suffixMode) {
        this.suffixMode = suffixMode;
    }

}
