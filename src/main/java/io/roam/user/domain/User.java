package io.roam.user.domain;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private UserRole userRole;
}
