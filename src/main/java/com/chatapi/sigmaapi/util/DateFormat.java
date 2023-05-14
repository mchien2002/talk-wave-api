package com.chatapi.sigmaapi.util;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormat implements JsonDeserializer<Date> {
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static Date getCurrentTime() {
        TimeZone timezone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Date currentTime = new Date();
        currentTime.setTime(currentTime.getTime() + timezone.getRawOffset());
        return currentTime;
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String dateString = json.getAsString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new JsonParseException("Failed to parse date " + dateString, e);
        }
    }
}
