package io.roam.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.roam.auth.dto.request.SignInRequest;
import io.roam.auth.dto.request.SignUpRequest;
import io.roam.auth.dto.response.SignUpResponse;
import io.roam.auth.exception.UserAlreadyExistsException;
import io.roam.auth.exception.UserNotFoundException;
import io.roam.jwt.entity.JwtPayload;
import io.roam.jwt.entity.JwtToken;
import io.roam.jwt.service.JwtService;
import io.roam.user.entity.SocialType;
import io.roam.user.entity.User;
import io.roam.user.entity.UserRole;
import io.roam.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    

    public JwtToken signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        return jwtService.issueToken(JwtPayload.builder()
                .email(user.getEmail())
                .role(user.getUserRole())
                .build());
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadyExistsException();
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
}
