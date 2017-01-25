package com.coachme.coachmeforusers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getContext;

public class Helper {
    public static final String API_ENDPOINT = ConfigPropertiesReader.getConfig().getProperty("coachme_api_endpoint");
    private static final String PREFERENCES_NAME = "coachme-preferences";

    static ObjectMapper mapper = new ObjectMapper();
    static SharedPreferences sharedpreferences = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    public static <T> T convertJsonToObject(String jsonString, Class<T> className) {
        try {
            return mapper.readValue(jsonString, className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> convertJsonToListOfObjects(String jsonString, TypeReference<List<T>> className) {
        try {
            return (List<T>) mapper.readValue(jsonString, className);
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

    public static String getStringDate(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringDate = formatter.format(date);
        return stringDate;
    }

    public static void storeSharedPreference(String key, String value) {
        Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreference(String key) {
        return sharedpreferences.getString(key, null);
    }
}
