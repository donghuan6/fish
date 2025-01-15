package com.nine.domain.log;

import com.baomidou.mybatisplus.annotation.*;
import com.nine.dao.annotation.Gzip;
import com.nine.dao.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Schema(description = "日志信息")
@Data
@TableName(value = "sys_log")
public class Log extends BaseEntity {

    @Schema(description = "日志ID")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @Length(max = 100, message = "服务名不能超过100个字符")
    @Schema(description = "服务名")
    private String serviceName;

    @Length(max = 100, message = "方法名不能超过100个字符")
    @Schema(description = "方法名")
    private String method;

    @Length(max = 100, message = "请求url不能超过100个字符")
    @Schema(description = "请求url")
    private String url;

    @Gzip
    @Length(max = 5000, message = "请求参数不能超过5000个字符")
    @Schema(description = "请求参数")
    private String params;

    @Length(max = 200, message = "返回结果不能超过200个字符")
    @Schema(description = "返回结果")
    private String result;

    @Length(max = 20, message = "请求ip不能超过20个字符")
    @Schema(description = "请求ip")
    private String ip;

    @Range(min = 0, max = 1, message = "状态不能小于0")
    @Schema(description = "状态")
    private Integer status;

    @Length(max = 1000, message = "错误信息不能超过1000个字符")
    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;


}