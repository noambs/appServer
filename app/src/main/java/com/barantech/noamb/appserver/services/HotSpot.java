package com.barantech.noamb.appserver.services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HotSpot {





    private static WifiManager wifiManager;
    private WifiConfiguration  myConfig;
    private static Method enableWifi;
    private boolean permission;
    private Context context;
    public HotSpot(Context mContext)
    {
        context = mContext;

    }

    public boolean setHotSpot(String SSID, String password)
    {
        boolean result = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permission = Settings.System.canWrite(context);
        } else{
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if(permission)
        {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            ConnectivityManager cman = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            Method[] methods = cman.getClass().getMethods();
            try
            {

                wifiManager.setWifiEnabled(false);
                enableWifi = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                String ssid  =   SSID;//your SSID
                String pass  =   password; // your Password
                myConfig =  new WifiConfiguration();
                myConfig.SSID = ssid;
                myConfig.preSharedKey  = pass ;
                myConfig.status = WifiConfiguration.Status.ENABLED;
                myConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                myConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                myConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                result = (boolean)enableWifi.invoke(wifiManager, myConfig, true);

            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
        }else{
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            context.startActivity(intent);
        }

        return result;
    }

    public static void onDestroy()
    {
        try {
            enableWifi.invoke(wifiManager, null, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
