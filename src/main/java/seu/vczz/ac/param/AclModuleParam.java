package seu.vczz.ac.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE by vczz on 2018/5/30
 * 权限模块参数
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AclModuleParam {

    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2, max = 20, message = "权限模块名称长度在2-20字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不可以为空")
    @Min(value = 0, message = "权限模块状态不合法")
    @Max(value = 1, message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 150, message = "备注长度在150字符以内")
    private String remark;

}
