package com.nine.common.utils.spel;

import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * spel 表达式工具计算
 *
 * @author fan
 */
public class SpelExpressionUtil {


    /**
     * @param method     方法
     * @param args       方法参数列表
     * @param spel       spring spel 表达式
     * @param resultType 返回类型
     * @param <T>        返回类型
     * @return 返回执行 spel 的值
     */
    public static <T> T get(Method method, Object[] args, String spel, Class<T> resultType) {
        if (!StringUtils.hasText(spel)) {
            return null;
        }
        StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
        // 获取方法的参数名列表
        String[] parameterNames = discoverer.getParameterNames(method);
        if (Objects.isNull(parameterNames) || parameterNames.length == 0) {
            return null;
        }
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            context.setVariable(parameterName, args[i]);
        }
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(spel);
        return expression.getValue(context, resultType);
    }


}
