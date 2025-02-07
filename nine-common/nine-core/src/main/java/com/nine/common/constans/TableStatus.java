package com.nine.common.constans;

/**
 * 表状态
 */
public interface TableStatus {

    interface Deleted {
        /**
         * 正常状态
         */
        int NORMAL = 0;
        /**
         * 删除状态
         */
        int DELETED = 1;
    }

    interface Disabled {
        /**
         * 正常状态
         */
        int NORMAL = 0;
        /**
         * 禁用状态
         */
        int DISABLED = 1;
    }

}
