package seu.vczz.ac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * CREATE by vczz on 2018/5/29
 * 登录成功之后的controller
 */
@RequestMapping(value = "/admin")
@Controller
public class AdminController {

    /**
     * 登录成功页面
     * @return
     */
    @RequestMapping("/index.page")
    public ModelAndView index(){
        //登录成功，直接返回admin页面
        return new ModelAndView("admin");
    }

}
