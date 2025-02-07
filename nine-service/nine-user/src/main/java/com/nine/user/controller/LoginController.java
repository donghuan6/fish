package com.nine.user.controller;

import com.nine.common.constans.UserToken;
import com.nine.common.domain.R;
import com.nine.user.dto.LoginDto;
import com.nine.user.service.LoginService;
import com.nine.user.service.TokenService;
import com.nine.user.vo.UserVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenService tokenService;

    @Tag(name = "用户登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody @Valid LoginDto loginDto) {
        UserVo userVo = loginService.login(loginDto);
        String token = tokenService.createToken(userVo);
        Map<String, Object> map = new HashMap<>(2);
        map.put(UserToken.USER_TOKEN, token);
        map.put(UserToken.EXPIRE_MINUTE, UserToken.EXPIRE_MINUTE_TIME);
        return R.ok(map, "登录成功");
    }


    @Tag(name = "退出登录")
    @PostMapping("/logout")
    public R<Boolean> logout(HttpServletRequest request) {
        tokenService.delToken(request);
        return R.ok(Boolean.TRUE);
    }


}
