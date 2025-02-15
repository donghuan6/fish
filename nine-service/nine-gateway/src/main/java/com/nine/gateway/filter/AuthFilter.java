package com.nine.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nine.common.domain.R;
import com.nine.gateway.config.WhitelistProperties;
import com.nine.redis.user.TokenService;
import com.nine.redis.user.UserVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
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
 * 网关鉴权
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final WhitelistProperties whitelistProperties;

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
        ServerHttpResponse response = exchange.getResponse();
        // 认证逻辑
        String authCode = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authCode)) {
            return unAuth(exchange, "认证令牌不能为空");
        }
        UserVo userVo = tokenService.getCacheUserVo(authCode);
        if (Objects.isNull(userVo)) {
            return unAuth(exchange, "认证令牌无效");
        }





        return null;
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
