package io.roam.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.roam.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
