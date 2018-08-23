package com.barantech.noamb.appserver.screen;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.AsyncDialog;
import com.barantech.noamb.appserver.services.CommunicationThread;
import com.barantech.noamb.appserver.services.Connection;
import com.barantech.noamb.appserver.services.SendData;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class DeviceControl extends AppCompatActivity {
    private ImageView lock;
    private int ledsStatus;
    private ImageView redLed;
    private ImageView blueLed;
    private ImageView greenLed;
    private static CommunicationThread device;
    public static TextView numberOfPress;

    private boolean isReachable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        Intent intent = getIntent();
        isReachable = false;
        String deviceMacAddress = (String) intent.getStringExtra(CommunicationThread.FIELD_MAC);
        device = DeviceConnected.deviceList.get(deviceMacAddress);
        device.setActivityDeviceControl(this);
        toolbar.setTitle(device.getMacAddress());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lock = (ImageView) findViewById(R.id.lock);
        redLed = (ImageView) findViewById(R.id.red_led);
        greenLed = (ImageView) findViewById(R.id.green_led);
        blueLed = (ImageView) findViewById(R.id.blue_led);
        ledsStatus = device.getLedColor();

        try {
            isReachable = new Connection(device).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if(isReachable)
        {

            /* lock status in the beginning*/
            if(device.getLockStatus())
            {

                //device.sendData("*CLOSE#");
                new SendData(device).execute("*CLOSE#");
                lock.setImageResource(R.drawable.close_lock);
                lock.setTag(R.drawable.close_lock);

            }
            else
            {
                //device.sendData("*OPEN#");
                new SendData(device).execute("*OPEN#");
                lock.setImageResource(R.drawable.open_lock);
                lock.setTag(R.drawable.open_lock);
            }



            /* led status in the beginning*/
            if(ledsStatus == 0)
            {
                //device.sendData("*"+0+"#");
                new SendData(device).execute("*"+0+"#");
                redLed.setImageResource(R.mipmap.red_led_off);
                redLed.setTag(R.mipmap.red_led_off);
                greenLed.setImageResource(R.mipmap.green_led_off);
                greenLed.setTag(R.mipmap.green_led_off);
                blueLed.setImageResource(R.mipmap.blue_led_off);
                blueLed.setTag(R.mipmap.blue_led_off);
            }else{
                if(ledsStatus == 1)
                {
                    //device.sendData("*"+0+";"+(char)2+"#");
                    new SendData(device).execute("*"+0+";"+(char)2+"#");
                    redLed.setImageResource(R.mipmap.red_led_on);
                    redLed.setTag(R.mipmap.red_led_on);
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                }else{
                    if(ledsStatus == 2)
                    {
                        //device.sendData("*"+0+";"+(char)4+"#");
                        new SendData(device).execute("*"+0+";"+(char)4+"#");
                        redLed.setImageResource(R.mipmap.red_led_off);
                        redLed.setTag(R.mipmap.red_led_off);
                        greenLed.setImageResource(R.mipmap.green_led_on);
                        greenLed.setTag(R.mipmap.green_led_on);
                        blueLed.setImageResource(R.mipmap.blue_led_off);
                        blueLed.setTag(R.mipmap.blue_led_off);
                    }else{
                        //device.sendData("*"+0+";"+(char)8+"#");
                        new SendData(device).execute("*"+0+";"+(char)8+"#");
                        redLed.setImageResource(R.mipmap.red_led_off);
                        redLed.setTag(R.mipmap.red_led_off);
                        greenLed.setImageResource(R.mipmap.green_led_off);
                        greenLed.setTag(R.mipmap.green_led_off);
                        blueLed.setImageResource(R.mipmap.blue_led_on);
                        blueLed.setTag(R.mipmap.blue_led_on);
                    }
                }
            }
        }
        else
        {
            disconnectHandler();
        }




        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isReachable = new Connection(device).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(isReachable)
                {
                    int tag = (int) lock.getTag();
                    if(tag == R.drawable.open_lock)
                    {
                        //device.sendData("*CLOSE#");
                        new SendData(device).execute("*CLOSE#");
                        lock.setImageResource(R.drawable.close_lock);
                        lock.setTag(R.drawable.close_lock);
                        device.setLockStatus(true);
                    }
                    else
                    {
                        //device.sendData("*OPEN#");
                        new SendData(device).execute("*OPEN#");
                        lock.setImageResource(R.drawable.open_lock);
                        lock.setTag(R.drawable.open_lock);
                        device.setLockStatus(false);

                    }
                } else{
                    disconnectHandler();
                }

            }
        });




        redLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isReachable = new Connection(device).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(isReachable)
                {
                    int tag = (int) redLed.getTag();
                    if(device.getLockStatus())
                    {
                        //redLed.setImageResource(R.mipmap.red_led_on);
                        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator());

                        animation.setRepeatCount(4);

                        animation.setRepeatMode(Animation.REVERSE);

                        redLed.startAnimation(animation);
                        if(tag == R.mipmap.red_led_off) redLed.setImageResource(R.mipmap.red_led_off);
                        else redLed.setImageResource(R.mipmap.red_led_on);
                    }
                    else{
                        if(tag == R.mipmap.red_led_off)
                        {
                            //device.sendData("*"+0+";"+2+"#");
                            new SendData(device).execute("*"+0+";"+2+"#");
                            redLed.setImageResource(R.mipmap.red_led_on);
                            redLed.setTag(R.mipmap.red_led_on);
                            greenLed.setImageResource(R.mipmap.green_led_off);
                            greenLed.setTag(R.mipmap.green_led_off);
                            blueLed.setImageResource(R.mipmap.blue_led_off);
                            blueLed.setTag(R.mipmap.blue_led_off);
                            device.setLedColor(1);
                        }
                        else
                        {
                            //device.sendData("*"+0+"#");
                            new SendData(device).execute("*"+0+"#");
                            redLed.setImageResource(R.mipmap.red_led_off);
                            redLed.setTag(R.mipmap.red_led_off);
                            device.setLedColor(0);
                        }
                    }
                } else{
                    disconnectHandler();
                }





            }
        });


        greenLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isReachable = new Connection(device).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(isReachable)
                {
                    int tag = (int) greenLed.getTag();
                    if(device.getLockStatus())
                    {

                        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator());

                        animation.setRepeatCount(4);

                        animation.setRepeatMode(Animation.REVERSE);

                        greenLed.startAnimation(animation);
                        if(tag == R.mipmap.green_led_off) greenLed.setImageResource(R.mipmap.green_led_off);
                        else greenLed.setImageResource(R.mipmap.green_led_on);
                    }
                    else{
                        if(tag == R.mipmap.green_led_off)
                        {
                            //device.sendData("*"+0+";"+4+"#");
                            new SendData(device).execute("*"+0+";"+4+"#");
                            redLed.setImageResource(R.mipmap.red_led_off);
                            redLed.setTag(R.mipmap.red_led_off);
                            greenLed.setImageResource(R.mipmap.green_led_on);
                            greenLed.setTag(R.mipmap.green_led_on);
                            blueLed.setImageResource(R.mipmap.blue_led_off);
                            blueLed.setTag(R.mipmap.blue_led_off);
                            device.setLedColor(2);
                        }
                        else
                        {
                            //device.sendData("*"+0+"#");
                            new SendData(device).execute("*"+0+"#");
                            greenLed.setImageResource(R.mipmap.green_led_off);
                            greenLed.setTag(R.mipmap.green_led_off);
                            device.setLedColor(0);
                        }
                    }
                } else{
                    disconnectHandler();
                }



            }
        });

        blueLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isReachable = new Connection(device).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(isReachable)
                {
                    int tag = (int) blueLed.getTag();
                    if(device.getLockStatus())
                    {

                        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator());

                        animation.setRepeatCount(4);

                        animation.setRepeatMode(Animation.REVERSE);

                        blueLed.startAnimation(animation);
                        if(tag == R.mipmap.blue_led_off) blueLed.setImageResource(R.mipmap.blue_led_off);
                        else blueLed.setImageResource(R.mipmap.blue_led_on);
                    }
                    else{
                        if(tag == R.mipmap.blue_led_off)
                        {
                            //device.sendData("*"+0+";"+8+"#");
                            new SendData(device).execute("*"+0+";"+8+"#");
                            redLed.setImageResource(R.mipmap.red_led_off);
                            redLed.setTag(R.mipmap.red_led_off);
                            greenLed.setImageResource(R.mipmap.green_led_off);
                            greenLed.setTag(R.mipmap.green_led_off);
                            blueLed.setImageResource(R.mipmap.blue_led_on);
                            blueLed.setTag(R.mipmap.blue_led_on);
                            device.setLedColor(3);
                        }
                        else
                        {
                            //device.sendData("*"+0+"#");
                            new SendData(device).execute("*"+0+"#");
                            blueLed.setImageResource(R.mipmap.blue_led_off);
                            blueLed.setTag(R.mipmap.blue_led_off);
                            device.setLedColor(0);
                        }
                    }
                } else{
                    disconnectHandler();
                }



            }
        });


        numberOfPress = (TextView) findViewById(R.id.press_counter);
        updateNumberOfPress(device.getNumberOfPress(), device.getMacAddress());
        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isReachable = new Connection(device).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(isReachable)
                {
                    //device.sendData("*OPEN#");
                    new SendData(device).execute("*OPEN#");
                    lock.setImageResource(R.drawable.open_lock);
                    lock.setTag(R.drawable.open_lock);

                    //device.sendData("*"+0+"#");
                    new SendData(device).execute("*"+0+"#");
                    redLed.setImageResource(R.mipmap.red_led_off);
                    redLed.setTag(R.mipmap.red_led_off);
                    greenLed.setImageResource(R.mipmap.green_led_off);
                    greenLed.setTag(R.mipmap.green_led_off);
                    blueLed.setImageResource(R.mipmap.blue_led_off);
                    blueLed.setTag(R.mipmap.blue_led_off);
                    updateNumberOfPress(0, device.getMacAddress());
                    device.setNumberOfPress(0);
                } else{
                    disconnectHandler();
                }


            }
        });




    }

    public static void updateNumberOfPress(int num, String mac)
    {
        if(device.getMacAddress().equals(mac))
            numberOfPress.setText(String.valueOf(num));
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }


    private void disconnectHandler()
    {
        final AsyncDialog builder = new AsyncDialog(this);
        final Timer timer;// Declare it above


        timer = new Timer();//Initialized
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                // cancel the progress dialogue after 5 seconds
                //builder.cancel(true);
                timer.cancel();
              // finish();

            }
        }, 5000 ,5000);
        builder.execute();
        DeviceConnected.removeDevice(device);
        try {
            device.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }




}
