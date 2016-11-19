package com.coachme.coachmeforusers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;

import java.util.concurrent.TimeUnit;

public class ReservationTimeRemainingActivity extends AppCompatActivity {
    private static final String FORMAT = "%02d:%02d:%03d"; // Format du timer

    private int reservationTimeSelected = 0;
    private Button stopTrainingButton;
    private TextView timeRemainingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_remaining);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopTrainingButton = (Button) findViewById(R.id.stopTrainingButton);
        timeRemainingTextView = (TextView) findViewById(R.id.timeRemainingTextView);

        reservationTimeSelected = Integer.parseInt(getIntent().getStringExtra("newReservationTimeSelected")) * 60000; // Conversion en millis du temps de reservation;

        // Instance du timer de reservation
        new CountDownTimer(reservationTimeSelected, 10) {
            // Au démarrage
            public void onTick(long millisUntilFinished) {
                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - remainingMinutes * 60 * 1000);
                long remainingMillis = millisUntilFinished - remainingMinutes * 60 * 1000 - remainingSeconds * 1000;

                timeRemainingTextView.setText("" + String.format(FORMAT, remainingMinutes, remainingSeconds, remainingMillis));
            }

            public void onFinish() {
                timeRemainingTextView.setText("00:00:000");
                Toast toast = Toast.makeText(getApplicationContext(), "Entrainement terminé", Toast.LENGTH_LONG);
                toast.show();

                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                backToSignInActivity();
                            }
                        }, 4000);
            }
        }.start();

        stopTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSignInActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }


    /**
     * Cette methode renvoie vers l'activité SignInActivity
     */
    private void backToSignInActivity() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
