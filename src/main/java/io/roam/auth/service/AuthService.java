package io.roam.auth.service;

import io.roam.auth.dto.request.SocialSignInRequest;
import io.roam.auth.dto.request.SocialSignUpRequest;
import io.roam.auth.dto.response.SignInResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.auth.exception.EmailAlreadyExistsException;
import io.roam.auth.exception.InvalidPasswordException;
import io.roam.auth.exception.UserIdAlreadyExistsException;
import io.roam.auth.exception.UserNotFoundException;
import io.roam.external.oauth2.dto.response.SocialAuthResponse;
import io.roam.external.oauth2.service.GoogleOAuthService;
import io.roam.jwt.entity.JwtPayload;
import io.roam.jwt.entity.JwtToken;
import io.roam.jwt.service.JwtService;
import io.roam.user.entity.SocialType;
import io.roam.user.entity.User;
import io.roam.user.entity.UserRole;
import io.roam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final GoogleOAuthService googleOAuthService;

    public SignInResponse signIn(SignInRequest request) {
        User user = userRepository.findByUserId(request.userId())
            .orElseThrow(UserNotFoundException::new);
        
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return SignInResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(jwtService.issueToken(JwtPayload.builder()
                    .clientId(user.getUserId())
                    .socialType(user.getSocialType())
                    .role(user.getUserRole())
                    .build()))
                .build();
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserId(signUpRequest.userId())) {
            throw new UserIdAlreadyExistsException();
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        User user = User.builder()
            .userId(signUpRequest.userId())
            .email(signUpRequest.email())
            .password(encodedPassword)
            .socialType(SocialType.NONE)
            .name(signUpRequest.name())
            .userRole(UserRole.USER)
            .build();

        userRepository.save(user);

        return SignUpResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .userRole(user.getUserRole().name())
                .build();
    }

    public SignInResponse socialSignIn(SocialSignInRequest signInRequest) {
        SocialAuthResponse socialAuthResponse = switch (signInRequest.socialType()) {
            case GOOGLE -> googleOAuthService.authorize(signInRequest.token());
            default -> throw new IllegalArgumentException("Invalid social type");
        };

        log.trace("socialAuthResponse: {}", socialAuthResponse);

        String userId = socialAuthResponse.socialId() + "@" + socialAuthResponse.socialType();

        User user = userRepository.findByUserId(userId)
            // 존재하지 않으면 생성
            .orElseGet(() -> userRepository.save(User.builder()
                .userId(userId)
                .email(socialAuthResponse.email())
                .name(socialAuthResponse.name())
                .profileImageUrl(socialAuthResponse.profileImageUrl())
                .socialType(signInRequest.socialType())
                .userRole(UserRole.USER)
                .build()));

        log.trace("Social User: {}", user);

        return SignInResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(jwtService.issueToken(JwtPayload.builder()
                    .clientId(user.getUserId())
                    .socialType(user.getSocialType())
                    .role(user.getUserRole())
                    .build()))
                .build();
    }
}
