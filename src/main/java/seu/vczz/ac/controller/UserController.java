package seu.vczz.ac.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seu.vczz.ac.model.SysUser;
import seu.vczz.ac.service.ISysUserService;
import seu.vczz.ac.util.MD5Util;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CREATE by vczz on 2018/5/29
 * 前台登录页面用户controller，主要是用来进行用户登录
 */
@Controller
public class UserController {

    @Autowired
    private ISysUserService iSysUserService;

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //先从请求中拿到用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //先通过用户名找到用户
        SysUser user = iSysUserService.findByKeyword(username);
        //定义一个错误的话返回的msg
        String errorMsg = "";
        //拿到跳转过来的页面,可能为空
        String ret = request.getParameter("ret");
        //接下来进行参数的校验
        if (StringUtils.isBlank(username)){
            errorMsg = "用户名不能为空";
        }else if (StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if (user == null){
            errorMsg = "查询不到相关用户";
        }else if (!user.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else {
            //登录成功
            //将用户放在session
            request.getSession().setAttribute("user", user);
            if (StringUtils.isNotBlank(ret)){
                //如果之前的页面不是空，跳回去
                response.sendRedirect(ret);
            }else {
                response.sendRedirect("/admin/index.page");//否则调到管理员下的index todo
            }
        }
        //没有登录成功
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)){
            //没有登录成功，哪里来回哪去
            response.sendRedirect(ret);
        }
        //否则转到登录页面
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * 登出
     */
    @RequestMapping("logout.page")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //直接使session失效然后跳转到登录页面即可
        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }



}
