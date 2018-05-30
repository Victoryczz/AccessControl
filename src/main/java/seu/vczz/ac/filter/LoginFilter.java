package seu.vczz.ac.filter;

import seu.vczz.ac.common.RequestHolder;
import seu.vczz.ac.model.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CREATE by vczz on 2018/5/30
 * 登录filter
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //先从session中取到用户
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if (user == null){
            String path = "signin.jsp";
            response.sendRedirect(path);
            return;
        }
        //否则保存一下
        RequestHolder.add(user);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
