package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coachme.coachmeforusers.utils.Helper;

public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Class<? extends Activity> activityClass;
        if (Helper.getSharedPreference("currentMachine") != null) {
            activityClass = HomeActivity.class;
        } else {
            activityClass = ChooseMachineActivity.class;
        }
        Intent intent = new Intent(getApplicationContext(), activityClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
