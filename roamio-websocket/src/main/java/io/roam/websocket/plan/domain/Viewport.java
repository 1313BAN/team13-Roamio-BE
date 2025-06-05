package io.roam.websocket.plan.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Viewport {
    private double lat;
    private double lng;
    private int zoom;
} 