package seu.vczz.ac.service;

import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.model.SysRole;
import seu.vczz.ac.model.SysUser;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/31
 * 核心service，获取角色用户权限树
 */
public interface ISysCoreService {
    /**
     * 获取当前用户权限点列表
     * @return
     */
    List<SysAcl> getCurrentUserAclList();
    /**
     * 获取角色的权限点
     * @param roleId
     * @return
     */
    List<SysAcl> getRoleAclList(Integer roleId);
    /**
     * 根据用户id获取用户权限
     * @param userId
     * @return
     */
    //根据用户id获取用户权限  用户-->角色-->权限
    List<SysAcl> getUserAclList(Integer userId);
    /**
     * 根据userId获取已分配角色列表
     * @param userId
     * @return
     */
    List<SysRole> getRoleListByUserId(int userId);
    /**
     * 根据权限点id获取已分配用户列表
     * @param aclId
     * @return
     */
    List<SysUser> getUserListByAclId(Integer aclId);
    /**
     * 根据aclId获取已分配角色
     * @param aclId
     * @return
     */
    List<SysRole> getRoleListByAclId(Integer aclId);
}
