package seu.vczz.ac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import seu.vczz.ac.common.ServerResponse;


/**
 * CREATE by vczz on 2018/5/27
 * 测试配置
 */
@Controller
@RequestMapping(value = "/test")
@Slf4j
public class TestController {

    @RequestMapping(value = "/hello.json")
    @ResponseBody
    public ServerResponse test(){
        log.info("--------------------hello----------------------");
        return ServerResponse.createBySuccessMessage("hello fuck you");
    }
}
