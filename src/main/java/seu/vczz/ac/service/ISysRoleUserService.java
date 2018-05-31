package seu.vczz.ac.service;

import seu.vczz.ac.model.SysUser;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/31
 * 角色-用户
 */
public interface ISysRoleUserService {
    /**
     * 通过角色id获取所有已分配该角色的用户
     * @param roleId
     * @return
     */
    List<SysUser> getUserListByRoleId(Integer roleId);
    /**
     * 更改角色分配的用户
     * @param roleId
     */
    void changeRoleUsers(Integer roleId, List<Integer> userIdList);

}
