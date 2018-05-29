package seu.vczz.ac.service;

import seu.vczz.ac.dto.DeptLevelDto;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 * 计算树层级结构
 */
public interface ISysTreeService {

    /**
     * 部门层级树
     * @return
     */
    List<DeptLevelDto> deptTree();
}
