package io.roam.user.service;

import org.springframework.stereotype.Service;

import io.roam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
}
