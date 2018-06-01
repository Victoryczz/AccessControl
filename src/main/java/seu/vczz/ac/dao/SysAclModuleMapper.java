package seu.vczz.ac.dao;

import org.apache.ibatis.annotations.Param;
import seu.vczz.ac.model.SysAclModule;
import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parentId")Integer parentId, @Param("name")String name, @Param("id")Integer id);

    List<SysAclModule> getChildAclModuleListByLevel(@Param("level")String level);

    void batchUpdateLevel(@Param("sysAclModuleList")List<SysAclModule> sysAclModuleList);

    List<SysAclModule> selectAllAclModule();

    int countByParentId(@Param("aclModuleId")int aclModuleId);

}