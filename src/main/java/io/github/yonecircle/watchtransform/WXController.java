package io.github.yonecircle.watchtransform;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.yonecircle.watchtransform.dto.SettingSaveRequest;
import io.github.yonecircle.watchtransform.dto.StatusResponse;
import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.entity.SettingInfo;
import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.exception.ValidationException;
import io.github.yonecircle.watchtransform.repository.SettingInfoRepository;

@Controller
public class WXController {

    private final ServiceProcess serviceProcess;
    private final ConfigService configService;
    private final StatusHolder statusHolder;
    private final WXExecuteRequestValidator wxExecuteRequestValidator;
    private final SettingInfoRepository repository;

    ////////////////////////////////////////////////////////////////////////////////////
    //Controllerが使うクラスを生成する
    //return:無し
    ////////////////////////////////////////////////////////////////////////////////////
    public WXController(ServiceProcess serviceProcess, ConfigService configService, StatusHolder statusHolder, 
                        WXExecuteRequestValidator wxExecuteRequestValidator, SettingInfoRepository repository) {
        this.serviceProcess = serviceProcess;
        this.configService = configService;
        this.statusHolder = statusHolder;
        this.wxExecuteRequestValidator = wxExecuteRequestValidator;
        this.repository = repository;                    
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //"index"にアクセスすると、index.htmlを返す
    //return:HTML
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("index")
    public String index() {
        return "index";
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //"executionStatus"にアクセスすると、executionStatus.htmlを返す
    //return:HTML
    //Notes:index.htmlのexecute()でwindow.location.hrefにより遷移する
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/executionStatus")
    public String executionStatus() {
        return "executionStatus";
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //WX処理（非同期）を開始して200 OKを返す
    //return:ResponseEntity
    //Notes:index画面で実行ボタンを押すと実行される
    //      画面遷移はindex.html側のJSがwindow.location.hrefで行う
    ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/api/wx/execute")
    @ResponseBody
    public ResponseEntity<Void> execute(@RequestBody WXExecuteRequest dto) {

        //nullまたは空チェック
        wxExecuteRequestValidator.validateNotNullOrEmpty(dto);

        //確認用
        System.out.println("=== execute() called ===");
        System.out.println("endfileFolderPath = " + dto.getEndfileFolderPath());
        System.out.println("resultFolderPath  = " + dto.getTxtFolderPath());
        System.out.println("tempFolderPath    = " + dto.getTempFolderPath());
        System.out.println("returnCode        = " + dto.getReturnCode());
        System.out.println("suffixMode        = " + dto.getSuffixMode());

        Path endFileDir    = Paths.get(dto.getEndfileFolderPath());
        Path resultFileDir = Paths.get(dto.getTxtFolderPath());
        Path tempFileDir   = Paths.get(dto.getTempFolderPath());

        //watchAndTransformを呼ぶだけで即returnする（@Asyncで別スレッド実行）
        serviceProcess.watchAndTransform(
            endFileDir,
            resultFileDir,
            tempFileDir,
            dto.getReturnCode(),
            dto.getSuffixMode());

        return ResponseEntity.ok().build();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //JavaScriptのポーリングに対して現在のステータスを返す
    //return:String（WXStatusのenum名）
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/api/wx/status")
    @ResponseBody
    public StatusResponse getStatus() {
        StatusSnapshot snapshot = statusHolder.getSnapshot();
        StatusResponse res = new StatusResponse();
        res.setStatus(snapshot.status());
        res.setMessage(snapshot.message());

        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //プロパティファイルの内容を読み込みDTOとして返す
    //return:WXExecuteRequest
    //Notes:index画面が表示されるとJavaScriptが発火し、このメソッドを叩く
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/api/wx/config")
    @ResponseBody
    public WXExecuteRequest loadConfig() throws SystemException {
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
    public void saveConfig(@RequestBody WXExecuteRequest dto) throws SystemException {
        wxExecuteRequestValidator.validateNotNull(dto);
        configService.saveProperties(dto);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //executionStatus.html表示中に監視停止ボタンが押されると非同期処理Threadを中断させる
    //return:void
    //Notes:「監視停止」ボタンが押されると発火
    ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/api/wx/stopwatching")
    @ResponseBody
    public void stopWatching() {
        Thread thread = statusHolder.getThread();
        if (thread == null) {
            throw new ValidationException("現在、ファイル監視はしていません");
        }
        thread.interrupt();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //settingsにGetでアクセスされたら、DB情報を出力する（仮）
    //return:
    ////////////////////////////////////////////////////////////////////////////////////
    @ResponseBody
    @GetMapping("/api/wx/settings")
    public String settings() {
        List<SettingInfo> settings = repository.findAll();
        for (SettingInfo setting : settings) {
            System.out.println(setting.getId() + setting.getName());
        }
        return "DB";
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //settingsにPostでアクセスされたら、設定情報をDBに保存する
    //return:無し
    //Notes:dtoを受け取ってDBに保存する
    ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/api/wx/settings")
    @ResponseBody
    public void saveSettingInfo(@RequestBody SettingSaveRequest dto) {
        SettingInfo settingInfo = new SettingInfo();

        settingInfo.setName(dto.getSettingName());
        settingInfo.setEndFilePath(dto.getEndfileFolderPath());
        settingInfo.setTextPath(dto.getTxtFolderPath());
        settingInfo.setTempPath(dto.getTempFolderPath());
        settingInfo.setReturnCode(dto.getReturnCode());
        settingInfo.setSuffixMode(dto.getSuffixMode());

        repository.save(settingInfo);

    }
}