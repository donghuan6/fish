package com.nine.common.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限（菜单）
 */
@Data
public class PermitVo implements Serializable {
    private static final long serialVersionUID = -2617950789064492923L;

    @Schema(description = "权限ID")
    private Long permitId;

    @Schema(description = "权限名")
    private String permitName;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "父权限主键")
    private Long parentId;

    @Schema(description = "M-目录菜单,C-子菜单,B-按键")
    private String menuType;

    @Schema(description = "0-显示,1-隐藏")
    private Integer menuShow;

    @Schema(description = "0-正常,1-停用")
    private Integer menuStatus;

    @Schema(description = "权限标识")
    private String permit;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "备注")
    private String remark;
}


