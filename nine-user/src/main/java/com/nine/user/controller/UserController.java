package com.nine.user.controller;

import com.nine.common.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public R<Boolean> get() {
        return R.ok(Boolean.TRUE, "hello");
    }

}
