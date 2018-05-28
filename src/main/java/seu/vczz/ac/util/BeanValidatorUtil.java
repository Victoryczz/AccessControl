package seu.vczz.ac.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import seu.vczz.ac.exception.ParamException;

import javax.validation.*;
import java.util.*;

/**
 * CREATE by vczz on 2018/5/27
 * 校验工具类
 */
public class BeanValidatorUtil {

    //首先需要一个验证工厂
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    /**
     * 验证对象
     * @param t 对象
     * @param groups 类别,一个对象可能用于多个校验组，因为每个组队这个对象的要求不一样，所以可以使用该属性
     * @param <T> 对象类型泛型
     * @return
     */
    private static <T> Map<String, String> validate(T t, Class... groups){
        //拿到一个validator
        Validator validator = validatorFactory.getValidator();
        //获取验证结果
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()){
            //如果结果集是空,返回空集
            return Collections.emptyMap();
        }else {
            //定义一个放错误的map
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateResult.iterator();
            //将map放进去
            while (iterator.hasNext()){
                //violation 违例
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }
    /**
     * 校验集合
     * @param collection
     * @return
     */
    private static Map<String, String> validateList(Collection<?> collection){
        //先判断集合是不是空
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());

        return errors;
    }
    //这是对前面两个方法的封装
    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }
    //检查参数
    public static void check(Object param) throws ParamException {
        Map<String, String> map = BeanValidatorUtil.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }


}
