package com.barantech.noamb.appserver.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barantech.noamb.appserver.R;


public class ConfigHotSpotScreen extends AppCompatActivity {
    private EditText ssid;
    private EditText password;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_hot_spot_screen);

        ssid = findViewById(R.id.SSID);
        password = findViewById(R.id.password);
        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
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
                    Intent clientScreenActivity = new Intent(getApplicationContext(),ClientControlScreen.class);
                    clientScreenActivity.putExtra("SSID", string_ssid);
                    clientScreenActivity.putExtra("password",string_password);
                    ConfigHotSpotScreen.this.startActivity(clientScreenActivity);
                }

            }
        });
    }
}
