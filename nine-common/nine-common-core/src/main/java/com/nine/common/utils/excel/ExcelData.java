package com.nine.common.utils.excel;

import lombok.Data;

import java.util.List;

@Data
public class ExcelData {

    // 原始数据
    private List<Object> dataList;

    // 表头
    private List<String> headName;

    // 数据
    private String[][] data;


}
