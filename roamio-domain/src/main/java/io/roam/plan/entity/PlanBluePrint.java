package io.roam.plan.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.roam.common.entity.BaseTimeEntity;
import io.roam.plan.type.PlaceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_plan_blue_print_plan_id", columnList = "plan_id")
})
@Entity
public class PlanBluePrint extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(columnDefinition = "SMALLINT UNSIGNED")
    private Integer day;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    private String placeId;

    @Column(columnDefinition = "DECIMAL(10, 5)", nullable = false)
    private BigDecimal position;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    private Double latitude;

    private Double longitude;

    private String memo;
}
