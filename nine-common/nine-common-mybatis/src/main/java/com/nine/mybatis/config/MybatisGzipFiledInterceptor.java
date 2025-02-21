package com.nine.mybatis.config;

import cn.hutool.core.util.ReflectUtil;
import com.nine.common.utils.gzip.GzipUtil;
import com.nine.mybatis.annotation.Gzip;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 会对某些大文本或者会乱码的内容进行 gzip 压缩与解压缩
 * <p>
 * 拦截 update(对于 mybatis-plus 来说，update 包括 insert 和 update)
 * 拦截 query
 *
 * @author fan
 */
@Component
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                CacheKey.class, BoundSql.class})
})
public class MybatisGzipFiledInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object result;
        if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
            CacheKey cacheKey;
            BoundSql boundSql;
            // 查询
            if (args.length == 6) {
                boundSql = (BoundSql) args[5];
                cacheKey = (CacheKey) args[4];
            } else {
                // 对于没有明确 CacheKey 的查询，创建 CacheKey
                boundSql = ms.getBoundSql(args[1]);
                cacheKey = executor.createCacheKey(ms, args[1], (RowBounds) args[2], boundSql);
            }
            // 是否缓存
            boolean isCached = executor.isCached(ms, cacheKey);
            // 查询操作
            result = invocation.proceed();
            if (!isCached) {
                // 未缓存，处理 gzip 字段
                processGzipFields(result, true);
            }
        } else {
            // 更新操作
            result = args[1];
            processGzipFields(result, false);
            result = invocation.proceed();
        }
        return result;
    }

    private void processGzipFields(Object obj, boolean isSelect) {
        Consumer<Object> handler = o -> gzipFieldHandler(isSelect, o);
        if (obj instanceof Collection) {
            ((Collection<?>) obj).forEach(handler);
        } else if (obj instanceof Map) {
            ((Map<?, ?>) obj).values().stream().limit(1).forEach(handler);
        } else {
            handler.accept(obj);
        }
    }

    private void gzipFieldHandler(boolean isSelect, Object obj) {
        if (obj == null) {
            return;
        }
        Class<?> objClass = obj.getClass();
        // 排除非 java 内置对象，只处理自定义的
        if (objClass.getName().startsWith("java.")
                || Modifier.isFinal(objClass.getModifiers()) && objClass.getSuperclass() != null
                || objClass.isInterface()) {
            return;
        }
        ReflectUtil.getFieldMap(objClass).values()
                .stream()
                .filter(f -> f.getAnnotation(Gzip.class) != null
                        && f.getType().equals(String.class))
                .filter(f -> ReflectUtil.getFieldValue(obj, f.getName()) != null)
                .forEach(f -> {
                            String fieldValue = ReflectUtil.getFieldValue(obj, f).toString();
                            if (StringUtils.hasText(fieldValue)) {
                                String value = isSelect ? GzipUtil.decodeBase64AndUnGzip(fieldValue) : GzipUtil.toGzipAndBase64(fieldValue);
                                ReflectUtil.setFieldValue(obj, f, value);
                            }
                        }
                );
    }
}
