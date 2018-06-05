package seu.vczz.ac.dao;

import org.apache.ibatis.annotations.Param;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.model.SysAcl;
import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("aclModuleId")Integer aclModuleId, @Param("name")String name, @Param("id")Integer id);

    int countByAclModuleId(@Param("aclModuleId")Integer aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId")Integer aclModuleId, @Param("page")PageQuery page);

    List<SysAcl> getAll();

    List<SysAcl> getAclListByIdList(@Param("idList")List<Integer> idList);

    List<SysAcl> getByUrl(@Param("url")String url);
}