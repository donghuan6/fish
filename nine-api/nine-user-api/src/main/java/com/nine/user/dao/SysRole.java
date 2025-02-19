package com.nine.user.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "角色")
@Data
@TableName("sys_role")
public class SysRole {

    @Schema(description = "角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @NotBlank(message = "角色名称不能为空")
    @Length(max = 30, message = "角色名称长度不能超过30")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "角色字符串不能为空")
    @Length(max = 30, message = "角色字符串长度不能超过30")
    @Schema(description = "角色字符串")
    private String roleKey;

    @Length(max = 100, message = "角色描述长度不能超过100")
    @Schema(description = "角色描述")
    private String remark;
}
