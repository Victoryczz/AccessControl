package seu.vczz.ac.service.impl;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.beans.PageResult;
import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.dao.SysUserMapper;
import seu.vczz.ac.exception.ParamException;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.param.UserParam;
import seu.vczz.ac.service.ISysUserService;
import seu.vczz.ac.util.BeanValidatorUtil;
import seu.vczz.ac.util.IpUtil;
import seu.vczz.ac.util.MD5Util;
import seu.vczz.ac.util.PasswordUtil;
import java.util.Date;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/29
 */
@Service("iSysUserService")
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 新增保存用户
     * @param userParam
     */
    public void save(UserParam userParam){
        //先进行数据校验
        BeanValidatorUtil.check(userParam);
        //检查有没有同email或者同phone的用户
        if (checkEmailExist(userParam.getMail(), userParam.getId())){
            throw new ParamException("邮箱已经被注册");
        }
        if (checkPhoneExist(userParam.getTelephone(), userParam.getId())){
            throw new ParamException("电话号已经被注册");
        }
        //采用后台生成密码的方式，先生成password，然后邮件发送给用户，使用该密码操作
        String password = PasswordUtil.randomPassword();
        // 暂时使用写死的方便测试
        password = "123456";
        String encryptedPassword = MD5Util.encrypt(password);
        //构造用户
        SysUser user = SysUser.builder().username(userParam.getUsername()).password(encryptedPassword).mail(userParam.getMail())
                .deptId(userParam.getDeptId()).telephone(userParam.getTelephone()).status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());
        //插入，在插入之前需要向email发送随机密码
        //todo: sendEmail

        sysUserMapper.insertSelective(user);
    }
    //传入id的原因是登陆的时候直接使用该方法进行校验
    //检查邮箱
    private boolean checkEmailExist(String email, Integer userId){
        return sysUserMapper.countByMail(email) > 0;
    }
    //检查手机号
    private boolean checkPhoneExist(String phone, Integer userId){
        return sysUserMapper.countByTelephone(phone) > 0;
    }

    /**
     * 更新用户
     * @param userParam
     */
    public void update(UserParam userParam){
        //先进行数据校验
        BeanValidatorUtil.check(userParam);
        //先取到更新之前的用户
        SysUser before = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before, "待更新用户不能为空");
        //检查有没有同email或者同phone的用户
        if (!userParam.getMail().equals(before.getMail())){
            if (checkEmailExist(userParam.getMail(), userParam.getId())){
                throw new ParamException("邮箱已经被注册");
            }
        }
        if (!userParam.getTelephone().equals(before.getTelephone())){
            if (checkPhoneExist(userParam.getTelephone(), userParam.getId())){
                throw new ParamException("电话号已经被注册");
            }
        }
        SysUser after = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).mail(userParam.getMail())
                .deptId(userParam.getDeptId()).telephone(userParam.getTelephone()).status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //更新
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 通过关键字查找用户(这就是可以通过用户名、邮箱、电话号登录的原因，数据库使用了or)
     * @param keyword
     * @return
     */
    public SysUser findByKeyword(String keyword){
        return sysUserMapper.findByKeyword(keyword);
    }

    /**
     * 通过部门id获取用户
     * @param deptId
     * @param pageQuery
     * @return
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery){
        //参数校验
        BeanValidatorUtil.check(pageQuery);
        //查询数据库
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0){
            //找到分页用户
            List<SysUser> userList = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            PageResult<SysUser> pageResult = PageResult.<SysUser>builder().total(count).data(userList).build();
            return pageResult;
        }
        return PageResult.<SysUser>builder().build();
    }

}
