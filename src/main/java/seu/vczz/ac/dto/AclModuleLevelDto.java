package seu.vczz.ac.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import seu.vczz.ac.model.SysAclModule;
import java.util.List;

/**
 * CREATE by vczz on 2018/5/30
 * 权限模块层级树
 */
@Setter
@Getter
@ToString
public class AclModuleLevelDto extends SysAclModule{
    //权限模块列表
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();
    //权限点列表
    private List<AclDto> aclList = Lists.newArrayList();

    /**
     * 装配
     * @param aclModule
     * @return
     */
    public static AclModuleLevelDto adapt(SysAclModule aclModule){
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }


}
