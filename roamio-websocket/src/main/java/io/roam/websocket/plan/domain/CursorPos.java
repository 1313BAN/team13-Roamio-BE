package io.roam.websocket.plan.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursorPos {
    private int x;
    private int y;
}
