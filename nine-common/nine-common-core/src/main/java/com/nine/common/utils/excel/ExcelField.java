package com.nine.common.utils.excel;

import com.nine.common.annotation.Excel;
import lombok.Data;

import java.lang.reflect.Field;

@Data
public class ExcelField {

    private Field field;

    private int order;

    private String name;

    private Object value;

    private Excel.ColumnType columnType;

}