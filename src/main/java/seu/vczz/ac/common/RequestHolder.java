package seu.vczz.ac.common;

import seu.vczz.ac.model.SysUser;
import javax.servlet.http.HttpServletRequest;

/**
 * CREATE by vczz on 2018/5/30
 * 使用threadlocal来保存信息
 */
public class RequestHolder {
    //用户
    private static final ThreadLocal<SysUser> USER_THREAD_LOCAL = new ThreadLocal<>();
    //请求
    private static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 添加用户信息
     * @param user
     */
    public static void add(SysUser user){
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 添加请求信息
     * @param request
     */
    public static void add(HttpServletRequest request){
        REQUEST_THREAD_LOCAL.set(request);
    }

    /**
     * 获得当前用户
     * @return
     */
    public static SysUser getCurrentUser(){
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获得请求信息
     * @return
     */
    public static HttpServletRequest getCurrentRequest(){
        return REQUEST_THREAD_LOCAL.get();
    }

    /**
     * 移除信息
     */
    public static void remove(){
        USER_THREAD_LOCAL.remove();;
        REQUEST_THREAD_LOCAL.remove();
    }
}
