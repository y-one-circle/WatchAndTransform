package com.example.demo;

import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WXController {

    private final ServiceProcess serviceprocess;
    private final Watcher watcher;
    private final ConfigService configService;

    ////////////////////////////////////////////////////////////////////////////////////
    //Controllerが使うクラスを生成する
    //return:無し
    ////////////////////////////////////////////////////////////////////////////////////
    public WXController(ServiceProcess serviceprocess, Watcher watcher, ConfigService configService) {
        this.serviceprocess = serviceprocess;
        this.watcher = watcher;
        this.configService = configService;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //"ffindex"にアクセスすると、index.htmlを返す
    //return:HTML
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("index")
    public String index() {
        return "index";
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //プロパティファイルの内容を読み込みDTOとして返す
    //return:
    //Notes:index画面が表示されるとJavaScriptが発火し、このメソッドを叩く
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/api/wx/config")
    @ResponseBody
    public WXExecuteRequest loadConfig() {
        Properties props = configService.loadProperties();

        WXExecuteRequest dto = new WXExecuteRequest();
        dto.setEndfileFolderPath(props.getProperty("endfileFolderPath", ""));
        dto.setTxtFolderPath(props.getProperty("txtFolderPath", ""));
        dto.setTempFolderPath(props.getProperty("tempFolderPath", ""));
        dto.setReturnCode(props.getProperty("returnCode", ""));

        return dto;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //configにPostでアクセスされたら、Path情報をPropertyファイルに保存する
    //return:無し
    //Notes:dtoを受け取ってPropertyとして保存する
    ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/api/wx/config")
    @ResponseBody
    public void saveConfig(@RequestBody WXExecuteRequest dto) {
        configService.saveProperties(dto);
    }
}
