package io.roam.user.service;

import org.springframework.beans.factory.annotation.Autowired;

import io.roam.user.dto.request.SignUpRequest;
import io.roam.user.entity.User;
import io.roam.user.entity.UserRole;
import io.roam.user.repository.UserRepository;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User signUp(SignUpRequest signUpRequest) {
        User user = User.builder()
            .email(signUpRequest.email())
            .password(signUpRequest.password())
            .name(signUpRequest.name())
            .userRole(UserRole.USER)
            .build();

        userRepository.save(user);

        return user;
    }
}
