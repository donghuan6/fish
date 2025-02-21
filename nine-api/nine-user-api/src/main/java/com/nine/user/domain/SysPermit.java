package com.nine.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(description = "用户权限")
@TableName("sys_permit")
public class SysPermit {

    @Schema(description = "权限ID")
    @TableId(value = "permit_id", type = IdType.AUTO)
    private Long permitId;

    @Length(max = 50, message = "权限名长度不能超过50")
    @NotBlank(message = "权限名不能为空")
    @Schema(description = "权限名")
    private String permitName;

    @Length(max = 100, message = "路径长度不能超过100")
    @NotBlank(message = "路径不能为空")
    @Schema(description = "路径")
    private String path;

    @Schema(description = "父权限主键")
    private Long parentId;

    @Length(max = 1, message = "M-目录菜单,C-子菜单,B-按键")
    @Schema(description = "M-目录菜单,C-子菜单,B-按键")
    private String menuType;

    @Range(min = 0, max = 1, message = "0-显示,1-隐藏")
    @Schema(description = "0-显示,1-隐藏")
    private Integer menuShow;

    @Range(min = 0, max = 1, message = "0-正常,1-停用")
    @Schema(description = "0-正常,1-停用")
    private Integer menuStatus;

    @Length(max = 50, message = "权限标识长度不能超过50")
    @Schema(description = "权限标识")
    private String permit;

    @Range(min = 0, max = 500, message = "排序，0-最低，100-最高")
    @Schema(description = "排序")
    private Integer permitSort;

    @Length(max = 100, message = "图标长度不能超过100")
    @Schema(description = "图标")
    private String icon;

    @Length(max = 100, message = "备注长度不能超过100")
    @Schema(description = "备注")
    private String remark;

}
