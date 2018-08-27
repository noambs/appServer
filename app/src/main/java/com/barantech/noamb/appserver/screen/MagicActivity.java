package com.barantech.noamb.appserver.screen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.HotSpotIntentService;

public class MagicActivity extends PermissionsActivity{

    public static void useMagicActivityToTurnOn(Context c){
        Uri uri = new Uri.Builder().scheme(c.getString(R.string.intent_data_scheme)).authority(c.getString(R.string.intent_data_host_turnon)).build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(uri);
        c.startActivity(i);
    }

    public static void useMagicActivityToTurnOff(Context c){
        Uri uri = new Uri.Builder().scheme(c.getString(R.string.intent_data_scheme)).authority(c.getString(R.string.intent_data_host_turnoff)).build();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(uri);
        c.startActivity(i);
    }

    private static final String TAG = MagicActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

    }


    @Override
    void onPermissionsOkay() {
        carryOnWithHotSpotting();
    }

    /**
     * The whole purpose of this activity - to start {@link HotSpotIntentService}
     * This may be called straright away in {@code onCreate} or after permissions granted.
     */
    private void carryOnWithHotSpotting() {
        Intent intent = getIntent();
        HotSpotIntentService.start(this, intent);

    }
}
