package io.roam.plan.entity;

import java.time.LocalDateTime;
import java.util.List;

import io.roam.common.entity.BaseTimeEntity;
import io.roam.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_plan_owner_id", columnList = "owner_id")
})
@Entity
public class Plan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "end_date", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanBluePrint> bluePrints;
}
