package com.nine.user.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "角色权限")
@Data
@TableName("sys_role_permit")
public class RolePermit {

    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private Long roleId;

    @NotNull(message = "权限ID不能为空")
    @Schema(description = "权限ID")
    private Long permitId;

}
