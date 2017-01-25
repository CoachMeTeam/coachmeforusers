package com.coachme.coachmeforusers.activities.reservations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.model.Reservation;
import com.coachme.coachmeforusers.utils.Helper;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.util.Date;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getCurrentUser;
import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class ReservationTimePickerActivity extends Activity {
    private int newReservationTimeSelected = 0;
    private Button startTrainingButton;
    private Button timePickerLessButton;
    private Button timePickerPlusButton;
    private TextView timePickerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        initComponents();
    }

    private void initComponents() {
        startTrainingButton = (Button) findViewById(R.id.startTrainingButton);
        timePickerLessButton = (Button) findViewById(R.id.timePickerLessButton);
        timePickerPlusButton = (Button) findViewById(R.id.timePickerPlusButton);
        timePickerTextView = (TextView) findViewById(R.id.timePickerTextView);

        timePickerTextView.setText("0");
        startTrainingButton.setEnabled(false);

        startTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientResource reservationResource = new ClientResource(API_ENDPOINT + "/reservations");
                String stringMachine = Helper.getSharedPreference("currentMachine");
                Machine machine = Helper.convertJsonToObject(stringMachine, Machine.class);
                String reservationDate = Helper.getStringDate(new Date());
                int duration = newReservationTimeSelected * 60;

                Reservation reservation = new Reservation(reservationDate, duration, machine.getId(), getCurrentUser().getId());
                String jsonReservation = Helper.convertObjectToJson(reservation);
                try {
                    reservationResource.post(new JsonRepresentation(jsonReservation), MediaType.APPLICATION_JSON);

                    Intent intent = new Intent(getApplicationContext(), ReservationTimeRemainingActivity.class);
                    intent.putExtra("newReservationTimeSelected", Integer.toString(newReservationTimeSelected));
                    startActivity(intent);
                } catch (ResourceException e) {
                    Toast.makeText(getApplicationContext(),
                            "Une erreur est survenue lors du choix de temps d'entrainement.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        timePickerLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentReservationTimeSelected = Integer.parseInt(timePickerTextView.getText().toString());
                newReservationTimeSelected = (currentReservationTimeSelected == 0 ? 0 : currentReservationTimeSelected - 1);
                timePickerTextView.setText(Integer.toString(newReservationTimeSelected));
                setStartTrainingButtonAvailability();
            }
        });

        timePickerPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentReservationTimeSelected = Integer.parseInt(timePickerTextView.getText().toString());
                newReservationTimeSelected = currentReservationTimeSelected + 1;
                timePickerTextView.setText(Integer.toString(newReservationTimeSelected));
                setStartTrainingButtonAvailability();
            }
        });
    }

    private void setStartTrainingButtonAvailability() {
        if (newReservationTimeSelected == 0) {
            startTrainingButton.setEnabled(false);
        } else if (newReservationTimeSelected > 0) {
            startTrainingButton.setEnabled(true);
        }
    }
}
