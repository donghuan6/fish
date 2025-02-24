package com.nine.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 对应数据库表，实体都继承此类
 *
 * @author fan
 */
@Getter
@Setter
public abstract class BaseEntity {

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    protected String createBy;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

    @TableField(fill = FieldFill.UPDATE)
    protected String updateBy;

    /**
     * 0-存在
     * 1-删除
     */
    @TableField
    protected Integer deleted;

}
