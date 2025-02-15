
```sql
-- 用户
create table `sys_user`
(
    `user_id`     bigint       not null primary key auto_increment comment 'id',
    `username`    varchar(30)  not null comment '用户名/登录名',
    `phone`       varchar(20)  not null comment '手机号',
    `nick_name`   varchar(50)  not null comment '昵称',
    `password`    varchar(100) not null comment '密码',
    `email`       varchar(50)           default '' comment '邮箱',
    `sex`         tinyint               default 0 comment '性别:0-保密,1-男,2-女',
    `avatar`      varchar(100)          default '' comment '头像',
    `login_ip`    varchar(50)           default null comment '最后登录ip',
    `login_date`  datetime     not null comment '最后登录时间',
    `status`      tinyint      not null default 0 comment '用户状态:0-正常,1-禁用',
    `deleted`     tinyint      not null default 0 comment '0-存在,1-删除',
    `create_time` datetime     not null comment '创建时间',
    `create_by`   varchar(30)  not null comment '创建人',
    `update_time` datetime     not null comment '更新时间',
    `update_by`   varchar(30)  not null comment '更新人',
    unique key `idx_username` (`username`)
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci comment ='用户';

insert into sys_user (user_id, username, phone, nick_name, password, login_ip, login_date, create_time, create_by,
                      update_time, update_by)
values (default, 'admin', '000', 'admin', '123', '127.0.0.1', now(), now(), 'admin', now(), 'admin'),
       (default, 'user', '111', 'user', '123', '127.0.0.1', now(), now(), 'admin', now(), 'admin');

-- 权限
create table `sys_permit`
(
    `permit_id`   int          not null primary key auto_increment comment 'id',
    `permit_name` varchar(50)  not null comment '权限名',
    `path`        varchar(100) not null comment '路径',
    `parent_id`   int          default 0 comment '父权限主键',
    `menu_type`   char(1)      default '' comment 'M-目录菜单,C-子菜单,B-按键',
    `menu_show`   tinyint      default 0 comment '0-显示,1-隐藏',
    `menu_status` tinyint      default 0 comment '0-正常,1-停用',
    `permit`      varchar(50)  not null comment '权限标识',
    `icon`        varchar(100) default '' comment '图标',
    `remark`      varchar(100) default '' comment '备注'
)engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci comment ='权限';

insert into sys_permit(permit_id, permit_name, path, parent_id, menu_type, permit, remark)
values (default, '登录', '/login', 0, 'B', 'login:login', '登录权限'),
       (default, '进入登录页面', '/login', 0, 'B', 'login:toLogin', '进入登录页面权限'),
       (default, '退出', '/logout', 0, 'B', 'logout', '退出权限'),
       (default, '注册', '/register', 0, 'B', 'reg:register', '注册权限'),
       (default, '进入注册页面', '/toRegister', 0, 'B', 'reg:toRegister', '进入注册页面权限'),
       (default, '进入主页面', '/main', 0, 'B', 'main', '进入主页面权限'),
       (default, '用户管理', '', 0, 'M', 'user:manager', '用户管理权限'),
       (default, '用户查询', '/user/list', 7, 'C', 'user:list', '用户查询权限'),
       (default, '用户新增', '/user/add', 7, 'B', 'user:add', '用户新增权限'),
       (default, '进入用户新增页面', '/user/toAdd', 7, 'B', 'user:toAdd', '进入用户新增页面权限'),
       (default, '用户修改', '/user/modify', 7, 'B', 'user:modify', '用户修改权限'),
       (default, '进入用户修改页面', '/user/toModify', 7, 'B', 'user:toModify', '进入用户修改页面权限'),
       (default, '用户删除', '/user/remove', 7, 'B', 'user:remove', '用户删除权限');

-- 角色
create table `sys_role`
(
    `role_id`   int         not null primary key auto_increment comment 'id',
    `role_name` varchar(30) not null comment '角色名',
    `role_key`  varchar(30) not null comment '角色key',
    `remark`    varchar(100) default '' comment '备注'
) engine = innodb
    default charset = utf8mb4 
    collate = utf8mb4_0900_ai_ci comment = '角色';

insert into sys_role(role_id, role_name, role_key, remark)
values (default, '管理员', 'admin', '业务权限'),
       (default, '普通用户', 'user', '基础权限');

-- 角色与权限
create table `sys_role_permit`
(
    `role_id`   int not null comment '角色id',
    `permit_id` int not null comment '权限id'
)engine = innodb
default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci comment ='角色与权限';

insert into sys_role_permit(role_id, permit_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6);

-- 角色与用户
create table `sys_role_user`
(
    `role_id` int    not null comment '角色id',
    `user_id` bigint not null comment '用户id'
)engine = innodb
default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci comment ='角色与用户';

insert into sys_role_user(role_id, user_id)
values (1, 1),
       (2, 2);
```