package com.coachme.coachmeforusers.activities.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.activities.reservations.ReservationTimePickerActivity;
import com.coachme.coachmeforusers.model.User;
import com.coachme.coachmeforusers.utils.Helper;

import org.apache.commons.lang3.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.getContext;
import static com.coachme.coachmeforusers.CoachMeForUsersApp.setCurrentUser;
import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class SignInWithCredentials extends Activity {
    private final String FIELD_REQUIRED = "Ce champ est requis.";

    private EditText credentialsEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_credentials);

        initComponents();
    }

    private void initComponents() {
        credentialsEditText = (EditText) findViewById(R.id.credentialsEditText);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfFormValid()) {
                    ClientResource userResource = new ClientResource(API_ENDPOINT + "/users/" + credentialsEditText.getText());
                    try {
                        Representation representation = userResource.get(MediaType.APPLICATION_JSON);
                        User user = Helper.convertJsonToObject(representation.getText(), User.class);
                        if (!user.isLoggedOnAMachine()) {
                            user.setLoggedOnAMachine(true);
                            String jsonUser = Helper.convertObjectToJson(user);
                            userResource.put(new JsonRepresentation(jsonUser), MediaType.APPLICATION_JSON);
                            setCurrentUser(user);

                            Intent intent = new Intent(getContext(), ReservationTimePickerActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(),
                                    "Vous ne pouvez pas vous connecter sur plusieurs machines à la fois. Veuillez-vous déconnecter de la précédente machine sur laquelle vous étiez.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (ResourceException e) {
                        if (e.getStatus().getCode() == Status.CLIENT_ERROR_NOT_FOUND.getCode()) {
                            Toast.makeText(getContext(),
                                    "Aucun adhérent n'est associé avec l'identifiant saisi.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Une erreur est survenue lors la connexion utilisateur sur la machine.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getContext(),
                                "Une erreur est survenue lors la connexion utilisateur sur la machine.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });
    }

    private boolean checkIfFormValid() {
        boolean isFormValid = true;
        if (StringUtils.isBlank(credentialsEditText.getText().toString())) {
            isFormValid = false;
            credentialsEditText.setError(FIELD_REQUIRED);
        }
        return isFormValid;
    }
}
