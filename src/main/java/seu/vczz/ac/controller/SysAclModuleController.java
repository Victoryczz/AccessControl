package seu.vczz.ac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.dto.AclModuleLevelDto;
import seu.vczz.ac.param.AclModuleParam;
import seu.vczz.ac.service.ISysAclModuleService;
import seu.vczz.ac.service.ISysTreeService;
import java.util.List;


/**
 * CREATE by vczz on 2018/5/30
 * 权限模块controller
 */
@RequestMapping(value = "/sys/aclModule")
@Controller
@Slf4j
public class SysAclModuleController {

    @Autowired
    private ISysAclModuleService iSysAclModuleService;
    @Autowired
    private ISysTreeService iSysTreeService;

    /**
     * 保存新增权限模块
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse saveAckModule(AclModuleParam param){
        iSysAclModuleService.save(param);
        return ServerResponse.createBySuccess();
    }
    /**
     * 权限模块页面
     * @return
     */
    @RequestMapping(value = "/acl.page")
    public ModelAndView defaultPage(){
        return new ModelAndView("acl");
    }
    /**
     * 更新权限模块信息
     * @param aclModuleParam
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse updateAclModule(AclModuleParam aclModuleParam){
        iSysAclModuleService.update(aclModuleParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 树形结构
     * @return
     */
    @RequestMapping(value = "tree.json")
    @ResponseBody
    public ServerResponse tree(){
        List<AclModuleLevelDto> dtoList = iSysTreeService.aclModuleTree();
        return ServerResponse.createBySuccess(dtoList);
    }
    /**
     * 删除权限模块，要先看存不存在权限点
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete.json")
    @ResponseBody
    public ServerResponse delete(@RequestParam("id") Integer id){
        //todo
        return ServerResponse.createBySuccess();
    }

}
