package seu.vczz.ac.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import seu.vczz.ac.util.JsonUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * CREATE by vczz on 2018/5/28
 * http请求/响应拦截器
 * 注意：amall中直接实现HandlerInterceptor,现在继承的类其实也实现了HandlerInterceptor
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter{

    private static final String START_TIME = "requestStartTime";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start. url:{}, params:{}", url, JsonUtil.obj2String(parameterMap));
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURI().toString();
//        long start = (Long) request.getAttribute(START_TIME);
//        long end = System.currentTimeMillis();
//        log.info("request finished. url:{}, cost:{}", url, end - start);
//        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        long start = (Long) request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request completed. url:{}, cost:{}ms", url, end - start);

//      removeThreadLocalInfo();
    }

    /*public void removeThreadLocalInfo() {
        RequestHolder.remove();;
    }*/
}
