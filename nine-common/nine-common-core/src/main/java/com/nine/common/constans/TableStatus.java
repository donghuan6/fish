package com.nine.common.constans;

/**
 * 表状态
 */
public interface TableStatus {

    /**
     * 是否删除
     */
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

    /**
     * 是否禁用
     */
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

    /**
     * 0-成功，1-失败
     */
    interface ResultStatus {
        /**
         * 成功
         */
        int SUCCESS = 0;
        /**
         * 失败
         */
        int FAIL = 1;
    }

}
