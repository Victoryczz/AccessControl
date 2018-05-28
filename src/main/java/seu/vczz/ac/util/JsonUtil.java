package seu.vczz.ac.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import seu.vczz.ac.model.SysUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * CREATE by vczz on 2018/5/28
 * json工具类
 */
@Slf4j
public class JsonUtil {

    //关键的东西，调用objectMapper的方法进行序列化反序列化
    private static org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
    //时间戳格式
    private static final String STAND_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //这几个config较为关键，实践总结
        //以下为序列化
        //对象所有的字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        //取消默认的时间戳的形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式统一使用:yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STAND_FORMAT));
        //以下为非序列化
        //忽略在json中存在，但是在java对象中不存在对应属性的情况，放置错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 将obj序列化为json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        //先判断obj是否是null
        if (obj == null){
            return null;
        }

        try {
            return obj instanceof String? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse Object to String error", e);
            return null;
        }
    }

    /**
     * 返回格式化好的json字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        //先判断obj是否是null
        if (obj == null){
            return null;
        }

        try {
            return obj instanceof String? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse Object to String pretty error", e);
            return null;
        }
    }

    /**
     * 反序列化，将string反序列化为object
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz){
        //先判断字符串是否为空或class是否为空
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            //如果class是字符串的话，直接返回str，否则调用readValue()方法
            return clazz.equals(String.class)? (T) str :objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 反序列化方法，当clazz是集合且集合内为bean时就无能为力，所以引出该反序列化方法，实现集合的反序列化
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        //先判断字符串是否为空或class是否为空
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {

            return (T)(typeReference.getType().equals(String.class)?str:objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 同样是为了解决集合类的反序列化问题，注意参数中泛型必须使用:?
     * @param str
     * @param collectionClass
     * @param elementClasses
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses){
        //构造一个JavaType类型
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return (T)objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    public static void main(String[] args) {

        SysUser user1 = new SysUser();
        user1.setId(1);
        user1.setUsername("vczz");



        String user1Str = JsonUtil.obj2String(user1);
        String user1PrettyStr = JsonUtil.obj2StringPretty(user1);
        //打印
        log.info(user1Str);
        log.info(user1PrettyStr);
        //反序列化一下
        SysUser user = JsonUtil.string2Obj(user1Str, SysUser.class);
        System.out.println("end");
        //序列化
        List<SysUser> userList = Lists.newArrayList();
        userList.add(user1);
        String userListStr = JsonUtil.obj2StringPretty(userList);
        log.info(userListStr);

        List<SysUser> users = JsonUtil.string2Obj(userListStr, new TypeReference<List<SysUser>>(){
        });


    }


}

