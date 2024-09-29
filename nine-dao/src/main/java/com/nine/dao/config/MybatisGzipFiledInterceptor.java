package com.nine.dao.config;

import cn.hutool.core.util.ReflectUtil;
import com.nine.common.utils.gzip.GzipUtil;
import com.nine.dao.annotation.Gzip;
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

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
        boolean isSelect = args.length > 2 && ms.getSqlCommandType() == SqlCommandType.SELECT;
        boolean noCache = true;
        Object obj;
        if (isSelect) {
            // 查询
            if (args.length == 6) {
                // 是否没有被缓存过
                noCache = !executor.isCached(ms, (CacheKey) args[4]);
            }
            obj = invocation.proceed();
        } else {
            // 更新或插入
            obj = args[1];
        }
        if (noCache) {
            Consumer<Object> handler = o -> gzipFieldHandler(isSelect, obj);
            if (obj instanceof Collection<?>) {
                ((Collection<?>) obj).forEach(handler);
            } else if (obj instanceof Map) {
                ((Map<?, ?>) obj).values().stream().limit(1).forEach(handler);
            } else {
                handler.accept(obj);
            }
        }
        return isSelect ? obj : invocation.proceed();
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
                .findFirst()
                .ifPresent(f -> {
                            Object fieldValue = ReflectUtil.getFieldValue(obj, f);
                            Optional.ofNullable(fieldValue).map(Object::toString)
                                    .ifPresent(v -> {
                                                String value = isSelect
                                                        ? GzipUtil.decodeBase64AndUnGzip(v)
                                                        : GzipUtil.toGzipAndBase64(v);
                                                ReflectUtil.setFieldValue(obj, f, value);
                                            }
                                    );
                        }
                );
    }
}
