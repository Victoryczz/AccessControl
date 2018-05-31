package seu.vczz.ac.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.dto.AclModuleLevelDto;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.RoleParam;
import seu.vczz.ac.service.*;
import seu.vczz.ac.util.StringUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * CREATE by vczz on 2018/5/30
 * 角色
 */
@RequestMapping("/sys/role")
@Controller
@Slf4j
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysTreeService iSysTreeService;
    @Autowired
    private ISysRoleAclService iSysRoleAclService;
    @Autowired
    private ISysRoleUserService iSysRoleUserService;
    @Autowired
    private ISysUserService iSysUserService;


    /**
     * 保存新增角色
     * @param param
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse save(RoleParam param){
        iSysRoleService.save(param);
        return ServerResponse.createBySuccess();
    }
    /**
     * 更新角色信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse update(RoleParam param){
        iSysRoleService.update(param);
        return ServerResponse.createBySuccess();
    }
    /**
     * 获取全部角色信息
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public ServerResponse list() {
        return ServerResponse.createBySuccess(iSysRoleService.getAll());
    }

    /**
     * 角色管理页面
     * @return
     */
    @RequestMapping(value = "role.page")
    public ModelAndView page(){
        return new ModelAndView("role");
    }

    /**
     * 角色的权限树
     * @return
     */
    @RequestMapping(value = "/roleTree.json")
    @ResponseBody
    public ServerResponse roleTree(@RequestParam("roleId")int roleId){
        List<AclModuleLevelDto> list = iSysTreeService.roleAclTree(roleId);
        //log.info(JsonUtil.obj2StringPretty(list));
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 改变角色对应的权限点
     * @param roleId
     * @param aclIds
     * @return
     */
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public ServerResponse changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        iSysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return ServerResponse.createBySuccess();
    }

    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping(value = "/users.json")
    @ResponseBody
    public ServerResponse users(@RequestParam("roleId")int roleId){
        //选中的(已分配该角色的用户)
        List<SysUser> selectedUserList = iSysRoleUserService.getUserListByRoleId(roleId);
        //所有的
        List<SysUser> allUser = iSysUserService.getAllUser();
        List<SysUser> unselectedUserList = Lists.newArrayList();
        //将所有选中的id放到set
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for (SysUser sysUser : allUser){
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())){
                //拿到没有选中的user
                unselectedUserList.add(sysUser);
            }
        }
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 更改角色分配的用户,即将角色重新分配
     * @param roleId
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/changeUsers.json")
    @ResponseBody
    public ServerResponse changeUsers(@RequestParam("roleId")int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "")String userIds){
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        iSysRoleUserService.changeRoleUsers(roleId, userIdList);
        return ServerResponse.createBySuccess();
    }


}
