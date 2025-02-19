package com.nine.log.dto;

import com.nine.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "日志-分页参数")
public class SysLogPageDto extends PageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 694162979605021438L;

    @Length(max = 100, message = "服务名不能超过100个字符")
    @Schema(description = "服务名")
    private String serviceName;

    @Length(max = 100, message = "标题不能超过100个字符")
    @Schema(description = "标题")
    private String title;

    @Length(max = 100, message = "方法名不能超过100个字符")
    @Schema(description = "方法名")
    private String method;

    @Length(max = 100, message = "请求url不能超过100个字符")
    @Schema(description = "请求url")
    private String url;

    @Length(max = 20, message = "请求ip不能超过20个字符")
    @Schema(description = "请求ip")
    private String ip;

    @Range(min = 0, max = 1, message = "状态，0-成功，1-失败")
    @Schema(description = "状态，0-成功，1-失败")
    private Integer status;


}
