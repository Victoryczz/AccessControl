package seu.vczz.ac.service;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/31
 * 角色权限管理
 */
public interface ISysRoleAclService {

    /**
     * 更新角色权限
     * @param roleId
     * @param aclIdList
     */
    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);
}
