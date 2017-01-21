package com.coachme.coachmeforusers.service.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import com.coachme.coachmeforusers.activities.reservations.ReservationTimePickerActivity;

import java.io.IOException;

public class NFCCardReader implements NfcAdapter.ReaderCallback {
    private Context context;

    public NFCCardReader(Context c) {
        context = c;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        readTag(tag);
    }

    public void readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        if (mifare != null) {
            Intent intent = new Intent(context, ReservationTimePickerActivity.class);
            context.startActivity(intent);
            try {
                mifare.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
