package com.barantech.noamb.appserver.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.CommunicationThread;

public class DeviceControl extends AppCompatActivity {
    private ImageView lock;
    private int ledsStatus;
    private ImageView redLed;
    private ImageView blueLed;
    private ImageView greenLed;
    private CommunicationThread device;
    public static DeviceControl activity;
    public static TextView numberOfPress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        Intent intent = getIntent();

        String deviceMacAddress = (String) intent.getStringExtra(CommunicationThread.FIELD_MAC);
        device = DeviceConnected.deviceList.get(deviceMacAddress);

        lock = (ImageView) findViewById(R.id.lock);
        device.setLockStatus(false);
        if(device.getLockStatus())
            lock.setTag(R.drawable.close_lock);
        else
            lock.setTag(R.drawable.open_lock);

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) lock.getTag();
                if(tag == R.drawable.close_lock)
                {
                    device.sendData("*OPEN#");
                    lock.setImageResource(R.drawable.open_lock);
                    lock.setTag(R.drawable.open_lock);
                }
                else
                {
                    device.sendData("*CLOSE#");
                    lock.setImageResource(R.drawable.close_lock);
                    lock.setTag(R.drawable.close_lock);
                }
            }
        });

        redLed = (ImageView) findViewById(R.id.red_led);
        greenLed = (ImageView) findViewById(R.id.green_led);
        blueLed = (ImageView) findViewById(R.id.blue_led);

        device.setLedColor(0);
        ledsStatus = device.getLedColor();

        if(ledsStatus == 0)
        {
            device.sendData("*"+0+"#");
            redLed.setImageResource(R.mipmap.red_led_off);
            redLed.setTag(R.mipmap.red_led_off);
            greenLed.setImageResource(R.mipmap.green_led_off);
            greenLed.setTag(R.mipmap.green_led_off);
            blueLed.setImageResource(R.mipmap.blue_led_off);
            blueLed.setTag(R.mipmap.blue_led_off);
        }else{
            if(ledsStatus == 1)
            {
                device.sendData("*"+0+";"+(char)2+"#");
                redLed.setImageResource(R.mipmap.red_led_on);
                redLed.setTag(R.mipmap.red_led_on);
                greenLed.setImageResource(R.mipmap.green_led_off);
                greenLed.setTag(R.mipmap.green_led_off);
                blueLed.setImageResource(R.mipmap.blue_led_off);
                blueLed.setTag(R.mipmap.blue_led_off);
            }else{
                if(ledsStatus == 2)
                {
                    device.sendData("*"+0+";"+(char)4+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                    greenLed.setImageResource(R.mipmap.green_led_on);
                    greenLed.setTag(R.mipmap.green_led_on);
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                }else{
                    device.sendData("*"+0+";"+(char)8+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                    blueLed.setImageResource(R.mipmap.blue_led_on);
                    blueLed.setTag(R.mipmap.blue_led_on);
                }
            }
        }

        redLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) redLed.getTag();
                if(tag == R.mipmap.red_led_off)
                {
                    device.sendData("*"+0+";"+(char)2+"#");
                    redLed.setImageResource(R.mipmap.red_led_on);
                    redLed.setTag(R.mipmap.red_led_on);
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                }
                else
                {
                    device.sendData("*"+0+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                }
            }
        });


        greenLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) greenLed.getTag();
                if(tag == R.mipmap.green_led_off)
                {
                    device.sendData("*"+0+";"+(char)4+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                    greenLed.setImageResource(R.mipmap.green_led_on);
                    greenLed.setTag(R.mipmap.green_led_on);
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                }
                else
                {
                    device.sendData("*"+0+"#");
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                }
            }
        });

        blueLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) blueLed.getTag();
                if(tag == R.mipmap.blue_led_off)
                {
                    device.sendData("*"+0+";"+(char)8+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                    blueLed.setImageResource(R.mipmap.blue_led_on);
                    blueLed.setTag(R.mipmap.blue_led_on);
                }
                else
                {
                    device.sendData("*"+0+"#");
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                }
            }
        });


        numberOfPress = (TextView) findViewById(R.id.press_counter);

        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                device.sendData("*OPEN#");
                lock.setImageResource(R.drawable.open_lock);
                lock.setTag(R.drawable.open_lock);

                device.sendData("*"+0+"#");
                redLed.setImageResource(R.mipmap.red_led_off);
                redLed.setTag(R.mipmap.red_led_off);
                greenLed.setImageResource(R.mipmap.green_led_off);
                greenLed.setTag(R.mipmap.green_led_off);
                blueLed.setImageResource(R.mipmap.blue_led_off);
                blueLed.setTag(R.mipmap.blue_led_off);
                updateNumberOfPress(0);
            }
        });



        activity = this;
    }

    public static void updateNumberOfPress(int num)
    {
        numberOfPress.setText(String.valueOf(num));
    }
}
