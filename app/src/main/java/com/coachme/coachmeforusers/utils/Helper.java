package com.coachme.coachmeforusers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Helper {
    static ObjectMapper mapper = new ObjectMapper();


    public static <T> T convertJsonToObject(String jsonString, Class<T> className) {
        try {
            return mapper.readValue(jsonString, className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
