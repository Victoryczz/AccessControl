package seu.vczz.ac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.UserParam;
import seu.vczz.ac.service.ISysUserService;

/**
 * CREATE by vczz on 2018/5/29
 * 后台系统用户controller
 */
@RequestMapping(value = "/sys/user")
@Controller
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 新增保存用户
     * @param userParam
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse saveDept(UserParam userParam){
        iSysUserService.save(userParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 更新用户信息
     * @param userParam
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse updateDept(UserParam userParam){
        iSysUserService.update(userParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户分页查询展示
     * @param deptId
     * @param pageQuery
     * @return
     */
    @RequestMapping(value = "/page.json")
    @ResponseBody
    public ServerResponse page(@RequestParam("deptId")int deptId, PageQuery pageQuery){
        PageResult<SysUser> pageResult = iSysUserService.getPageByDeptId(deptId, pageQuery);
        return ServerResponse.createBySuccess(pageResult);
    }


}
