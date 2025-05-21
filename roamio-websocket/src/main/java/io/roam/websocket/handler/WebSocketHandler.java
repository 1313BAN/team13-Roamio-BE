package io.roam.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            session.sendMessage(new TextMessage("Connected to the server"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}