package io.roam.plan.repository;

import java.util.List;

import io.roam.plan.entity.PlanCollaboratorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.roam.plan.entity.PlanCollaborator;
import io.roam.plan.entity.PlanCollaboratorPK;

@Repository
public interface PlanCollaboratorRepository extends JpaRepository<PlanCollaborator, PlanCollaboratorPK> {
    
    List<PlanCollaborator> findByPlanId(Long planId);

    List<PlanCollaborator> findByUserId(Long userId);

    Boolean existsByPlanIdAndUserId(Long planId, Long userId);

    @Query("""
        SELECT pc FROM PlanCollaborator pc
        JOIN FETCH pc.plan p
        JOIN FETCH p.owner
        WHERE pc.user.id = :userId
    """)
    List<PlanCollaborator> findWithPlanAndOwnerByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT  pc.plan.id as planId,
                pc.user.userId as userId,
                pc.user.email as email,
                pc.user.name as userName,
                pc.user.profileImageUrl as profileImageUrl
        FROM PlanCollaborator pc
        WHERE pc.plan.id = :planId
    """)
    List<PlanCollaboratorInfo> getCollaboratorListByPlanId(@Param("planId") Long planId);

    @Query("""
        SELECT  pc.plan.id as planId,
                pc.user.userId as userId,
                pc.user.email as email,
                pc.user.name as userName,
                pc.user.profileImageUrl as profileImageUrl
        FROM PlanCollaborator pc
        WHERE pc.plan.id IN :planIds
    """)
    List<PlanCollaboratorInfo> getCollaboratorListByPlanIds(@Param("planIds") List<Long> planIds);
}
