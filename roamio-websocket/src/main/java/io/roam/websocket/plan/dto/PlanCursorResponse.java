package io.roam.websocket.plan.dto;

import java.util.List;

import io.roam.websocket.plan.domain.CursorPos;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanCursorResponse {
    private String userId;
    private String name;
    private List<CursorPos> positions;
}
