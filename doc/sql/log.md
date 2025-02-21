
```sql
-- 操作日志
CREATE TABLE `sys_log`
(
    `log_id`       bigint       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `service_name` varchar(100) NOT NULL COMMENT '服务名',
    `title`        varchar(100) NOT NULL COMMENT '标题',
    `method`       varchar(10)  NOT NULL COMMENT '请求方法',
    `class_method` varchar(100) NOT NULL COMMENT '方法名',
    `url`          varchar(100) NOT NULL COMMENT '请求url',
    `params`       text COMMENT '请求参数',
    `result`       varchar(2000) DEFAULT NULL COMMENT '返回结果',
    `username`     varchar(100)  default null comment '操作用户名',
    `ip`           varchar(20)  NOT NULL COMMENT '请求ip',
    `status`       tinyint       DEFAULT '0' COMMENT '状态，0-成功，1-失败',
    `cost_time`    bigint        DEFAULT '0' COMMENT '耗时',
    `error_msg`    varchar(2000) DEFAULT NULL COMMENT '错误信息',
    `create_time`  datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`    varchar(30)   DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
```