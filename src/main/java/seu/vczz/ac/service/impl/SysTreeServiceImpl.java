package seu.vczz.ac.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.dao.SysDeptMapper;
import seu.vczz.ac.dto.DeptLevelDto;
import seu.vczz.ac.model.SysDept;
import seu.vczz.ac.service.ISysTreeService;
import seu.vczz.ac.util.LevelUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 */
@Service("iSysTreeService")
public class SysTreeServiceImpl implements ISysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 部门层级树
     * @return
     */
    public List<DeptLevelDto> deptTree(){
        //先取出所有的部门
        List<SysDept> deptList = sysDeptMapper.selectAllDept();

        List<DeptLevelDto> dtoList = Lists.newArrayList();
        //遍历部门，适配dto
        for (SysDept dept : deptList){
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }
    //将dtoList中的dto设置树形
    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            //如果dtoList为空，就返回空
            return Lists.newArrayList();
        }
        //Multimap ----> <key, value> = <level, list<DeptLevelDto>>形式
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        //根级目录下的部门list
        List<DeptLevelDto> rootDeptList = Lists.newArrayList();
        //遍历所有部门，先找出根部门
        for (DeptLevelDto dto : dtoList){
            //注意，此时相同key的都放在list里了，以level为key，以list为value
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootDeptList.add(dto);
            }
        }
        //先将根级部门按照seq排序
        Collections.sort(rootDeptList, deptSeqComparator);
        //递归生成树
        transfromDeptTree(rootDeptList, LevelUtil.ROOT, levelDeptMap);
        return rootDeptList;
    }
    //将list转换为树(其实该函数就是做了排序功能，因为上面的函数已经将level和dtoList给对应了)，从rootList开始
    private void transfromDeptTree(List<DeptLevelDto> dtoList, String level, Multimap<String, DeptLevelDto> levelDeptMap){
        for (int i = 0; i < dtoList.size(); i++){
            //遍历该级下的每个部门，递归处理
            DeptLevelDto deptLevelDto = dtoList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(deptLevelDto.getLevel(), deptLevelDto.getId().toString());
            //处理下一层
            List<DeptLevelDto> tempDeptDtoList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptDtoList)){
                //如果不是空，就处理下一级
                //排序
                Collections.sort(tempDeptDtoList, deptSeqComparator);
                //设置dto
                deptLevelDto.setDeptList(tempDeptDtoList);
                //递归
                transfromDeptTree(tempDeptDtoList, nextLevel, levelDeptMap);
            }
        }
    }
    //seq比较器
    private Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };




}
