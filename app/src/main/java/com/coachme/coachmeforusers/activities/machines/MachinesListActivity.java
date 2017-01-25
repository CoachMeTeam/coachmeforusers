package com.coachme.coachmeforusers.activities.machines;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.utils.Helper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class MachinesListActivity extends Activity {
    private static final String TIMER_FORMAT = "%02d";

    private LinearLayout machinesContainer;
    private TextView machinesListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines_list);

        initComponents();

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
                                machinesListTextView.setText("Rafraichissement de la page dans " + String.format(TIMER_FORMAT, remainingSeconds));
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
                String machineAvailable = machine.isAvailable() ? "\nDisponible : Oui " : "\nDisponible : Non ";

                TextView textView = new TextView(this);
                textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(0, 0, 0, 15);
                textView.setText(machine.getMachineName() + machineAvailable);
                machinesContainer.addView(textView);
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

    private void initComponents() {
        machinesContainer = (LinearLayout) findViewById(R.id.machinesListContainer);
        machinesListTextView = (TextView) findViewById(R.id.machinesListTextView);
    }
}
