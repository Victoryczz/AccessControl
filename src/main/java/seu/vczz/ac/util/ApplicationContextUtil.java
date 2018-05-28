package seu.vczz.ac.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * CREATE by vczz on 2018/5/28
 */
@Component("applicationContextUtil")
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    //自动注入的
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    /**
     * 根据类型获得bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }
    /**
     * 根据名称和类型获得bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(String name, Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}
