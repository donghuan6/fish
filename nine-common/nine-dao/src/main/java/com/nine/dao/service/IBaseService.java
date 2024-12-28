package com.nine.dao.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;


/**
 * 所有 dao 的基类，默认增加 list 与 page 方法，并且增加一个查询条件的接口
 *
 * @param <T> 具体映射 dao 类型
 * @author fan
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 列表查询
     *
     * @param queryDao 查询条件
     * @return 列表数据
     */
    default List<T> list(T queryDao) {
        return list(qw(queryDao));
    }

    /**
     * 分页查询
     *
     * @return 分页数据
     */
    default Page<T> page(Integer pageNum, Integer pageSize, T queryDao) {
        return page(new Page<>(
                        Optional.ofNullable(pageNum).orElse(1),
                        Optional.ofNullable(pageSize).orElse(10)),
                qw(queryDao));
    }

    /**
     * 默认实现的查询条件
     *
     * @return 条件
     */
    LambdaQueryWrapper<T> qw(T queryDao);

}
