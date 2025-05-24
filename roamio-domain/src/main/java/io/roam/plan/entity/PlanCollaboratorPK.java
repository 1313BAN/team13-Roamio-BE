package io.roam.plan.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PlanCollaboratorPK implements Serializable {
    private Long planId;
    private Long userId;
}
