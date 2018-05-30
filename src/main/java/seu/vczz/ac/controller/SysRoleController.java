package seu.vczz.ac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.param.RoleParam;
import seu.vczz.ac.service.ISysRoleService;

import javax.xml.bind.ValidationEvent;


/**
 * CREATE by vczz on 2018/5/30
 * 角色
 */
@RequestMapping("/sys/role")
@Controller
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * 保存新增角色
     * @param param
     * @return
     */
    @RequestMapping(value = "/save.json")
    @ResponseBody
    public ServerResponse save(RoleParam param){
        iSysRoleService.save(param);
        return ServerResponse.createBySuccess();
    }
    /**
     * 更新角色信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/update.json")
    @ResponseBody
    public ServerResponse update(RoleParam param){
        iSysRoleService.update(param);
        return ServerResponse.createBySuccess();
    }
    /**
     * 获取全部角色信息
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public ServerResponse list() {
        return ServerResponse.createBySuccess(iSysRoleService.getAll());
    }

    /**
     * 角色管理页面
     * @return
     */
    @RequestMapping(value = "role.page")
    public ModelAndView page(){
        return new ModelAndView("role");
    }
}
