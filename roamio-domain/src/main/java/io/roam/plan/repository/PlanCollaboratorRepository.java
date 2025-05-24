package io.roam.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.roam.plan.entity.PlanCollaborator;
import io.roam.plan.entity.PlanCollaboratorPK;

@Repository
public interface PlanCollaboratorRepository extends JpaRepository<PlanCollaborator, PlanCollaboratorPK> {
    
    List<PlanCollaborator> findByPlanId(Long planId);

    List<PlanCollaborator> findByUserId(Long userId);

    PlanCollaborator findByPlanIdAndUserId(Long planId, Long userId);
}
