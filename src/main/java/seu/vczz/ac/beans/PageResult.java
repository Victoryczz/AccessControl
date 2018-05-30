package seu.vczz.ac.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 * 分页响应
 */
@Setter
@Getter
@Builder
@ToString
public class PageResult<T> {

    //一般用来存储返回的数据集
    private List<T> data = Lists.newArrayList();


    private int total = 0;

}
