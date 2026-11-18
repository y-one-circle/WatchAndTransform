package io.github.yonecircle.watchtransform.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "setting_info")
public class SettingInfo {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column 
    private String name;
    @Column
    private String textPath;
    @Column
    private String endFilePath;
    @Column
    private String tempPath;
    @Column
    private String returnCode;
    @Column
    private Boolean suffixMode;
    @Column(insertable = false)
    private OffsetDateTime createdAt;
    @Column(insertable = false)
    private OffsetDateTime updatedAt;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTextPath() {
        return textPath;
    }
    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }
    public String getEndFilePath() {
        return endFilePath;
    }
    public void setEndFilePath(String endFilePath) {
        this.endFilePath = endFilePath;
    }
    public String getTempPath() {
        return tempPath;
    }
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
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
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    
}
