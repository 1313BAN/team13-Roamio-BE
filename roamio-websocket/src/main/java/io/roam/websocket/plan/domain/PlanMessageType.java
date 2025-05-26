package io.roam.websocket.plan.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PlanMessageType {
    ENTER("ENTER"),
    LEAVE("LEAVE"),
    USER_LIST("USER_LIST"),
    POS("POS"),
    BLUEPRINT("BLUEPRINT"),
    BLUEPRINT_LIST("BLUEPRINT_LIST"),
    REMOVE_BLUEPRINT("REMOVE_BLUEPRINT"),
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
