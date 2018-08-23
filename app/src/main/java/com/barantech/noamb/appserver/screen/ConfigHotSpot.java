package com.barantech.noamb.appserver.screen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.HotSpot;

import java.lang.reflect.InvocationTargetException;


public class ConfigHotSpot extends AppCompatActivity {

    private EditText ssid;
    private EditText password;
    private Button mButton;
    public static HotSpot hotSpot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_hot_spot);


        ssid = findViewById(R.id.SSID);
        password = findViewById(R.id.password);
        ssid.setText("RBS1");
        password.setText("12345678");
        if(hotSpot == null)
            hotSpot = new HotSpot(ConfigHotSpot.this);
        mButton = findViewById(R.id.button1);
       /* if(HotSpot.wifiManager!=null)
        {
            hotSpot.onDestroy();
        }*/
        mButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String string_ssid = ssid.getText().toString();
                String string_password = password.getText().toString();
                if(string_ssid.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter SSID Name!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(hotSpot.getApState()==13)
                    {
                       /* try {
                            HotSpot.enableWifi.invoke(HotSpot.wifiManager, null, false);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "The App Adaptering it options...", Toast.LENGTH_LONG).show();


                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                        Intent clientScreenActivity = new Intent(getApplicationContext(),DeviceConnected.class);
                        clientScreenActivity.putExtra("SSID", string_ssid);
                        clientScreenActivity.putExtra("password",string_password);
                        ConfigHotSpot.this.startActivity(clientScreenActivity);
                    }
                   /* if(hotSpot.setHotSpot(string_ssid, string_password))
                    {

                        Intent clientScreenActivity = new Intent(getApplicationContext(),DeviceConnected.class);
                        clientScreenActivity.putExtra("SSID", string_ssid);
                        clientScreenActivity.putExtra("password",string_password);
                        ConfigHotSpot.this.startActivity(clientScreenActivity);
                    }*/

                }

            }
        });
    }


}
