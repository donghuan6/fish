package com.nine.log.vo;

import com.nine.log.domain.SysLog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "日志-返回")
public class SysLogVo extends SysLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 8286234087727859427L;


}
