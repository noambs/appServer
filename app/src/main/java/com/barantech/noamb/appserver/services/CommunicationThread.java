package com.barantech.noamb.appserver.services;



import android.widget.Toast;

import com.barantech.noamb.appserver.screen.DeviceConnected;
import com.barantech.noamb.appserver.screen.DeviceControl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;


public class CommunicationThread extends Thread{

    public static final String FIELD_MAC= "mac";


    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputBuffer;
    private String macAddress;
    private String ipAddress;
    private String info;
    private int numberOfPress;
    private DeviceConnected activityDeviceConnected;
    private DeviceControl activityDeviceControl;
    private boolean lockStatus;
    private int ledColor;
    private String read;
    private boolean isConnected;
    public CommunicationThread(Socket socket, DeviceConnected activity) {
        this.clientSocket = socket;
        this.activityDeviceConnected = activity;

        numberOfPress = 0;
        boolean isReachable = false;

        if(clientSocket!=null)
        {

            try {
                this.inputBuffer = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                //this.outputStream = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
                this.outputStream  = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream())),

                true);

            } catch (IOException exp) {
                exp.printStackTrace();
            }
            macAddress = "";
            ipAddress = clientSocket.getInetAddress().toString().substring(1);
            setLedColor(0);
            lockStatus = false;



        }


    }
    @Override
    public void run()
    {
        while(true)
        {


            read = "";
            if(macAddress.equals(""))
            {
                sendData("*mac#");

            }
            try {
                if(inputBuffer.ready())
                {

                    read = inputBuffer.readLine();
                    if(read!=null)
                    {
                        if(read.contains("*P#") && !lockStatus)
                        {
                            numberOfPress++;
                            if(activityDeviceControl!=null)
                            {
                                activityDeviceControl.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DeviceControl.updateNumberOfPress(numberOfPress,macAddress);
                                    }
                                });
                            }

                        }

                        if(read.contains("mac"))
                        {
                            macAddress = read.substring(3);
                            sendData("*ok#");
                            DeviceConnected.addDevice(this);
                            isConnected = true;
                            activityDeviceConnected.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    DeviceConnected.loadDevice();
                                }
                            });
                        }
                    }
                }else{
                     try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }


                /*activityDeviceConnected.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activityDeviceConnected, read, Toast.LENGTH_SHORT).show();
                    }
                });*/

            } catch (IOException e) {
                e.printStackTrace();

            }





        }


    }

    /**
     * send data to the device/client
     * @param data
     */
    public void sendData(String data)
    {
        outputStream.println(data);

    }

    /* getter */
    public String getMacAddress() {
        return macAddress;
    }

    public int getNumberOfPress() {
        return numberOfPress;
    }

    public String getIpAddress() {
         return ipAddress;
    }

    public  String getInfo() {
        return info;
    }

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public BufferedReader getInputBuffer() {
        return inputBuffer;
    }

    public Socket getClientSocket()
    {
        return clientSocket;
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     *
     * @return true if lock and false if unlock
     */
    public boolean getLockStatus()
    {
        return lockStatus;
    }

    /**
     *
     * @return 0-all leds off , 1-red led on, 2-green led on, 3-blue led on
     */
    public int getLedColor()
    {
        return ledColor;
    }
    /*setter*/
    public void setLockStatus(boolean lock)
    {
        lockStatus = lock;
    }

    public void setLedColor(int color)
    {
        ledColor = color;
    }

    public void setNumberOfPress(int num)
    {
        numberOfPress = num;
    }
    public void setActivityDeviceControl(DeviceControl activityDeviceControl) {
        this.activityDeviceControl = activityDeviceControl;
    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
         public void run() {
            Toast toast = Toast.makeText(activityDeviceConnected, this.msg, Toast.LENGTH_LONG);
            toast.show();

        }
    }

}
