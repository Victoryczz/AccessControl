package seu.vczz.ac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.beans.PageQuery;
import seu.vczz.ac.common.ServerResponse;
import seu.vczz.ac.param.SearchLogParam;
import seu.vczz.ac.service.ISysLogService;
/**
 * CREATE by vczz on 2018/6/5
 * 权限更新日志
 */
@RequestMapping("/sys/log")
@Controller
public class SysLogController {

    @Autowired
    private ISysLogService iSysLogService;

    /**
     * 日志页
     * @return
     */
    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    /**
     * 还原
     * @param id
     * @return
     */
    @RequestMapping("/recover.json")
    @ResponseBody
    public ServerResponse recover(@RequestParam("id") int id) {
        iSysLogService.recover(id);
        return ServerResponse.createBySuccess();
    }

    /**
     * 查询日志
     * @param param
     * @param page
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public ServerResponse searchPage(SearchLogParam param, PageQuery page) {
        return ServerResponse.createBySuccess(iSysLogService.searchPageList(param, page));
    }


}
