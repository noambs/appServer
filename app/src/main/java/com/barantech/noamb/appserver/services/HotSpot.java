package com.barantech.noamb.appserver.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.barantech.noamb.appserver.screen.ConfigHotSpot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class HotSpot {





    public static WifiManager wifiManager;
    public static WifiConfiguration  myConfig;
    public static Method enableWifi;
    private boolean permission;
    private ConfigHotSpot context;
    private  int apState ;


    public HotSpot(ConfigHotSpot mContext)
    {
        context = mContext;
        apState = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permission = Settings.System.canWrite(context);
        } else{
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }

        if(permission)
        {
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            try {
                apState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean setHotSpot(String SSID, String password)
    {
        boolean result = false;
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permission = Settings.System.canWrite(context);
        } else{
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }*/
        if(permission)
        {
            //wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


            /*try {
                apState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }*/
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

    public int getApState()
    {
        return apState;
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
