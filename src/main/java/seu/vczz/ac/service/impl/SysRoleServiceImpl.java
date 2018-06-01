package seu.vczz.ac.service.impl;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysRoleMapper;
import seu.vczz.ac.exception.ParamException;
import seu.vczz.ac.model.SysRole;
import seu.vczz.ac.param.RoleParam;
import seu.vczz.ac.service.ISysRoleService;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.IpUtil;
import java.util.Date;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 */
@Service("iSysRoleService")
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 新增保存角色
     * @param param
     */
    public void save(RoleParam param){
        //参数校验
        BeanValidatorUtil.check(param);
        //是否已经存在
        if (checkExist(param.getId(), param.getName())){
            throw new ParamException("角色名称已经存在");
        }
        //构造role
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
    }
    //校验是否存在
    private boolean checkExist(Integer id, String name){
        return sysRoleMapper.countByRoleName(name, id) > 0;
    }

    /**
     * 更新角色信息
     * @param param
     */
    public void update(RoleParam param){
        //参数校验
        BeanValidatorUtil.check(param);
        //是否已经存在
        if (checkExist(param.getId(), param.getName())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        //构造role
        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus())
                .type(param.getType()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 获取所有角色
     * @return
     */
    public List<SysRole> getAll(){
        return sysRoleMapper.getAll();
    }


}
