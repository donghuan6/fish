package com.nine.dingtalk.param.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenVo implements Serializable {
    private static final long serialVersionUID = 9074479561933985754L;

    private String accessToken;
    private Long expiresIn;

}
