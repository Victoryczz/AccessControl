package seu.vczz.ac.service;

import seu.vczz.ac.param.AclModuleParam;

/**
 * CREATE by vczz on 2018/5/30
 * 权限模块service
 */
public interface ISysAclModuleService {

    /**
     * 新增保存权限模块信息
     * @param aclModuleParam
     */
    void save(AclModuleParam aclModuleParam);
    /**
     * 更新模块信息
     * @param aclModuleParam
     */
    void update(AclModuleParam aclModuleParam);



}
