package seu.vczz.ac.service;

import seu.vczz.ac.param.DeptParam;

/**
 * CREATE by vczz on 2018/5/28
 */
public interface ISysDeptService {

    /**
     * 新增保存部门
     * @param deptParam
     */
    void save(DeptParam deptParam);
    /**
     * 更新部门信息
     * @param deptParam
     */
    void update(DeptParam deptParam);
    /**
     * 删除部门
     * @param deptId
     */
    void delete(Integer deptId);

}
