package com.nine.user.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nine.dao.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户")
@TableName("sys_user")
public class User extends BaseEntity {

    @Schema(description = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Schema(description = "用户名")
    @Length(max = 30, message = "用户名长度不能超过30")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "手机号")
    @Length(max = 20, message = "手机号长度不能超过20")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "昵称")
    @Length(max = 50, message = "昵称长度不能超过50")
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    @Schema(description = "密码")
    @Length(min = 6, max = 20, message = "用户密码长度在6-20位之间")
    @NotBlank(message = "用户密码不能为空")
    private String password;

    @Schema(description = "邮箱")
    @Length(max = 50, message = "邮箱长度不能超过50")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "性别：0-保密，1-男，2-女")
    @Range(max = 2, message = "性别范围在0-2之间")
    @NotNull(message = "性别不能为空")
    private Integer sex;

    @Schema(description = "头像")
    @Length(max = 100, message = "头像长度不能超过100")
    private String avatar;

    @Schema(description = "最后登录ip")
    @Length(max = 50, message = "最后登录ip长度不能超过50")
    @NotBlank(message = "最后登录ip不能为空")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @NotNull(message = "最后登录时间不能为空")
    private LocalDateTime loginDate;

    @Schema(description = "用户状态：0-正常，1-禁用")
    @Range(max = 1, message = "用户状态范围在0-1之间")
    @NotNull(message = "用户状态不能为空")
    private Integer status;


}
