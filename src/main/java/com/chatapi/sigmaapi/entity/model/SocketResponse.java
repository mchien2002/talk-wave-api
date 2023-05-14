package com.chatapi.sigmaapi.entity.model;

import java.util.Map;

public class SocketResponse {
    private String event;
    private Map<String, ?> payload;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, ?> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, ?> payload) {
        this.payload = payload;
    }
}
