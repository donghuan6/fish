package com.nine.user.controller;

import com.nine.common.domain.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户管理")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "获取用户信息")
    @GetMapping("/")
    public R<Boolean> get() {
        return R.ok(Boolean.TRUE, "hello");
    }

}
