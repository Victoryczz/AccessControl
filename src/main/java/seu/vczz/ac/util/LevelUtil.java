package seu.vczz.ac.util;

import org.apache.commons.lang3.StringUtils;

/**
 * CREATE by vczz on 2018/5/28
 * 计算部门层级，其实在数据库中属于冗余字段，主要是加速部门的递归查询，直接通过层级可以直接查询部门信息
 */
public class LevelUtil {
    //根级目录是0
    public static final String ROOT = "0";
    //层级之间分隔符
    private static final String SEPARATOR = ".";

    /**
     * 计算部门层级  规则如下：
     * 0
     * 0.1
     * 0.1.2
     * 0.1.3
     * 0.2
     * @param parentId
     * @param parentLevel
     * @return
     */
    public static String calculateLevel(String parentLevel, String parentId){
        //如果没有parentId的话，就默认是根级目录
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }
        //拼接返回
        return parentLevel+SEPARATOR+parentId;
    }

}
