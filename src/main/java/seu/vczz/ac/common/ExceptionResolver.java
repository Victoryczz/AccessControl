package seu.vczz.ac.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import seu.vczz.ac.exception.PermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CREATE by vczz on 2018/5/27
 * 全局异常处理
 */

@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver{

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //获取请求url
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView modelAndView;
        //默认错误信息为system error
        String defaultMsg = "System error";
        //根据json还是page定义异常类型
        //这里的逻辑是要求我们所有的请求，如果是数据则以.json结尾，如果是页面，则以.page结尾
        if (url.endsWith(".json")){
            //如果请求的是数据
            if (e instanceof PermissionException){
                //如果是我们自己定义的异常
                ServerResponse serverResponse = ServerResponse.createByErrorMessage(e.getMessage());
                //注意 jsonView是我们的springMVC-servlet中定义的view
                modelAndView = new ModelAndView("jsonView", serverResponse.toMap());
            }else {
                log.error("unknown json exception, url:{}, exception:{}", url, e);
                //否则是默认的异常信息
                ServerResponse serverResponse = ServerResponse.createByErrorMessage(defaultMsg);
                modelAndView = new ModelAndView("jsonView", serverResponse.toMap());
            }
        }else if (url.endsWith(".page")){
            //如果请求的是页面
            ServerResponse serverResponse = ServerResponse.createByErrorMessage(defaultMsg);
            //注意请求的是页面，所以我们需要定义一个异常页面来处理
            modelAndView = new ModelAndView("exception", serverResponse.toMap());
        }else {
            log.error("unknown page exception, url:{}, exception:{}", url, e);
            //否则所有的都走jsonView处理
            ServerResponse serverResponse = ServerResponse.createByErrorMessage(defaultMsg);
            modelAndView = new ModelAndView("jsonView", serverResponse.toMap());
        }
        return modelAndView;
    }
}
