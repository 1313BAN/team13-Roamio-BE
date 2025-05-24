package io.roam.websocket.plan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PlanMessageType {
    ENTER("ENTER"),
    LEAVE("LEAVE"),
    POS("POS"),
    UNKNOWN("UNKNOWN");

    private final String type;

    PlanMessageType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static PlanMessageType fromString(String value) {
        try {
            return PlanMessageType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}
