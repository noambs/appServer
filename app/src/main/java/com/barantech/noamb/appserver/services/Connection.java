package com.barantech.noamb.appserver.services;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetAddress;

public class Connection extends AsyncTask<Void, Void, Boolean> {
    private CommunicationThread device;
    public Connection(CommunicationThread mDevice)
    {
        device = mDevice;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean isReachable = false;
        if(device!=null && device.isConnected() && device.getClientSocket()!=null)
        {
            try {
                InetAddress hostAddress = device.getClientSocket().getInetAddress();
                if(hostAddress!=null)
                    isReachable = hostAddress.isReachable(500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isReachable;
    }


    @Override
    protected void onPostExecute(Boolean isReachable) {
        super.onPostExecute(isReachable);

    }
}
