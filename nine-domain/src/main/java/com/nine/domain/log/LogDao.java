package com.nine.domain.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nine.dao.annotation.Gzip;
import com.nine.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * 日志信息
 *
 * @author fan
 */
@Data
@TableName(value = "log", excludeProperty = {"updateAt", "updateBy", "deleted"})
public class LogDao extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1662147345689781903L;

    @ApiModelProperty("服务名")
    @Length(max = 100, message = "服务名不能超过100个字符")
    private String serviceName;

    @Length(max = 100, message = "方法名不能超过100个字符")
    private String method;

    @Length(max = 100, message = "请求url不能超过100个字符")
    private String url;

    @Gzip
    @Length(max = 5000, message = "请求参数不能超过5000个字符")
    private String params;

    @Length(max = 200, message = "返回参数不能超过200个字符")
    private String result;

    @Length(max = 20, message = "ip不能超过20个字符")
    private String ip;

    @Range(min = 0, max = 1, message = "状态不能小于0")
    private Integer status;

    @Length(max = 1000, message = "错误信息不能超过1000个字符")
    private String errorMsg;


}
