package io.roam.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.roam.plan.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOwnerId(Long ownerId);

    Optional<Plan> findById(Long id);
    
    /**
     * planId에 해당하는 Plan 엔티티의 프록시 객체를 반환합니다.
     * 이 메서드는 실제 Plan 엔티티를 데이터베이스에서 조회하지 않고 프록시 객체만 생성합니다.
     * @param planId 플랜 아이디
     * @return Plan 엔티티의 프록시 객체
     */
    @Query("SELECT p FROM Plan p WHERE p.id = :planId")
    Plan getReference(@Param("planId") Long planId);
}
