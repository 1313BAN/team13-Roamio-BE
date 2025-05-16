package io.roam.jwt;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import io.roam.jwt.entity.AuthUserDetail;
import io.roam.user.type.SocialType;
import io.roam.user.type.UserRole;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    private UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static UserAuthentication of(String socialType, String clientId, UserRole role) {
        // 사용자 정보 생성 (principal)
        AuthUserDetail userDetail = AuthUserDetail.builder()
            .socialType(SocialType.valueOf(socialType))
            .clientId(clientId)
            .build();

        // 사용자 권한 생성 (authorities)
        List<GrantedAuthority> authorities = List.of(role);

        return new UserAuthentication(
            userDetail,
            null,
            authorities
        );
    }
}
