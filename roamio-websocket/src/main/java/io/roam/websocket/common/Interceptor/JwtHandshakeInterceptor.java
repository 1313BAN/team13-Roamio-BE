package io.roam.websocket.common.Interceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            // 파라미터에서 토큰 추출

            Map<String, String> queryParams = parseQueryString(request.getURI().getQuery());

            String token = queryParams.get("token");
            
            if (token == null) {
                return false;
            }

            // 토큰 검증
            Claims claims = jwtTokenProvider.getClaims(token);

            // 인증 정보 설정
            String userId = jwtTokenProvider.getUserId(claims);
            String name = jwtTokenProvider.getName(claims);
            String socialType = jwtTokenProvider.getSocialType(claims);
            String role = jwtTokenProvider.getRole(claims);
        
            Authentication authentication = UserAuthentication.of(socialType, userId, UserRole.valueOf(role));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            attributes.put("queryParams", queryParams);
            attributes.put("userId", userId);
            attributes.put("name", name);
            attributes.put("socialType", socialType);
            attributes.put("role", role);

            log.info("userId: {} / socialType: {} / role: {}", userId, socialType, role);
            return true;
        } catch (Exception e) {
            log.error("Handshake failed: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // NOP
    }

    /**
     * 쿼리 문자열을 파싱하여 Map으로 변환
     */
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> queryParams = new HashMap<>();
        
        if (query == null || query.isEmpty()) {
            return queryParams;
        }
        
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = decodeUrlComponent(pair.substring(0, idx));
                String value = decodeUrlComponent(pair.substring(idx + 1));
                
                queryParams.put(key, value);
            }
        }
        
        return queryParams;
    }
    
    /**
     * URL 인코딩된 컴포넌트를 디코딩
     */
    private String decodeUrlComponent(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error decoding URL component: {}", e.getMessage(), e);
            return s;
        }
    }
}