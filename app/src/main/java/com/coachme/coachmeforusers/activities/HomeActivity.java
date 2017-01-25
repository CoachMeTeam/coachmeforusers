package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.utils.Helper;

public class HomeActivity extends Activity {
    private Button signInButton;
    private TextView machineNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);

        initComponents();
    }

    @Override
    public void onBackPressed() {
    }

    private void initComponents() {
        String stringMachine = Helper.getSharedPreference("currentMachine");
        Machine machine = Helper.convertJsonToObject(stringMachine, Machine.class);

        machineNameTextView = (TextView) findViewById(R.id.machineNameTextView);
        signInButton = (Button) findViewById(R.id.signInButton);

        machineNameTextView.setText(machine.getMachineName());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}