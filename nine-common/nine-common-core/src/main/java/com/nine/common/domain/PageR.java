package com.nine.common.domain;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;

import java.util.List;

/**
 * 统一分页响应体
 *
 * @param <T> data 类型
 * @author fan
 */
@Getter
public final class PageR<T> extends R<List<T>> {

    /**
     * 总记录数
     */
    private final long total;

    public PageR(List<T> data, long total) {
        super(data);
        this.total = total;
    }

    public PageR(List<T> data) {
        super(data);
        this.total = CollUtil.size(data);
    }


}
