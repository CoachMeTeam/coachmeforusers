package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import com.coachme.coachmeforusers.R;
import com.coachme.coachmeforusers.service.nfc.NFCCardReader;

public class SignInActivity extends Activity {
    public static final String TAG = "SignInActivity";
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

        mNFCCardReader = new NFCCardReader(this);
        enableNFCReaderMode();
    }

    @Override
    protected void onStart() {
        super.onStart();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
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