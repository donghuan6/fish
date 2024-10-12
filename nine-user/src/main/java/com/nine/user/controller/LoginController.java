package com.nine.user.controller;

import com.nine.common.domain.R;
import com.nine.dao.dto.LoginDto;
import com.nine.user.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户登录")
@RestController
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Tag(name = "用户登录")
    @GetMapping("/login")
    public R<Boolean> login(@RequestBody @Valid LoginDto loginDto) {
        loginService.login(loginDto);
        return R.ok(Boolean.TRUE, "hello");
    }

}
