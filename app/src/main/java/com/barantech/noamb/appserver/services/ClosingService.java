package com.barantech.noamb.appserver.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class ClosingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //HotSpot.onDestroy();
        Server.onDestroy();

        // Destroy the service
        stopSelf();
    }
}
