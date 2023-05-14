package com.chatapi.sigmaapi.util;

import java.util.List;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.chatapi.sigmaapi.entity.model.SocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class EntityUtils {
    public static List<Object> getListOjFromJson(String jsonStr) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> listOj = mapper.readValue(jsonStr,
                new TypeReference<List<Object>>() {
                });
        return listOj;

    }

    public static String getKeyByValue(Map<String, WebSocketSession> map, WebSocketSession value) {
        return map.entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static Object getEntityBySocketResponse(SocketResponse sResponse, String key) {
        Gson gson = new Gson();
        Object entity = gson.fromJson(gson.toJson(sResponse.getPayload().get(key)), Object.class);
        return entity;
    }
}
