package seu.vczz.ac.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * CREATE by vczz on 2018/5/28
 * 测试参数校验类
 */
@Getter
@Setter
public class TestVo {

    @NotBlank(message = "msg不能为空")
    private String msg;
    @NotNull(message = "id不能为空")
    private Integer id;


}
