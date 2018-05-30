package seu.vczz.ac.service;

import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.UserParam;

/**
 * CREATE by vczz on 2018/5/29
 * 用户service
 */

public interface ISysUserService {


    /**
     * 新增保存用户
     * @param userParam
     */
    void save(UserParam userParam);
    /**
     * 更新用户
     * @param userParam
     */
    void update(UserParam userParam);
    /**
     * 通过关键字查找用户
     * @param keyword
     * @return
     */
    SysUser findByKeyword(String keyword);
    /**
     * 通过部门id获取用户
     * @param deptId
     * @param pageQuery
     * @return
     */
    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

}
