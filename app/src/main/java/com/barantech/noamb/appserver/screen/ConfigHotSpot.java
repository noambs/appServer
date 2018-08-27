package com.barantech.noamb.appserver.screen;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.barantech.noamb.appserver.R;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class ConfigHotSpot extends PermissionsActivity {


    public static ConfigHotSpot context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            setContentView(R.layout.activity_hot_spot);
            final TextView actionsNoteTv = findViewById(R.id.actions_note_tv);
            actionsNoteTv.setMovementMethod(LinkMovementMethod.getInstance());


        }

        else
        {
            setContentView(R.layout.activity_config_hot_spot);
        }

        context = this;

    }

    @Override
    void onPermissionsOkay() {

    }


    public void onClickTurnOnAction(View v){
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int apState = 0;
        try {
            apState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(apState==13)
        {
            Intent intent = new Intent(getString(R.string.intent_action_turnoff));
            sendImplicitBroadcast(this,intent);

        }else{
            Intent intent = new Intent(getString(R.string.intent_action_turnon));
            sendImplicitBroadcast(this,intent);
        }

    }




    private static void sendImplicitBroadcast(Context ctxt, Intent i) {
        PackageManager pm=ctxt.getPackageManager();
        List<ResolveInfo> matches=pm.queryBroadcastReceivers(i, 0);

        for (ResolveInfo resolveInfo : matches) {
            Intent explicit=new Intent(i);
            ComponentName cn=
                    new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                            resolveInfo.activityInfo.name);

            explicit.setComponent(cn);
            ctxt.sendBroadcast(explicit);
        }
    }




}
