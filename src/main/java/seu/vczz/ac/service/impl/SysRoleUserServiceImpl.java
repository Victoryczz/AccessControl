package seu.vczz.ac.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seu.vczz.ac.beans.LogType;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysLogMapper;
import seu.vczz.ac.dao.SysRoleUserMapper;
import seu.vczz.ac.dao.SysUserMapper;
import seu.vczz.ac.model.SysLogWithBLOBs;
import seu.vczz.ac.model.SysRoleUser;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.service.ISysRoleUserService;
import seu.vczz.ac.util.IpUtil;
import seu.vczz.ac.util.JsonUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * CREATE by vczz on 2018/5/31
 */
@Service("iSysRoleUserService")
public class SysRoleUserServiceImpl implements ISysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 通过角色id获取所有已分配该角色的用户
     * @param roleId
     * @return
     */
    public List<SysUser> getUserListByRoleId(Integer roleId){
        //取到所有的userId
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        //然后取到所有的user
        return sysUserMapper.getUserByIdList(userIdList);
    }

    /**
     * 更改角色分配的用户
     * @param roleId
     */
    public void changeRoleUsers(Integer roleId, List<Integer> userIdList){
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()){
            //如果更改前和要插入的数量相同
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            //去掉交集
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)){
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);

    }

    @Transactional
    public void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }
    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonUtil.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonUtil.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
