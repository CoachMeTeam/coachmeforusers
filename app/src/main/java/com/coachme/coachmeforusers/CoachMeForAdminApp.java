package com.coachme.coachmeforusers;

import android.app.Application;
import android.content.Context;

import com.coachme.coachmeforusers.model.User;

public class CoachMeForAdminApp extends Application {
    private static CoachMeForAdminApp instance;
    private static User currentUser;

    public static CoachMeForAdminApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CoachMeForAdminApp.currentUser = currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
