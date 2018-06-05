package seu.vczz.ac.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.UserParam;
import seu.vczz.ac.service.ISysCoreService;
import seu.vczz.ac.service.ISysTreeService;
import seu.vczz.ac.service.ISysUserService;
import seu.vczz.ac.util.JsonUtil;

import java.util.Map;

/**
 * CREATE by vczz on 2018/5/29
 * 后台系统用户controller
 */
@RequestMapping(value = "/sys/user")
@Controller
@Slf4j
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysTreeService iSysTreeService;
    @Autowired
    private ISysCoreService iSysCoreService;

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

    /**
     * 根据用户id获取用户权限树
     * @param userId
     * @return
     */
    @RequestMapping("acls.json")
    @ResponseBody
    public ServerResponse acls(@RequestParam("userId")int userId){
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", iSysTreeService.userAclTree(userId));
        //log.info(JsonUtil.obj2StringPretty(iSysTreeService.userAclTree(userId)));
        map.put("roles", iSysCoreService.getRoleListByUserId(userId));
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 没有权限访问
     * @return
     */
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }


}
