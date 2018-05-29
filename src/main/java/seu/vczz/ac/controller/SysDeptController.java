package seu.vczz.ac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.dto.DeptLevelDto;
import seu.vczz.ac.param.DeptParam;
import seu.vczz.ac.service.ISysDeptService;
import seu.vczz.ac.service.ISysTreeService;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 * 部门
 */
@Controller
@RequestMapping(value = "/sys/dept")
@Slf4j
public class SysDeptController {

    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysTreeService iSysTreeService;

    /**
     * 部门页面
     * @return
     */
    @RequestMapping(value = "/dept.page")
    public ModelAndView defaultPage(){
        return new ModelAndView("dept");
    }

    /**
     * 新增部门
     * @param deptParam
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse saveDept(DeptParam deptParam){
        iSysDeptService.save(deptParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 部门树
     * @return
     */
    @RequestMapping(value = "/tree.json")
    @ResponseBody
    public ServerResponse deptTree(){
        List<DeptLevelDto> deptDtoList = iSysTreeService.deptTree();
        return ServerResponse.createBySuccess(deptDtoList);
    }

    /**
     * 更新部门信息
     * @param deptParam
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse updateDept(DeptParam deptParam){
        iSysDeptService.update(deptParam);
        return ServerResponse.createBySuccess();
    }

    /**
     * 删除部门，先没有做，因为要先看有没有用户，用户模块还没有
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete.json")
    @ResponseBody
    public ServerResponse delete(@RequestParam("id") Integer id){
        //todo
        return ServerResponse.createBySuccess();
    }




}
