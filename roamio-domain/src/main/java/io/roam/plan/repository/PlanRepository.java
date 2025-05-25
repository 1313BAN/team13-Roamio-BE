package io.roam.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.roam.plan.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOwnerId(Long ownerId);

    Optional<Plan> findById(Long id);
}
