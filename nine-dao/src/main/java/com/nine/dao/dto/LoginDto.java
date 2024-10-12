package com.nine.dao.dto;

import com.nine.common.constans.Regular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Schema(description = "登录参数")
public class LoginDto implements Serializable {
    private static final long serialVersionUID = -1077639436117393853L;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(min = 5, max = 30, message = "用户名长度在5-30位之间")
    private String userName;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度在6-20位之间")
    @Pattern(regexp = Regular.PASSWORD, message = "必须包含数字、字母、特殊字符")
    private String password;

}
