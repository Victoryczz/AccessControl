package seu.vczz.ac.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysAclModuleMapper;
import seu.vczz.ac.exception.ParamException;
import seu.vczz.ac.model.SysAclModule;
import seu.vczz.ac.param.AclModuleParam;
import seu.vczz.ac.service.ISysAclModuleService;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.IpUtil;
import seu.vczz.ac.util.LevelUtil;
import java.util.Date;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 */
@Service("iSysAclModuleService")
public class SysAclModuleServiceImpl implements ISysAclModuleService {

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    /**
     * 新增保存权限模块信息
     * @param aclModuleParam
     */
    public void save(AclModuleParam aclModuleParam){
        //参数校验
        BeanValidatorUtil.check(aclModuleParam);
        //检验有没有相同名称的
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同的权限模块");
        }
        //构造新的权限模块
        SysAclModule aclModule = SysAclModule.builder().name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark())
                .build();
        aclModule.setLevel(LevelUtil.calculateLevel(getParentLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId().toString()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        //插入
        sysAclModuleMapper.insertSelective(aclModule);

    }
    //校验同部门下有没有相同名称或id的部门
    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId){
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }
    //后的上级部门的层级
    private String getParentLevel(Integer aclModuleId){
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule != null){
            return aclModule.getLevel();
        }
        return null;
    }
    /**
     * 更新模块信息
     * @param aclModuleParam
     */
    public void update(AclModuleParam aclModuleParam){
        //参数校验
        BeanValidatorUtil.check(aclModuleParam);
        //检验有没有相同名称的
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同的权限模块");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        Preconditions.checkNotNull(before, "待更新的模块不存在");

        SysAclModule after = SysAclModule.builder().name(aclModuleParam.getName()).id(aclModuleParam.getId())
                .seq(aclModuleParam.getSeq()).remark(aclModuleParam.getRemark()).status(aclModuleParam.getStatus())
                .parentId(aclModuleParam.getParentId()).build();
        after.setLevel(LevelUtil.calculateLevel(getParentLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId().toString()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //如果模块下存在子模块，需要批量更新
        updateWithChild(before, after);

    }
    //更新子模块
    @Transactional
    void updateWithChild(SysAclModule before, SysAclModule after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }
}
