package seu.vczz.ac.service;

import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.model.*;
import seu.vczz.ac.param.SearchLogParam;

/**
 * CREATE by vczz on 2018/6/5
 */
public interface ISysLogService {

    /**
     * 还原
     * @param id
     */
    void recover(int id);
    /**
     * 查询日志
     * @param param
     * @param page
     * @return
     */
    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page);
    /**
     * 部门的更改
     * @param before
     * @param after
     */
    void saveDeptLog(SysDept before, SysDept after);
    /**
     * 用户的更改
     * @param before
     * @param after
     */
    void saveUserLog(SysUser before, SysUser after);
    /**
     * 权限模块的更改
     * @param before
     * @param after
     */
    void saveAclModuleLog(SysAclModule before, SysAclModule after);
    /**
     * 权限点的更改
     * @param before
     * @param after
     */
    void saveAclLog(SysAcl before, SysAcl after);
    /**
     * 角色模块的更改
     * @param before
     * @param after
     */
    void saveRoleLog(SysRole before, SysRole after);



}
