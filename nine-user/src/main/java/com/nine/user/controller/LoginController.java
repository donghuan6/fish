package com.nine.user.controller;

import com.nine.common.domain.R;
import com.nine.dao.dto.LoginDto;
import com.nine.dao.vo.UserVo;
import com.nine.user.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/login")
    public R<Boolean> login(@RequestBody @Valid LoginDto loginDto) {
        UserVo userVo = loginService.login(loginDto);
        Map<String,Object> res=new HashMap<>();
        return R.ok(Boolean.TRUE, "hello");
    }



}
