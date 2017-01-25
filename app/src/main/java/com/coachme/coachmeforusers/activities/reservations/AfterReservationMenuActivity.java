package com.coachme.coachmeforusers.activities.reservations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.activities.HomeActivity;
import com.coachme.coachmeforusers.activities.machines.MachinesListActivity;
import com.coachme.coachmeforusers.model.User;
import com.coachme.coachmeforusers.utils.Helper;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getContext;
import static com.coachme.coachmeforusers.CoachMeForUsersApp.getCurrentUser;
import static com.coachme.coachmeforusers.CoachMeForUsersApp.setCurrentUser;
import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class AfterReservationMenuActivity extends Activity {
    private Button goToMachinesListButton;
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_reservation_menu);

        initComponents();
    }

    @Override
    public void onBackPressed() {
    }

    private void initComponents() {
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
