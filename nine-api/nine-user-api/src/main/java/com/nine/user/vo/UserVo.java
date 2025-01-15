package com.nine.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户信息")
public class UserVo implements Serializable {
    private static final long serialVersionUID = -7883276796349327771L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别：0-保密，1-男，2-女")
    private Integer sex;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "最后登录ip")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private LocalDateTime loginDate;

    @Schema(description = "用户状态：0-正常，1-禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
