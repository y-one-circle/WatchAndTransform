package com.example.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wx")
public class WXController {
    
    private final ServiceProcess serviceprocess;
    private final Watcher watcher;

    public WXController(ServiceProcess serviceprocess, Watcher watcher) {
        this.serviceprocess = serviceprocess;
        this.watcher = watcher;
    }

    @PostMapping("/execute")
    public String execute(@RequestBody WXExecuteRequest dto) {
        System.out.println("=== execute() called ===");
        System.out.println("endfileFolderPath = " + dto.getEndfileFolderPath());
        System.out.println("txtFolderPath  = " + dto.getTxtFolderPath());
        System.out.println("tempFolderPath    = " + dto.getTempFolderPath());
        System.out.println("returnCode        = " + dto.getReturnCode());

        try {
            Path endFilePath = watcher.watcher(Paths.get(dto.getEndfileFolderPath()));

            serviceprocess.execute(
                endFilePath,
                dto.getEndfileFolderPath(),
                dto.getTxtFolderPath(),
                dto.getTempFolderPath(),
                dto.getReturnCode()
            );

            return "処理が完了しました";
        } catch (Exception e) {
            return "エラー: " + e.getMessage();
        }
    }

}
