package seu.vczz.ac.service;

import seu.vczz.ac.model.SysAcl;

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
}
