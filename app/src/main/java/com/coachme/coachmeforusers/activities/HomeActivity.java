package com.coachme.coachmeforusers.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.coachme.coachmeforusers.R;

import java.util.List;

public class HomeActivity extends Activity {
    private ImageButton coachMeLogoImageButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);

        /*String networkSSID = "CoachMeWifi";
        String networkPass = "coachmewifi";
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   //ssid must be in quotes
        conf.wepKeys[0] = "\"" + networkSSID + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.preSharedKey = "\"" + networkPass + "\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        coachMeLogoImageButton = (ImageButton) findViewById(R.id.imageButton);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        coachMeLogoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}
