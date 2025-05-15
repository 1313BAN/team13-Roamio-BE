package io.roam.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.roam.user.entity.SocialType;
import io.roam.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}
