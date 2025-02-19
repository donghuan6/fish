package com.nine.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nine.common.constans.Security;
import com.nine.common.constans.UserToken;
import com.nine.common.domain.R;
import com.nine.common.domain.user.UserVo;
import com.nine.common.utils.JwtUtil;
import com.nine.common.utils.servlet.ServletUtil;
import com.nine.gateway.config.WhitelistProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 网关鉴权，所有从网关进入的请求都需经过此过滤器
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final WhitelistProperties whitelistProperties;
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        // 匹配白名单，直接放行
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String w : whitelistProperties.getWhitelist()) {
            if (antPathMatcher.match(w, path)) {
                return chain.filter(exchange);
            }
        }
        // 认证逻辑
        String autoCode = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = ServletUtil.getToken(autoCode);
        if (StrUtil.isBlank(token)) {
            return unAuth(exchange, "Token 格式错误");
        }
        if (!JwtUtil.verifyToken(token)) {
            return unAuth(exchange, "Token 校验失败");
        }
        String userKey = JwtUtil.getUserKey(token);
        if (StrUtil.isBlank(userKey)) {
            return unAuth(exchange, "Token 缺少用户标识");
        }
        UserVo user = (UserVo) redisTemplate.opsForValue().get(userKey);
        if (Objects.isNull(user)) {
            return unAuth(exchange, "用户会话已失效");
        }
        // 设置用户信息至请求头
        ServerHttpRequest mutateRequest = request.mutate()
                .header(UserToken.USER_ID, user.getUserId().toString())
                .header(UserToken.USERNAME, user.getUsername())
                .header(UserToken.NICK_NAME, user.getNickName())
                .header(UserToken.USER_KEY, user.getUserKey())
                // 去掉微服务内部调用标记
                .headers(httpHeaders -> httpHeaders.remove(Security.FROM_SOURCE))
                .build();
        return chain.filter(exchange.mutate().request(mutateRequest).build());
    }

    private Mono<Void> unAuth(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        R<?> fail = R.fail(msg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(fail).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
