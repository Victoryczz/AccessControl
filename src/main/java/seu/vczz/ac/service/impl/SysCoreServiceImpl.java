package seu.vczz.ac.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.*;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.model.SysRole;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.service.ISysCoreService;
import java.util.List;

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



}
