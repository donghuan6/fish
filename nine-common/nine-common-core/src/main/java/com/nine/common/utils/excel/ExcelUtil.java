package com.nine.common.utils.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.nine.common.annotation.Excel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * poi excel .xlsx
 * excel 文件导入导出
 * todo 后期可以完善 导入 样式 还有复杂数据转换 等 功能
 */
@Slf4j
public class ExcelUtil {

    public static <T> void exportExcel(String sheetName, List<T> data, OutputStream os) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            if (CollUtil.isEmpty(data)) {
                // 生成一个空的 excel 导出
                workbook.write(os);
                return;
            }
            XSSFSheet sheet = workbook.createSheet(sheetName);
            ExcelData excelData = getExcelDto(data);
            writeHead(excelData, sheet);
            writeData(excelData, sheet);
            workbook.write(os);
        } catch (IOException e) {
            log.error("导出 Excel 失败, sheetName={}, data={}", sheetName, data, e);
            throw new RuntimeException("导出 Excel 失败," + e.getMessage());
        }
    }

    // 写表格中数据
    private static void writeData(ExcelData excelData, XSSFSheet sheet) {
        for (String[] rowData : excelData.getData()) {
            XSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            for (int i = 0; i < rowData.length; i++) {
                String val = rowData[i];
                row.createCell(i).setCellValue(val);
            }
        }
    }

    // 写表格头
    private static void writeHead(ExcelData excelData, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        for (int i = 0; i < excelData.getHeadName().size(); i++) {
            String name = excelData.getHeadName().get(i);
            row.createCell(i).setCellValue(name);
        }
    }

    private static <T> ExcelData getExcelDto(List<T> data) {
        ExcelData ed = new ExcelData();
        ed.setDataList((List<Object>) data);
        ed.setHeadName(getHeadList(data.get(0).getClass()));
        String[][] str = new String[data.size()][ed.getHeadName().size()];
        for (int i = 0; i < data.size(); i++) {
            T d = data.get(i);
            List<ExcelField> sortExcelField = getSortExcelField(d);
            for (int j = 0; j < sortExcelField.size(); j++) {
                ExcelField ef = sortExcelField.get(j);
                str[i][j] = Objects.isNull(ef.getValue()) ? "" : String.valueOf(ef.getValue());
            }
        }
        ed.setData(str);
        return ed;
    }

    private static List<String> getHeadList(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Excel.class))
                .map(f->f.getAnnotation(Excel.class).name())
                .collect(Collectors.toList());
    }

    private static <T> List<ExcelField> getSortExcelField(T data) {
        return Arrays.stream(data.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Excel.class))
                .map(f -> {
                    Excel an = f.getAnnotation(Excel.class);
                    ExcelField field = new ExcelField();
                    field.setField(f);
                    field.setOrder(an.order());
                    field.setName(an.name());
                    Object fieldValue = ReflectUtil.getFieldValue(data, f);
                    field.setValue(fieldValue);
                    field.setColumnType(an.type());
                    return field;
                })
                .sorted(Comparator.comparingInt(ExcelField::getOrder))
                .collect(Collectors.toList());
    }


}
