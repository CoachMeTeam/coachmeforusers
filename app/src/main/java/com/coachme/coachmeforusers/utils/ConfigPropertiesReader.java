package com.coachme.coachmeforusers.utils;


import java.util.Properties;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getContext;

public class ConfigPropertiesReader {
    public static Properties getConfig() {
        try {
            Properties properties = new Properties();
            properties.load(getContext().getAssets().open("app.properties"));
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
