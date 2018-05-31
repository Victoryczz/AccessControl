package seu.vczz.ac.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import seu.vczz.ac.model.SysAcl;


/**
 * CREATE by vczz on 2018/5/31
 * 权限点的dto
 * 当要绑定角色与权限的关系时，需要列出该角色的权限树《该树包含了权限模块和权限点，所以需要改dto
 */
@Setter
@Getter
@ToString
public class AclDto extends SysAcl{

    //默认是否选中
    private boolean checked = false;
    //是否可以操作权限
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl){
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(acl, aclDto);
        return aclDto;
    }

}
