package com.barantech.noamb.appserver.services;

import android.os.AsyncTask;
public class SendData extends AsyncTask<String, Void, Void> {

    private CommunicationThread device;

    public SendData(CommunicationThread mDevice)
    {
        device = mDevice;
    }


    @Override
    protected Void doInBackground(String... params) {
        String data = params[0];
        device.getOutputStream().println(data);
        return null;
    }
}

