package com.nine.log.vo;

import com.nine.log.dao.Log;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "日志-返回")
public class LogVo extends Log implements Serializable {

    @Serial
    private static final long serialVersionUID = 8286234087727859427L;


}
