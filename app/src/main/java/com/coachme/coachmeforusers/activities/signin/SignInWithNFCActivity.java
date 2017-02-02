package com.coachme.coachmeforusers.activities.signin;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.model.User;
import com.coachme.coachmeforusers.service.nfc.NFCCardReader;
import com.coachme.coachmeforusers.utils.Helper;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

import static com.coachme.coachmeforusers.CoachMeForUsersApp.setCurrentUser;
import static com.coachme.coachmeforusers.utils.Helper.API_ENDPOINT;

public class SignInWithNFCActivity extends Activity {
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    private NfcAdapter nfcAdapter;
    public NFCCardReader mNFCCardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "Cet appareil ne supporte pas le NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mNFCCardReader = new NFCCardReader();
        enableNFCReaderMode();

        ClientResource usersResource = new ClientResource(API_ENDPOINT + "/users/1");
        try {
            Representation representation = usersResource.get(MediaType.APPLICATION_JSON);
            User user = Helper.convertJsonToObject(representation.getText(), User.class);
            setCurrentUser(user);
        } catch (ResourceException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors du chargement des données utilsateurs.",
                    Toast.LENGTH_LONG)
                    .show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Une erreur est survenue lors du chargement des données utilsateurs.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(Helper.getSharedPreference("currentMachine"));
    }

    @Override
    public void onPause() {
        super.onPause();
        disableNFCReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableNFCReaderMode();
    }

    private void enableNFCReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, mNFCCardReader, READER_FLAGS, null);
        }
    }

    private void disableNFCReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }
}