package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.coachme.coachmeforusers.R;

public class HomeActivity extends Activity {
    private ImageView coachMeLogoImageView;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponents();
    }

    private void initComponents() {
        coachMeLogoImageView = (ImageView) findViewById(R.id.imageButton);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}