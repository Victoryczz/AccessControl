package seu.vczz.ac.service.impl;

import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.ReturnInstruction;
import org.apache.commons.collections.CollectionUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.*;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.model.SysRole;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.service.ISysCoreService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CREATE by vczz on 2018/5/31
 */
@Service("iSysCoreService")
public class SysCoreServiceImpl implements ISysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取当前用户权限点列表
     * @return
     */
    public List<SysAcl> getCurrentUserAclList(){
        SysUser currentUser = RequestHolder.getCurrentUser();
        return getUserAclList(currentUser.getId());
    }

    /**
     * 根据用户id获取用户权限
     * @param userId
     * @return
     */
    //根据用户id获取用户权限  用户-->角色-->权限
    public List<SysAcl> getUserAclList(Integer userId){
        if (isSuperAdmin(userId)){
            //如果是超级管理员，返回所有的权限点
            return sysAclMapper.getAll();
        }
        //1.如果不是的话，取出用户已经分配的角色id
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        //若果用户没有分配角色
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        //2.否则根据角色id取出所有的权限点id
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)){
            return Lists.newArrayList();
        }
        //3.根据权限点id取到权限点
        return sysAclMapper.getAclListByIdList(userAclIdList);
    }
    //是否是超级管理员
    private boolean isSuperAdmin(Integer userId){
        //只有vczz是超级管理员
        return userId == 6;
    }

    /**
     * 获取角色的权限点
     * @param roleId
     * @return
     */
    public List<SysAcl> getRoleAclList(Integer roleId){
        //1. 根据角色id获取权限点id
        List<Integer> roleAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(roleAclIdList)){
            return Lists.newArrayList();
        }
        //2. 根据权限点id获取权限点
        return sysAclMapper.getAclListByIdList(roleAclIdList);
    }

    /**
     * 根据userId获取已分配角色列表
     * @param userId
     * @return
     */
    public List<SysRole> getRoleListByUserId(int userId){
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        List<SysRole> roleList = sysRoleMapper.getRoleListByIdList(roleIdList);
        return roleList;
    }

    /**
     * 根据权限点id获取已分配用户列表
     * @param aclId
     * @return
     */
    public List<SysUser> getUserListByAclId(Integer aclId){
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return sysUserMapper.getUserByIdList(userIdList);
    }

    /**
     * 根据aclId获取已分配角色
     * @param aclId
     * @return
     */
    public List<SysRole> getRoleListByAclId(Integer aclId){
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return Lists.newArrayList();
        }
        return sysRoleMapper.getRoleListByIdList(roleIdList);
    }

    /**
     * 判断是否具有某个url的访问权限
     * @return
     */
    public boolean hasUrlAcl(String url){
        //先判断是不是超级管理员vczz
        if (isSuperAdmin(RequestHolder.getCurrentUser().getId())){
            return true;
        }
        //取出url对应的权限点acl   一个url可能有多个权限点都可以访问
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)){
            //证明权限管理都不care这个url，所以都能访问
            return true;
        }
        //当前用户已有权限点
        List<SysAcl> userAclList = getUserAclList(RequestHolder.getCurrentUser().getId());
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        //变量，看是否存在一个有效的权限点
        boolean hasValidAcl = false;
        for (SysAcl acl : aclList){
            //遍历aclList
            if (acl == null || acl.getStatus() != 1){
                //如果权限点不存在或者无效，就返回true,不需要校验
                continue;
            }
            //只要一个有效就是true
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())){
                //只要用户有一个权限点，就有访问权限
                return true;
            }
        }
        if (!hasValidAcl){
            //如果不存在有效的权限点,就证明该url没啥用
            return true;
        }
        return false;

    }



}
