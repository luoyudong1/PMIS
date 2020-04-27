package com.kthw.pmis.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kthw.pmis.model.system.User;

/**
 * Created by YFZX-WB on 2017/5/11.
 */
public class VehicleMonitorHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleMonitorHandler.class);

    private static final Map<String, WebSocketSession> userSocketSessionMap =
            new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        TextMessage returnMessage = new TextMessage(message.getPayload() + " received at server");
        session.sendMessage(returnMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        User user = (User) attributes.get("AUTH_USER");
        if (user != null) {
            if (userSocketSessionMap.get(user.getUser_id()) == null) {
                LOG.info(user.getUser_id() + " connect to webSocket success...");
                userSocketSessionMap.put(user.getUser_id(), session);
            }
        } else {
            LOG.warn("user is null, cannot receive msg...");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        Iterator<Map.Entry<String, WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            if (entry.getValue().getId().equals(session.getId())) {
                iterator.remove();
                LOG.error(entry.getKey() + " webSocket connection closed, ", exception);
                break;
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Iterator<Map.Entry<String, WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            if (entry.getValue().getId().equals(session.getId())) {
                iterator.remove();
                LOG.info(entry.getKey() + " webSocket connection closed...");
                break;
            }
        }
    }

    public void broadcast(final WebSocketMessage message) throws IOException {
        Iterator<Map.Entry<String, WebSocketSession>> iterator = userSocketSessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, WebSocketSession> entry = iterator.next();
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
        }
    }

}
