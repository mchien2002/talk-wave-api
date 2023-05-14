package com.chatapi.sigmaapi.entity.model;

import java.util.HashMap;
import java.util.Map;

import com.chatapi.sigmaapi.constant.SocketRequestType;
import com.google.gson.Gson;

public class SocketRequest {
    private String event;
    private Map<String, ?> payload;

    public SocketRequest errorObject(String errorString) {
        this.event = SocketRequestType.SOCKET_REQUEST_ERROR;
        Map<String, String> errorMap = new HashMap<>();
        if (errorString != null) {

            errorMap.put("error", errorString);
        } else {
            errorMap.put("error", "Action failed");
        }
        this.payload = errorMap;
        return this;
    }

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

    public SocketRequest() {
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
