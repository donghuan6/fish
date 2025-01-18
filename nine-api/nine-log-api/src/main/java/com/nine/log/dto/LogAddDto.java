package com.nine.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@Schema(description = "日志-保存参数")
public class LogAddDto implements Serializable {
    private static final long serialVersionUID = -2587775486728967779L;

    @Length(max = 100, message = "服务名不能超过100个字符")
    @Schema(description = "服务名")
    private String serviceName;

    @Length(max = 100, message = "方法名不能超过100个字符")
    @Schema(description = "方法名")
    private String method;

    @Length(max = 100, message = "请求url不能超过100个字符")
    @Schema(description = "请求url")
    private String url;

    @Length(max = 5000, message = "请求参数不能超过5000个字符")
    @Schema(description = "请求参数")
    private String params;

    @Length(max = 2000, message = "返回结果不能超过2000个字符")
    @Schema(description = "返回结果")
    private String result;

    @Length(max = 20, message = "请求ip不能超过20个字符")
    @Schema(description = "请求ip")
    private String ip;

    @Range(min = 0, max = 1, message = "状态，0-正常，1-异常")
    @Schema(description = "状态，0-正常，1-异常")
    private Integer status;

    @Length(max = 1000, message = "错误信息不能超过1000个字符")
    @Schema(description = "错误信息")
    private String errorMsg;

}
