package com.coachme.coachmeforusers.activities.reservations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.utils.Helper;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.util.concurrent.TimeUnit;

import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class ReservationTimeRemainingActivity extends Activity {
    private static final String TIMER_FORMAT = "%02d:%02d:%03d"; // Format du timer

    private int reservationTimeSelected = 0;
    private Button stopTrainingButton;
    private TextView timeRemainingTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_remaining);
        initComponents();

        /**
         * Conversion en millis du temps de reservation
         */
        reservationTimeSelected = Integer.parseInt(getIntent().getStringExtra("newReservationTimeSelected")) * 60000;
        countDownTimer = new CountDownTimer(reservationTimeSelected, 10) {
            public void onTick(long millisUntilFinished) {
                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - remainingMinutes * 60 * 1000);
                long remainingMillis = millisUntilFinished - remainingMinutes * 60 * 1000 - remainingSeconds * 1000;

                timeRemainingTextView.setText("" + String.format(TIMER_FORMAT, remainingMinutes, remainingSeconds, remainingMillis));
            }

            public void onFinish() {
                timeRemainingTextView.setText("00:00:000");
                sendAvailableMachineRequest();
                Toast.makeText(getApplicationContext(), "Entrainement terminé", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                goToAfterReservationMenuActivity();
                            }
                        }, 4000);
            }
        };
        countDownTimer.start();

        stopTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                sendAvailableMachineRequest();
                goToAfterReservationMenuActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void sendAvailableMachineRequest() {
        try {
            String stringMachine = Helper.getSharedPreference("currentMachine");
            Machine machine = Helper.convertJsonToObject(stringMachine, Machine.class);
            machine.setAvailable(true);
            String jsonMachine = Helper.convertObjectToJson(machine);

            ClientResource machineResource = new ClientResource(API_ENDPOINT + "/machines/" + machine.getId());
            machineResource.put(new JsonRepresentation(jsonMachine), MediaType.APPLICATION_JSON);

            Toast.makeText(getApplicationContext(),
                    "La réservation de la machine vient de se terminer.",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (ResourceException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors de la libération de la machine.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void goToAfterReservationMenuActivity() {
        Intent intent = new Intent(getApplicationContext(), AfterReservationMenuActivity.class);
        startActivity(intent);
    }

    private void initComponents() {
        stopTrainingButton = (Button) findViewById(R.id.stopTrainingButton);
        timeRemainingTextView = (TextView) findViewById(R.id.timeRemainingTextView);
    }
}
