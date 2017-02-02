package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.Machine;
import com.coachme.coachmeforusers.utils.Helper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.List;

import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class ChooseMachineActivity extends Activity {
    private LinearLayout buttonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_machine);

        initComponents();

        ClientResource machinesResource = new ClientResource(API_ENDPOINT + "/machines");
        try {
            Representation representation = machinesResource.get(MediaType.APPLICATION_JSON);
            List<Machine> machines = Helper.convertJsonToListOfObjects(representation.getText(), new TypeReference<List<Machine>>() {
            });
            for (final Machine machine : machines) {
                if (!machine.isUsedByATablet()) {
                    Button button = new Button(this);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    button.setLayoutParams(layoutParams);
                    button.setBackgroundResource(R.drawable.buttonshape);
                    button.setTextColor(Color.WHITE);
                    button.setText(machine.getMachineName());
                    button.setGravity(Gravity.CENTER);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                ClientResource machineResource = new ClientResource(API_ENDPOINT + "/machines/" + machine.getId());

                                machine.setUsedByATablet(true);
                                String jsonMachine = Helper.convertObjectToJson(machine);
                                machineResource.put(new JsonRepresentation(jsonMachine), MediaType.APPLICATION_JSON);
                                Helper.storeSharedPreference("currentMachine", jsonMachine);

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } catch (ResourceException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Une erreur est survenue lors du choix de la machine.",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });

                    buttonsContainer.addView(button);
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors de l'obtention de la liste des machines disponibles.",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (ResourceException ex) {
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
        buttonsContainer = (LinearLayout) findViewById(R.id.machineButtonsContainer);
    }
}
