package com.nine.common.utils.resources;


import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * 针对 resources 目录下资源相关工具
 *
 * @author fan
 */
@Slf4j
public class ResourcesUtil {


    /**
     * 加载 resources 下指定目录文件，读取文件内容并返回 String
     */
    public static String readLineAsString(String resourcesDir, String filename) {
        if (CharSequenceUtil.isBlank(filename)) {
            return null;
        }
        String resourceFile = Objects.isNull(resourcesDir) ? filename : String.join("/", resourcesDir, filename);
        try (
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        ResourcesUtil.class
                                                .getClassLoader()
                                                .getResourceAsStream(resourceFile))))
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error("加载 resource 异常：resourcesDir={}, filename={}", resourcesDir, filename, e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
