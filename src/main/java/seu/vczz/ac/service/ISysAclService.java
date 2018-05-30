package seu.vczz.ac.service;

import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.param.AclParam;

/**
 * CREATE by vczz on 2018/5/30
 * 权限点
 */
public interface ISysAclService {

    /**
     * 新增保存权限点
     * @param aclParam
     */
    void save(AclParam aclParam);
    /**
     * 更新权限点
     * @param aclParam
     */
    void update(AclParam aclParam);
    /**
     * 获取权限点分页信息
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery);

}
