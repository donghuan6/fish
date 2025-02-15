package com.nine.user.controller;


import com.nine.common.domain.R;
import com.nine.redis.user.UserVo;
import com.nine.user.dto.LoginDto;
import com.nine.user.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户登录")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class LoginController {

    private final LoginService loginService;

    @Tag(name = "用户登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody @Valid LoginDto loginDto) {
        Map<String, Object> map = loginService.login(loginDto);
        return R.ok(map, "登录成功");
    }

    @Tag(name = "退出登录")
    @PostMapping("/logout")
    public R<Boolean> logout() {
        loginService.logout();
        return R.ok(Boolean.TRUE);
    }

    @Tag(name = "获取用户信息")
    @GetMapping("/info")
    public R<UserVo> info() {
        UserVo userVo = loginService.info();
        return R.ok(userVo);
    }


}
