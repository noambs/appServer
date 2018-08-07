package com.barantech.noamb.appserver.services;


import com.barantech.noamb.appserver.screen.DeviceConnected;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //port number
    public static final int PORT = 11111;

    //Server Socket declaration
    private static ServerSocket serverSocket ;
    private DeviceConnected activity;

    public Server(DeviceConnected activity)
    {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        new Thread(socketServerThread).start();
    }

    public static void onDestroy()
    {
        if(serverSocket != null)
        {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread implements Runnable
    {

        public void run()
        {
            try{
                //create ServerSocket using specified port
                serverSocket = new ServerSocket(PORT);
                while(!Thread.currentThread().isInterrupted())
                {
                    Socket socket = serverSocket.accept();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    CommunicationThread commThread = new CommunicationThread(socket, activity);
                    new Thread(commThread).start();
                }
            }
            catch (IOException exp)
            {

            }
        }
    }



}
