package seu.vczz.ac.dao;

import org.apache.ibatis.annotations.Param;
import seu.vczz.ac.model.SysRoleAcl;

import javax.swing.*;
import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList")List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId")Integer roleId);

    void batchInsert(@Param("roleAclList")List<SysRoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId")Integer aclId);
}