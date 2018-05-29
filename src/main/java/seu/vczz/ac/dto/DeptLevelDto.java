package seu.vczz.ac.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import seu.vczz.ac.model.SysDept;

import java.util.List;

/**
 * CREATE by vczz on 2018/5/28
 * 部门树层级dto
 */
@Setter
@Getter
@ToString
public class DeptLevelDto extends SysDept{

    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 对象快速适配
     * @param dept
     * @return
     */
    public static DeptLevelDto adapt(SysDept dept){
        DeptLevelDto dto = new DeptLevelDto();
        //相同属性的拷贝，主要是快速构建对象
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }

}
