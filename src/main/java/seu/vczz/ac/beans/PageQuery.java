package seu.vczz.ac.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * CREATE by vczz on 2018/5/30
 * 注意，该beans package是除了model以外的用来做请求或响应参数的bean
 * 分页请求
 */
public class PageQuery {


    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;

    @Setter
    @Getter
    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo-1)*pageSize;
    }
}
