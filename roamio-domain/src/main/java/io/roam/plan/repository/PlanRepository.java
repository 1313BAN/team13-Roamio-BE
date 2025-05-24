package io.roam.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.roam.plan.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOwnerId(Long ownerId);
}
