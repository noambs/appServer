package com.barantech.noamb.appserver.services;



import com.barantech.noamb.appserver.screen.DeviceConnected;
import com.barantech.noamb.appserver.screen.DeviceControl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class CommunicationThread implements Runnable{

    public static final String FIELD_MAC= "mac";


    private Socket clientSocket;
    private BufferedWriter outputStream;
    private BufferedReader inputBuffer;
    private String macAddress;
    private String ipAddress;
    private String info;
    private int numberOfPress;
    private DeviceConnected activityDeviceConnected;
    private DeviceControl activityDeviceControl;
    private boolean lockStatus;
    private int ledColor;
    public CommunicationThread(Socket socket, DeviceConnected activity) {
        this.clientSocket = socket;
        this.activityDeviceConnected = activity;
        this.activityDeviceControl = DeviceControl.activity;
        numberOfPress = 0;
        if(clientSocket!=null)
        {

            /* get the information of the connected device*/
            try {
                BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] splitted = line.split(" +");
                    if (splitted != null && splitted.length >= 4)
                    {
                        ipAddress = splitted[0];
                        macAddress = splitted[3];
                        info = splitted[5];
                    }
                }



                try {
                    this.inputBuffer = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                    this.outputStream = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
                } catch (IOException exp) {
                    exp.printStackTrace();
                }

                numberOfPress = 0;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DeviceConnected.addDevice(this);

            activityDeviceConnected.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DeviceConnected.loadDevice();
                }
            });



        }


    }
    @Override
    public void run()
    {

        while(!Thread.currentThread().isInterrupted())
        {
            String read = "";

            try {
                read = inputBuffer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Identifies if a swith press occurred
            if(read!=null)
            {
                if(read.equals("*P#"))
                {
                    numberOfPress++;
                    activityDeviceControl.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DeviceControl.updateNumberOfPress(numberOfPress);
                        }
                    });
                }
            }

        }

    }

    /**
     * send data to the device/client
     * @param data
     */
    public void sendData(String data)
    {
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public BufferedWriter getOutputStream() {
        return outputStream;
    }

    public BufferedReader getInputBuffer() {
        return inputBuffer;
    }

    public Socket getClientSocket()
    {
        return clientSocket;
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
}
