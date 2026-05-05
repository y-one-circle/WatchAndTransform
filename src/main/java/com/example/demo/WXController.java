package com.example.demo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wx")
public class WXController {
    
    private final ServiceProcess serviceprocess;
    private final Watcher watcher;
    private final ConfigService configService;

    public WXController(ServiceProcess serviceprocess, Watcher watcher, ConfigService configService) {
        this.serviceprocess = serviceprocess;
        this.watcher = watcher;
        this.configService = configService;
    }

    @PostMapping("/execute")
    public String execute(@RequestBody WXExecuteRequest dto) {
        System.out.println("=== execute() called ===");
        System.out.println("endfileFolderPath = " + dto.getEndfileFolderPath());
        System.out.println("txtFolderPath  = " + dto.getTxtFolderPath());
        System.out.println("tempFolderPath    = " + dto.getTempFolderPath());
        System.out.println("returnCode        = " + dto.getReturnCode());

        try {
            //ユーザ入力String→ロジック用正規化Path
            PathNormalizer normalizer = new PathNormalizer();
            Path endFileDir = normalizer.pathnormalizer(dto.getEndfileFolderPath());
            Path txtFileDir = normalizer.pathnormalizer(dto.getTxtFolderPath());
            Path tempFilerDir = normalizer.pathnormalizer(dto.getTempFolderPath());

            Path endFilePath = watcher.watcher(Paths.get(dto.getEndfileFolderPath()));

            serviceprocess.execute(
                endFilePath,
                endFileDir,
                txtFileDir,
                tempFilerDir,
                dto.getReturnCode(), 
                dto.getSuffixMode()
            );

            return "処理が完了しました";
        } catch (Exception e) {
            return "エラー: " + e.getMessage();
        }
    }
    //設定を保存する
    @PostMapping("/config")
    public void  saveConfig(@RequestBody WXExecuteRequest dto) {
        configService.saveproperties(dto);
    }

    //起動時に設定を読み込む
    @GetMapping("/config")
    public WXExecuteRequest loadConfig(){
        Properties props = configService.loadproperties();

        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(props.getProperty("endfileFolderPath", "")); 
        dto.setTxtFolderPath(props.getProperty("txtFolderPath", ""));
        dto.setTempFolderPath(props.getProperty("tempFolderPath", ""));
        dto.setReturnCode(props.getProperty("returnCode", ""));
        return dto;
    }
}
