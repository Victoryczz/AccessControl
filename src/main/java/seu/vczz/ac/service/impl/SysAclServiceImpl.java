package seu.vczz.ac.service.impl;

import com.google.common.base.Preconditions;
import com.sun.tools.corba.se.idl.StringGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysAclMapper;
import seu.vczz.ac.exception.ParamException;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.param.AclParam;
import seu.vczz.ac.service.ISysAclService;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.IpUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 */
@Service("iSysAclService")
public class SysAclServiceImpl implements ISysAclService {

    @Autowired
    private SysAclMapper sysAclMapper;

    /**
     * 新增保存权限点
     * @param aclParam
     */
    public void save(AclParam aclParam){
        //参数校验
        BeanValidatorUtil.check(aclParam);
        if (checkExist(aclParam.getId(), aclParam.getAclModuleId(),aclParam.getName())){
            throw new ParamException("相同权限模块下存在相同名称的权限点");
        }
        //构造acl
        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId())
                .type(aclParam.getType()).seq(aclParam.getSeq()).remark(aclParam.getRemark())
                .url(aclParam.getUrl()).status(aclParam.getStatus()).build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateTime(new Date());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclMapper.insertSelective(sysAcl);
    }
    //检查是否有重名
    private boolean checkExist(Integer id ,Integer aclModuleId, String name){
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }
    //产生权限码
    private String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    /**
     * 更新权限点
     * @param aclParam
     */
    public void update(AclParam aclParam){
        //校验
        BeanValidatorUtil.check(aclParam);
        if (checkExist(aclParam.getId(), aclParam.getAclModuleId(), aclParam.getName())){
            throw new ParamException("相同权限模块下存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq())
                .remark(aclParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 获取权限点分页信息
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery){
        BeanValidatorUtil.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0){
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            PageResult<SysAcl> sysAclPageResult = PageResult.<SysAcl>builder().data(aclList).total(count).build();
            return sysAclPageResult;
        }
        return PageResult.<SysAcl>builder().build();
    }


}
