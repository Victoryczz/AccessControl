package seu.vczz.ac.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seu.vczz.ac.dao.SysAclMapper;
import seu.vczz.ac.dao.SysAclModuleMapper;
import seu.vczz.ac.dao.SysDeptMapper;
import seu.vczz.ac.dto.AclDto;
import seu.vczz.ac.dto.AclModuleLevelDto;
import seu.vczz.ac.dto.DeptLevelDto;
import seu.vczz.ac.model.SysAcl;
import seu.vczz.ac.model.SysAclModule;
import seu.vczz.ac.model.SysDept;
import seu.vczz.ac.service.ISysCoreService;
import seu.vczz.ac.service.ISysTreeService;
import seu.vczz.ac.util.LevelUtil;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CREATE by vczz on 2018/5/28
 */
@Service("iSysTreeService")
public class SysTreeServiceImpl implements ISysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private ISysCoreService iSysCoreService;
    @Autowired
    private SysAclMapper sysAclMapper;

    /**
     * 根据用户id获取用户已分配权限树
     * @param userId
     * @return
     */
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = iSysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

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
    //aclModule的seq比较
    private Comparator<AclModuleLevelDto> aclModuleComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    /**
     * 权限模块树
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree(){
        //取出所有的aclModule
        List<SysAclModule> aclModuleList = sysAclModuleMapper.selectAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();

        for (SysAclModule sysAclModule : aclModuleList){
            AclModuleLevelDto dto = AclModuleLevelDto.adapt(sysAclModule);
            dtoList.add(dto);
        }
        return aclModuleListToTree(dtoList);
    }
    //转换
    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            //如果参数为空
            return Lists.newArrayList();
        }
        Multimap<String, AclModuleLevelDto> levelAclModDtoMap = ArrayListMultimap.create();
        //创建根级的level-->dto
        List<AclModuleLevelDto> rootDtoList = Lists.newArrayList();
        for (AclModuleLevelDto dto : dtoList){
            levelAclModDtoMap.put(dto.getLevel(), dto);
            if (dto.getLevel().equals(LevelUtil.ROOT)){
                rootDtoList.add(dto);
            }
        }
        //先将跟排序
        Collections.sort(rootDtoList, aclModuleComparator);

        transfromAclModuleTree(rootDtoList, LevelUtil.ROOT, levelAclModDtoMap);
        return rootDtoList;
    }
    //递归处理
    private void transfromAclModuleTree(List<AclModuleLevelDto> dtoList, String level, Multimap<String, AclModuleLevelDto> levelDeptMap){
        for (int i = 0; i < dtoList.size(); i++){
            //遍历该级下的每个部门，递归处理
            AclModuleLevelDto aclModuleLevelDto = dtoList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(aclModuleLevelDto.getLevel(), aclModuleLevelDto.getId().toString());
            //处理下一层
            List<AclModuleLevelDto> tempDeptDtoList = (List<AclModuleLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptDtoList)){
                //如果不是空，就处理下一级
                //排序
                Collections.sort(tempDeptDtoList, aclModuleComparator);
                //设置dto
                aclModuleLevelDto.setAclModuleList(tempDeptDtoList);
                //递归
                transfromAclModuleTree(tempDeptDtoList, nextLevel, levelDeptMap);
            }
        }
    }

    /**
     * 角色权限树，根据角色id获取角色权限树
     * 思想是取出所有的权限模块树，然后拥有的权限就是选中状态
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleAclTree(Integer roleId){
        //1. 当前用户已分配权限点
        List<SysAcl> userAclList = iSysCoreService.getCurrentUserAclList();
        //2. 当前角色已分配权限点
        List<SysAcl> roleAclList = iSysCoreService.getRoleAclList(roleId);
        //3. 当前系统所有的权限点dto
        List<AclDto> aclDtoList = Lists.newArrayList();
        //将用户权限点id/角色权限点id放在集合
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        //取到所有权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList){
            //遍历，适配成acldto
            AclDto aclDto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())){
                //如果用户aclIdList有该acl的id,则将dto的属性she为true
                aclDto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())){
                aclDto.setChecked(true);
            }
            aclDtoList.add(aclDto);
        }
        return aclListToTree(aclDtoList);
    }
    //将aclDtoList放入aclModuleLevelDto
    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        //1.拿到aclModuleTree
        List<AclModuleLevelDto> aclModuleDtoList = aclModuleTree();
        //map  key:aclModuleId--->value:List<AclDto> aclDtoList
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList){
            if (aclDto.getStatus() == 1){
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclModuleWithAcls(aclModuleDtoList, moduleIdAclMap);
        return aclModuleDtoList;
    }
    //将aclModule与acl绑定
    private void bindAclModuleWithAcls(List<AclModuleLevelDto> aclModuleDtoList, Multimap<Integer, AclDto> moduleIdAclMap){
        if (CollectionUtils.isEmpty(aclModuleDtoList)){
            return;
        }
        for (AclModuleLevelDto aclModuleDto : aclModuleDtoList){
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                Collections.sort(aclDtoList, aclSeqComparator);
                aclModuleDto.setAclList(aclDtoList);
            }
            //递归
            bindAclModuleWithAcls(aclModuleDto.getAclModuleList(), moduleIdAclMap);
        }
    }
    //comparator
    private Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
