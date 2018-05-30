package seu.vczz.ac.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CREATE by vczz on 2018/5/30
 * 权限点参数
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AclParam {

    private Integer id;

    @NotBlank(message = "权限点不可以为空")
    @Length(min = 2, max = 20, message = "权限点长度需要在2-20个字中间")
    private String name;

    @NotNull(message = "必须制定权限模块")
    private Integer aclModuleId;

    @Length(min = 6, max = 100, message = "权限点url长度需要在6-100字符之间")
    private String url;

    @NotNull(message = "必须制定权限点的类型")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须制定权限点的状态")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 1, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须制定权限点的展示顺序")
    private Integer seq;

    @Length(max = 200, message = "权限点备注长度需要在200个字符以内")
    private String remark;

}
