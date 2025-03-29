package com.nine.log.filter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;

public class StreamProxy {

    // 动态代理处理InputStream/OutputStream
    public static Object wrapStream(Object raw) {
        Class<?>[] interfaces = raw.getClass().getInterfaces();
        return Proxy.newProxyInstance(raw.getClass().getClassLoader(),
                interfaces,
                (proxy, method, args) -> {
                    if (method.getName().equals("getInputStream")) {
                        InputStream is = (InputStream) method.invoke(raw);
                        return new LoggingInputStream(is);
                    }
                    return method.invoke(raw, args);
                });
    }

    // 带元数据记录的InputStream
    private static class LoggingInputStream extends InputStream {

        private final InputStream delegate;
        private int bytesRead;

        public LoggingInputStream(InputStream is) {
            this.delegate = is;
        }

        @Override
        public int read() throws IOException {
            int data = delegate.read();
            if (data != -1) {
                bytesRead++;
            }
            return data;
        }

        @Override
        public String toString() {
            return String.format("InputStream[bytes=%d]", bytesRead);
        }
    }


}
