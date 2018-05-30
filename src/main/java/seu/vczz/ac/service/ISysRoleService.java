package seu.vczz.ac.service;

import seu.vczz.ac.model.SysRole;
import seu.vczz.ac.param.RoleParam;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 * 角色服务
 */
public interface ISysRoleService {

    /**
     * 新增保存角色
     * @param param
     */
    void save(RoleParam param);
    /**
     * 更新角色信息
     * @param param
     */
    void update(RoleParam param);
    /**
     * 获取所有角色
     * @return
     */
    List<SysRole> getAll();

}
