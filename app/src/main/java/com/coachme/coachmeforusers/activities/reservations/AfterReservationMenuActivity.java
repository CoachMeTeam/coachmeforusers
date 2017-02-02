package com.coachme.coachmeforusers.activities.reservations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.activities.HomeActivity;
import com.coachme.coachmeforusers.activities.machines.MachinesListActivity;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.model.User;
import com.coachme.coachmeforusers.utils.Helper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getContext;
import static com.coachme.coachmeforusers.CoachMeForUsersApp.getCurrentUser;
import static com.coachme.coachmeforusers.CoachMeForUsersApp.setCurrentUser;
import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class AfterReservationMenuActivity extends Activity {
    private static final String TIMER_FORMAT = "%02d";

    private Button goToMachinesListButton;
    private Button logOutButton;
    private LinearLayout machinesContainer;
    private TextView machinesByTypesTextView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_reservation_menu);

        initComponents();

        user = getCurrentUser();
        Timer updateLayoutTimer = new Timer();
        updateLayoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        CountDownTimer countDownTimer = new CountDownTimer(31000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - remainingMinutes * 60 * 1000);
                                machinesByTypesTextView.setText("Rafraichissement de la page dans " + String.format(TIMER_FORMAT, remainingSeconds));
                            }

                            public void onFinish() {
                            }
                        };
                        countDownTimer.start();
                        initMachinesListLayout();
                    }
                });
            }
        }, 0, 31000);
    }

    private void initMachinesListLayout() {
        machinesContainer.removeAllViews();

        ClientResource machinesResource = new ClientResource(API_ENDPOINT + "/machines");
        try {
            Representation representation = machinesResource.get(MediaType.APPLICATION_JSON);
            List<Machine> machines = Helper.convertJsonToListOfObjects(representation.getText(), new TypeReference<List<Machine>>() {
            });

            for (Machine machine : machines) {
                if (machine.getMachineType().equals(user.getGoal())) {
                    String machineAvailable = machine.isAvailable() ? "\nDisponible : Oui " : "\nDisponible : Non ";

                    TextView textView = new TextView(this);
                    textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);
                    textView.setPadding(0, 0, 0, 15);
                    textView.setText(machine.getMachineName() + machineAvailable);
                    machinesContainer.addView(textView);
                }
            }
        } catch (ResourceException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors de l'obtention de la liste des machines disponibles.",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors de l'obtention de la liste des machines disponibles.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void initComponents() {
        machinesContainer = (LinearLayout) findViewById(R.id.machinesByTypesContainer);
        machinesByTypesTextView = (TextView) findViewById(R.id.machinesByTypesTextView);
        goToMachinesListButton = (Button) findViewById(R.id.goToMachinesListButton);
        logOutButton = (Button) findViewById(R.id.logOutButton);

        goToMachinesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MachinesListActivity.class);
                startActivity(intent);
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User user = getCurrentUser();
                    user.setLoggedOnAMachine(false);
                    String jsonUser = Helper.convertObjectToJson(user);
                    ClientResource userResource = new ClientResource(API_ENDPOINT + "/users/" + user.getId());
                    userResource.put(new JsonRepresentation(jsonUser), MediaType.APPLICATION_JSON);

                    setCurrentUser(null);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (ResourceException e) {
                    Toast.makeText(getContext(),
                            "Une erreur est survenue lors la déconnexion.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}
