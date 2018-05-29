package seu.vczz.ac.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * CREATE by vczz on 2018/5/28
 * 部门参数，该package下的包基本都是用来校验参数用的
 */
@Setter
@Getter
@ToString
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(min = 2, max = 15, message = "部门名称的长度在2-15字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "顺序不能为空")
    private Integer seq;

    @Length(max = 150, message = "备注长度需要限制在150字符以内")
    private String remark;

}
