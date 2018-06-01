package seu.vczz.ac.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysDeptMapper;
import seu.vczz.ac.dao.SysUserMapper;
import seu.vczz.ac.exception.ParamException;
import seu.vczz.ac.model.SysDept;
import seu.vczz.ac.param.DeptParam;
import seu.vczz.ac.service.ISysDeptService;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.IpUtil;
import seu.vczz.ac.util.LevelUtil;

import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 */
@Service("iSysDeptService")
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 新增保存部门
     * @param deptParam
     */
    public void save(DeptParam deptParam){
        //参数校验
        BeanValidatorUtil.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())){
            //如果存在相同的部门
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        //构造部门对象
        SysDept dept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        //父部门的level+父部门id就是新的level
        dept.setLevel(LevelUtil.calculateLevel(getParentLevel(deptParam.getParentId()), deptParam.getParentId().toString()));
        //从threadlocal中取到用户，因为在访问该controller的方法之前，已经放了user，所以能取到
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }
    //校验同部门下有没有相同名称或id的部门
    private boolean checkExist(Integer parentId, String deptName, Integer deptId){
        return sysDeptMapper.countByNameAndParentId(deptName, parentId, deptId) > 0;
    }
    //获取上层部门的层级
    private String getParentLevel(Integer parentDeptId){
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(parentDeptId);
        if (sysDept == null){
            return null;
        }
        return sysDept.getLevel();
    }

    /**
     * 更新部门信息
     * @param deptParam
     */
    public void update(DeptParam deptParam){
        //参数校验
        BeanValidatorUtil.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())){
            //如果存在相同的部门
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        //先看更新前部门存在不存在
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        SysDept after = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getParentLevel(deptParam.getParentId()), deptParam.getParentId().toString()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //使用事务更新，如果更新了当前部门，则子部门需要同时更新
        updateWithChild(before, after);

    }
    //因为当更新了次部门，子部门需要同时更新
    @Transactional
    void updateWithChild(SysDept before, SysDept after){
        //新的层级
        String newLevelPrefix = after.getLevel();
        //老的层级
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)){
            //如果新老层级不一样，也就是换了父层级，那么所有该部门的子层级也需要换掉
            //先将所有的子部门查出来 使用了like level.% 这样的正则
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(deptList)){
                //如果存在子部门
                for (SysDept dept : deptList){
                    //更新每个子部门的层级
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        //新的level
                        level = newLevelPrefix+level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                //批量更新
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        //最后更新自己
        sysDeptMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 删除部门
     * @param deptId
     */
    public void delete(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在");

        //1.查询有没有子部门
        if (sysDeptMapper.countByParentId(deptId) > 0){
            throw new ParamException("当前删除部门下存在子部门");
        }
        //2.查询有没有用户
        if (sysUserMapper.countByDeptId(deptId) > 0){
            throw new ParamException("当前删除部门下存在用户");
        }
        //即不存在用户，也不存在部门就删除
        sysDeptMapper.deleteByPrimaryKey(deptId);

    }


}
