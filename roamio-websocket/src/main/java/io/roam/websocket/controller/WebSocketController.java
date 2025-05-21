package io.roam.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
public class WebSocketController {
    @GetMapping("/test")
    public String ws() {
        return "Hello, World!";
    }
}