package io.roam.websocket.Interceptor;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.jsonwebtoken.Claims;
import io.roam.jwt.JwtTokenProvider;
import io.roam.jwt.UserAuthentication;
import io.roam.user.type.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final String TOKEN_QUERY_PARAM = "token=";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 파라미터에서 토큰 추출
        String token = request.getURI() != null 
            ? Arrays.stream(request.getURI().getQuery().split("&"))
                .filter(param -> param.startsWith(TOKEN_QUERY_PARAM))
                .findFirst()
                .map(param -> param.substring(TOKEN_QUERY_PARAM.length()))
                .orElse(null)
            : null;

        if (token == null) {
            return false;
        }

        // 토큰 검증
        Claims claims;
        try {
            claims = jwtTokenProvider.getClaims(token);
        } catch (Exception e) {
            return false;
        }

        // 인증 정보 설정
        String clientId = jwtTokenProvider.getClientId(claims);
        String socialType = jwtTokenProvider.getSocialType(claims);
        String role = jwtTokenProvider.getRole(claims);
    
        Authentication authentication = UserAuthentication.of(socialType, clientId, UserRole.valueOf(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        attributes.put("clientId", clientId);
        attributes.put("socialType", socialType);
        attributes.put("role", role);

        log.info("clientId: {} / socialType: {} / role: {}", clientId, socialType, role);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // NOP
    }
}