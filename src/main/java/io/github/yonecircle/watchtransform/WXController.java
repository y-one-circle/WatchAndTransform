package io.github.yonecircle.watchtransform;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.yonecircle.watchtransform.dto.StatusResponse;
import io.github.yonecircle.watchtransform.dto.WXExecuteRequest;
import io.github.yonecircle.watchtransform.exception.SystemException;
import io.github.yonecircle.watchtransform.exception.ValidationException;

@Controller
public class WXController {

    private final ServiceProcess serviceProcess;
    private final ConfigService configService;
    private final StatusHolder statusHolder;

    ////////////////////////////////////////////////////////////////////////////////////
    //Controllerが使うクラスを生成する
    //return:無し
    ////////////////////////////////////////////////////////////////////////////////////
    public WXController(ServiceProcess serviceProcess, ConfigService configService, StatusHolder statusHolder) {
        this.serviceProcess = serviceProcess;
        this.configService = configService;
        this.statusHolder = statusHolder;
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

        if (dto.getEndfileFolderPath() == null || dto.getEndfileFolderPath().isEmpty()) {
            throw new ValidationException("監視フォルダのパスは入力されていません");
        }
        if (dto.getTxtFolderPath() == null || dto.getTxtFolderPath().isEmpty()) {
            throw new ValidationException("テキストフォルダのパスは入力されていません");
        }
        if (dto.getTempFolderPath() == null || dto.getTempFolderPath().isEmpty()) {
            throw new ValidationException("一時フォルダのパスは入力されていません");
        }
        if (dto.getReturnCode() == null || dto.getReturnCode().isEmpty()) {
            throw new ValidationException("リターンコードが入力されていません");
        }

        //確認用
        System.out.println("=== execute() called ===");
        System.out.println("endfileFolderPath = " + dto.getEndfileFolderPath());
        System.out.println("resultFolderPath  = " + dto.getTxtFolderPath());
        System.out.println("tempFolderPath    = " + dto.getTempFolderPath());
        System.out.println("returnCode        = " + dto.getReturnCode());
        System.out.println("suffixMode        = " + dto.getSuffixMode());

            //ユーザ入力String→ロジック用正規化Path
            //PathNormalizer normalizer = new PathNormalizer();
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
        StatusResponse res = new StatusResponse();
        res.setStatus(statusHolder.getStatus());
        res.setMessage(statusHolder.getErrorMessage());

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
        configService.saveProperties(dto);
    }
}