package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coachme.coachmeforusers.R;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTrainingButton = (Button) findViewById(R.id.startTrainingButton);
        timePickerLessButton = (Button) findViewById(R.id.timePickerLessButton);
        timePickerPlusButton = (Button) findViewById(R.id.timePickerPlusButton);
        timePickerTextView = (TextView) findViewById(R.id.timePickerTextView);

        timePickerTextView.setText("0");
        startTrainingButton.setEnabled(false);

        startTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationTimeRemainingActivity.class);
                intent.putExtra("newReservationTimeSelected", Integer.toString(newReservationTimeSelected));
                startActivity(intent);
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
                newReservationTimeSelected = currentReservationTimeSelected + 1; // A voir s'il ne faut pas limiter le temps maximum de reservation
                timePickerTextView.setText(Integer.toString(newReservationTimeSelected));
                setStartTrainingButtonAvailability();
            }
        });
    }

    /**
     * Cette methode active ou désactive le bouton permettant le démarrage de l'entrainement suivant le temps d'entrainement choisi
     * Si le temps d'entrainement est égal à 0 alors le bouton est désactivé
     * Sinon si le temps d'entrainement est supérieur à O alors le bouton est activé
     */
    private void setStartTrainingButtonAvailability() {
        if (newReservationTimeSelected == 0) {
            startTrainingButton.setEnabled(false);
        } else if (newReservationTimeSelected > 0) {
            startTrainingButton.setEnabled(true);
        }
    }
}
