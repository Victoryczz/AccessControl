package seu.vczz.ac.dao;

import org.apache.ibatis.annotations.Param;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.model.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyword(@Param("keyword")String keyword);

    int countByTelephone(@Param("telephone")String telephone);

    int countByMail(@Param("mail")String mail);

    int countByDeptId(@Param("deptId")Integer deptId);

    List<SysUser> getPageByDeptId(@Param("deptId")Integer deptId, @Param("pageQuery")PageQuery pageQuery);

    List<SysUser> getUserByIdList(@Param("idList")List<Integer> idList);

    List<SysUser> getAllUser();
}