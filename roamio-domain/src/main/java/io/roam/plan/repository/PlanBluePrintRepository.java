package io.roam.plan.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.roam.plan.entity.PlanBlueprint;

@Repository
public interface PlanBlueprintRepository extends JpaRepository<PlanBlueprint, Long> {
    List<PlanBlueprint> findByPlanId(Long planId);

    /**
     * 플랜 아이디와 아이디를 기준으로 플랜 블루프린트를 조회합니다.
     * @param id 플랜 블루프린트 아이디
     * @param planId 플랜 아이디
     * @return 플랜 블루프린트
     */
    Optional<PlanBlueprint> findByIdAndPlanId(Long id, Long planId);

    /**
     * 플랜 아이디를 기준으로 날짜와 순서를 기준으로 정렬된 플랜 블루프린트 목록을 조회합니다.  
     * @param planId 플랜 아이디
     * @return 정렬된 플랜 블루프린트 목록
     */
    @Query("""
        SELECT pb FROM PlanBlueprint pb
        JOIN FETCH pb.plan
        WHERE pb.plan.id = :planId
        ORDER BY pb.day ASC, pb.position ASC
    """)
    List<PlanBlueprint> findByPlanIdOrderByDayAscPositionAsc(Long planId);
}
