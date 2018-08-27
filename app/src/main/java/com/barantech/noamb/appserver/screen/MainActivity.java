package com.barantech.noamb.appserver.screen;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.barantech.noamb.appserver.R;


public class MainActivity extends AppCompatActivity {

    private Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configHotSpotScreenActivity = new Intent(getApplicationContext(),ConfigHotSpot.class);
                MainActivity.this.startActivity(configHotSpotScreenActivity);
            }
        });


    }


}
