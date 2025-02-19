package com.nine.common.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 */
@Data
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 2629352123851432826L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色字符串")
    private String roleKey;

    @Schema(description = "角色描述")
    private String remark;

}
