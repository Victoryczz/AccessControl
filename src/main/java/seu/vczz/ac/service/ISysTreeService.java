package seu.vczz.ac.service;

import seu.vczz.ac.dto.AclModuleLevelDto;
import seu.vczz.ac.dto.DeptLevelDto;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 * 计算树层级结构
 */
public interface ISysTreeService {

    /**
     * 部门层级树
     * @return
     */
    List<DeptLevelDto> deptTree();
    /**
     * 权限模块树
     * @return
     */
    List<AclModuleLevelDto> aclModuleTree();
    /**
     * 角色权限树，根据角色id获取角色权限树
     * 思想是取出所有的权限模块树，然后拥有的权限就是选中状态
     * @param roleId
     * @return
     */
    List<AclModuleLevelDto> roleAclTree(Integer roleId);
    /**
     * 根据用户id获取用户已分配权限树
     * @param userId
     * @return
     */
    List<AclModuleLevelDto> userAclTree(int userId);
}
