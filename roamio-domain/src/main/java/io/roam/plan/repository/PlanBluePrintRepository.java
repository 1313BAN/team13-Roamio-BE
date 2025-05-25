package io.roam.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.roam.plan.entity.PlanBluePrint;

@Repository
public interface PlanBluePrintRepository extends JpaRepository<PlanBluePrint, Long> {
    List<PlanBluePrint> findByPlanId(Long planId);

    /**
     * 플랜 아이디를 기준으로 날짜와 순서를 기준으로 정렬된 플랜 블루프린트 목록을 조회합니다.  
     * @param planId 플랜 아이디
     * @return 정렬된 플랜 블루프린트 목록
     */
    List<PlanBluePrint> findByPlanIdOrderByDayAscPositionAsc(Long planId);
}
