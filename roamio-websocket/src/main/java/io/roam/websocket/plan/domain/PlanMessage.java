package io.roam.websocket.plan.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlanMessage<T> {
    private final PlanMessageType type;
    private final T payload;

    public static <T> PlanMessage<T> of(PlanMessageType type, T payload) {
        return new PlanMessage<>(type, payload);
    }
}
