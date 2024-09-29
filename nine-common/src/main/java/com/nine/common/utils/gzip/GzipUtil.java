package com.nine.common.utils.gzip;


import cn.hutool.core.text.CharSequenceUtil;
import com.nine.common.ex.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip 相关压缩与解压工具
 *
 * @author fan
 */
@Slf4j
public class GzipUtil {


    /**
     * 使用 gzip 压缩字符串,并使用 base64 编码
     */
    public static String toGzipAndBase64(String str) {
        if (CharSequenceUtil.isBlank(str)) {
            return str;
        }
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
        ) {
            gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            log.error("使用 gzip 压缩字符串,并使用 base64 编码，异常，str={}", str, e);
            throw new ServiceException("使用 gzip 压缩字符串,并使用 base64 编码，异常");
        }
    }

    /**
     * 使用 base64 解码，并使用 gzip 解压字符串
     */
    public static String decodeBase64AndUnGzip(String compressString) {
        if (CharSequenceUtil.isBlank(compressString)) {
            return compressString;
        }
        byte[] decode = Base64.getDecoder().decode(compressString);
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
                GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] buff = new byte[1024 * 10];
            int n;
            while ((n = gzipInputStream.read(buff)) != -1) {
                byteArrayOutputStream.write(buff, 0, n);
            }
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("使用 base64 解码，并使用 gzip 解压字符串，异常，compressString={}", compressString, e);
            throw new ServiceException("使用 base64 解码，并使用 gzip 解压字符串，异常");
        }
    }

}
