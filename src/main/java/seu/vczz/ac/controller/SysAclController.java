package seu.vczz.ac.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.param.AclParam;
import seu.vczz.ac.service.ISysAclService;
import seu.vczz.ac.service.ISysCoreService;
import seu.vczz.ac.service.ISysRoleService;
import java.util.Map;

/**
 * CREATE by vczz on 2018/5/30
 * 权限点管理
 */
@RequestMapping(value = "sys/acl")
@Controller
public class SysAclController {

    @Autowired
    private ISysAclService iSysAclService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysCoreService iSysCoreService;

    /**
     * 保存新增权限点
     * @param aclParam
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse save(AclParam aclParam){
        iSysAclService.save(aclParam);
        return ServerResponse.createBySuccess();
    }
    /**
     * 更新权限点
     * @param aclParam
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse update(AclParam aclParam){
        iSysAclService.update(aclParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 获取权限点分页信息
     * @param aclMoudleId
     * @param pageQuery
     * @return
     */
    @RequestMapping(value = "/page.json")
    @ResponseBody
    public ServerResponse page(@RequestParam("aclModuleId")int aclMoudleId, PageQuery pageQuery){
        PageResult<SysAcl> pageResult = iSysAclService.getPageByAclModuleId(aclMoudleId, pageQuery);
        return ServerResponse.createBySuccess(pageResult);
    }
    @RequestMapping("acls.json")
    @ResponseBody
    public ServerResponse acls(@RequestParam("aclId")int aclId){
        Map<String, Object> map = Maps.newHashMap();
        map.put("roles", iSysCoreService.getRoleListByAclId(aclId));
        map.put("users", iSysCoreService.getUserListByAclId(aclId));
        return null;
    }

}
