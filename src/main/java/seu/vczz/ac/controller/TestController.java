package seu.vczz.ac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.dao.SysUserMapper;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.TestVo;
import seu.vczz.ac.util.ApplicationContextUtil;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.JsonUtil;
import java.util.Map;


/**
 * CREATE by vczz on 2018/5/27
 * 测试配置
 */
@Controller
@RequestMapping(value = "/test")
@Slf4j
public class TestController {
    //测试接口
    @RequestMapping(value = "/hello.json")
    @ResponseBody
    public ServerResponse test(){
        log.info("--------------------hello----------------------");
        return ServerResponse.createBySuccessMessage("hello fuck you");
    }

    //测试数据校验
    @RequestMapping(value = "/validation.json")
    @ResponseBody
    public ServerResponse validate(TestVo testVo){
        log.info("--------------------validation----------------------");
        try {
            Map<String, String> errors = BeanValidatorUtil.validateObject(testVo);
            if (!errors.isEmpty()){
                for (Map.Entry entry : errors.entrySet()){
                    log.info("{}---->>>{}", entry.getKey(), entry.getValue());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ServerResponse.createBySuccessMessage("hello fuck you");
    }

    //测试jsonUtil以及applicationUtil
    @RequestMapping(value = "/testApp")
    @ResponseBody
    public ServerResponse testApplicationUtil(){
        log.info("----------application context util-------------");
        SysUserMapper sysUserMapper = ApplicationContextUtil.popBean(SysUserMapper.class);
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(1);
        log.info(JsonUtil.obj2String(sysUser));
        return ServerResponse.createBySuccessMessage("fuck u");
    }


}
