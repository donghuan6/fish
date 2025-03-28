package com.nine.log.domain;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.nine.common.annotation.Excel;
import com.nine.log.dto.SysLogPageDto;
import com.nine.log.vo.SysLogVo;
import com.nine.mybatis.annotation.Gzip;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@Schema(description = "日志信息")
@TableName(value = "sys_log")
public class SysLog {

    @Excel(name = "日志ID", type = Excel.ColumnType.NUMBER)
    @Schema(description = "日志ID")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @Excel(name = "服务名", type = Excel.ColumnType.STRING)
    @Length(max = 100, message = "服务名不能超过100个字符")
    @Schema(description = "服务名")
    private String serviceName;

    @Excel(name = "标题", type = Excel.ColumnType.STRING)
    @Length(max = 100, message = "标题不能超过100个字符")
    @Schema(description = "标题")
    private String title;

    @Excel(name = "请求方法", type = Excel.ColumnType.STRING)
    @Length(max = 10, message = "请求方法不能超过10个字符")
    @Schema(description = "请求方法")
    private String method;

    @Excel(name = "方法名", type = Excel.ColumnType.STRING)
    @Length(max = 100, message = "方法名不能超过100个字符")
    @Schema(description = "方法名")
    private String classMethod;

    @Excel(name = "请求url", type = Excel.ColumnType.STRING)
    @Length(max = 100, message = "请求url不能超过100个字符")
    @Schema(description = "请求url")
    private String url;

    @Excel(name = "请求参数", type = Excel.ColumnType.STRING)
    @Gzip
    @Length(max = 5000, message = "请求参数不能超过5000个字符")
    @Schema(description = "请求参数")
    private String params;

    @Excel(name = "返回结果", type = Excel.ColumnType.STRING)
    @Length(max = 2000, message = "返回结果不能超过2000个字符")
    @Schema(description = "返回结果")
    private String result;

    @Excel(name = "操作用户名", type = Excel.ColumnType.STRING)
    @Length(max = 100, message = "操作用户名不能超过100个字符")
    @Schema(description = "操作用户名")
    private String username;

    @Excel(name = "请求ip", type = Excel.ColumnType.STRING)
    @Length(max = 20, message = "请求ip不能超过20个字符")
    @Schema(description = "请求ip")
    private String ip;

    @Excel(name = "状态", type = Excel.ColumnType.NUMBER)
    @Range(min = 0, max = 1, message = "状态0-1")
    @Schema(description = "状态，0-成功，1-失败")
    private Integer status;

    @Excel(name = "耗时", type = Excel.ColumnType.NUMBER)
    @Schema(description = "耗时")
    private Long costTime;

    @Excel(name = "错误信息", type = Excel.ColumnType.STRING)
    @Length(max = 2000, message = "错误信息不能超过2000个字符")
    @Schema(description = "错误信息")
    private String errorMsg;

    @Excel(name = "创建时间", type = Excel.ColumnType.DATE)
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Excel(name = "创建人", type = Excel.ColumnType.STRING)
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    public SysLog setFailStatus() {
        this.status = 1;
        return this;
    }

    public SysLog setOkStatus() {
        this.status = 0;
        return this;
    }

    public SysLog convert(SysLogPageDto dto) {
        SysLog sysLog = new SysLog();
        BeanUtil.copyProperties(dto, sysLog, false);
        return sysLog;
    }

    public SysLogVo convert() {
        SysLogVo vo = new SysLogVo();
        BeanUtil.copyProperties(this, vo, false);
        return vo;
    }

}