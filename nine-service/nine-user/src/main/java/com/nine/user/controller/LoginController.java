package com.nine.user.controller;

import com.nine.common.domain.R;
import com.nine.user.dto.LoginDto;
import com.nine.user.service.LoginService;
import com.nine.user.vo.UserVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户登录")
@RestController
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Tag(name = "用户登录")
    @PostMapping("/login")
    public R<Boolean> login(@RequestBody @Valid LoginDto loginDto) {
        UserVo userVo = loginService.login(loginDto);
        Map<String, Object> res = new HashMap<>();
        return R.ok(Boolean.TRUE, "hello");
    }

    @Tag(name = "退出登录")
    @PostMapping("/logout")
    public R<Boolean> logout() {


        return R.ok(Boolean.TRUE, "hello");
    }

}
