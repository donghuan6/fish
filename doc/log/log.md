
```sql
-- 操作日志
create table `sys_user`
(
    `id`          bigint       not null primary key auto_increment comment 'id',
    `user_name`   varchar(30)  not null comment '用户名/登录名',
    `phone`       varchar(20)  not null comment '手机号',
    `nick_name`   varchar(50)  not null comment '昵称',
    `password`    varchar(100) not null comment '密码',
    `email`       varchar(50)           default '' comment '邮箱',
    `sex`         tinyint               default 0 comment '性别：0-保密，1-男，2-女',
    `avatar`      varchar(100)          default '' comment '头像',
    `login_ip`    varchar(50)  not null comment '最后登录ip',
    `login_date`  datetime     not null comment '最后登录时间',
    `status`      tinyint      not null default 0 comment '用户状态：0-正常，1-禁用',
    `deleted`     tinyint      not null default 0 comment '0-存在，1-删除',
    `create_time` datetime     not null comment '创建时间',
    `create_by`   varchar(30)  not null comment '创建人',
    `update_time` datetime     not null comment '更新时间',
    `update_by`   varchar(30)  not null comment '更新人',
    unique key `idx_user_name` (`user_name`)
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci comment ='用户';
```