package seu.vczz.ac.dao;

import org.apache.ibatis.annotations.Param;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.dto.SearchLogDto;
import seu.vczz.ac.model.SysLog;
import seu.vczz.ac.model.SysLogWithBLOBs;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int countBySearchDto(@Param("dto")SearchLogDto searchLogDto);

    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto")SearchLogDto dto, @Param("page")PageQuery page);
}