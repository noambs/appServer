package com.barantech.noamb.appserver.services;


import com.barantech.noamb.appserver.screen.DeviceConnected;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    //port number
    public static final int PORT = 11111;

    //Server Socket declaration
    public static ServerSocket serverSocket ;
    private DeviceConnected activity;

    public Server(DeviceConnected activity)
    {
        this.activity = activity;
        //SocketServerThread socketServerThread = new SocketServerThread();
        //socketServerThread.start();
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

    public void run()
    {
        try{
            //create ServerSocket using specified port
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            //while(!Thread.currentThread().isInterrupted())
            while(true)
            {
                if(!serverSocket.isClosed() && serverSocket!=null)
                {
                    Socket socket = serverSocket.accept();
                    if(socket!=null)
                    {
                        CommunicationThread commThread = new CommunicationThread(socket, activity);
                        commThread.start();
                    }
                }


            }
        }
        catch (IOException exp)
        {
            exp.printStackTrace();
        }
    }



}
