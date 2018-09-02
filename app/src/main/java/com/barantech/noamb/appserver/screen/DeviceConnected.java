package com.barantech.noamb.appserver.screen;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.CommunicationThread;
import com.barantech.noamb.appserver.services.HotSpotIntentService;
import com.barantech.noamb.appserver.services.MyOnStartTetheringCallback;
import com.barantech.noamb.appserver.services.Server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class DeviceConnected extends AppCompatActivity {
    private static SwipeRefreshLayout swipeRefreshLayout;
    public static LinkedHashMap<String, CommunicationThread> deviceList;
    private static Context context;
    private static RecyclerView mRecyclerView;
    private static ProgressBar mProgressBar;
    private boolean pause;
    private ImageButton hotSpotButton;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_connected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pause = false;
        hotSpotButton = (ImageButton) findViewById(R.id.switchHotSpot);
        hotSpotButton.setTag(R.drawable.hot_spot_on);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.device_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        mRecyclerView.addItemDecoration(horizontalDecoration);

        deviceList = new LinkedHashMap<String, CommunicationThread>();
        server = new Server(this);
        server.start();
        context = DeviceConnected.this;
        loadDevice();

        hotSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) hotSpotButton.getTag();
                if(tag == R.drawable.hot_spot_on)
                {

                    hotSpotButton.setBackgroundResource(R.drawable.hot_spot_off);
                    hotSpotButton.setTag(R.drawable.hot_spot_off);

                    turnOffHotSpot();
                    deviceList.clear();
                    loadDevice();
                }else{

                    hotSpotButton.setBackgroundResource(R.drawable.hot_spot_on);
                    hotSpotButton.setTag(R.drawable.hot_spot_on);

                    turnOnHotSpot();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                loadDevice();
            }
        });
    }



        public static void loadDevice()
        {
            if(deviceList.isEmpty())
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);

            }else{
                DeviceAdapter adapter;
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                adapter = new DeviceAdapter(deviceList);
                adapter.setOnClickListener(new DeviceAdapter.oneDeviceClickListener(){

                    @Override
                    public void onDeviceClicked(CommunicationThread device) {

                        Intent deviceIntent = new Intent(context, DeviceControl.class);
                        deviceIntent.putExtra(CommunicationThread.FIELD_MAC, device.getMacAddress());
                        context.startActivity(deviceIntent);

                    }
                }
                );

                mRecyclerView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }


        }

        private static class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder>
        {

            private LinkedHashMap<String, CommunicationThread> mDeviceList;
            private oneDeviceClickListener mClickListener;

            public DeviceAdapter(LinkedHashMap<String, CommunicationThread> deviceList)
            {
                this.mDeviceList = deviceList;
            }


            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.device_list_row, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                if(holder.IsBinded) return;
                holder.IsBinded = true;
                final CommunicationThread device = (new ArrayList<CommunicationThread>(mDeviceList.values())).get(position);
                if(device!=null)
                {
                    holder.mac.setText(device.getMacAddress());
                    holder.ip.setText(device.getIpAddress());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mClickListener != null) {
                                mClickListener.onDeviceClicked(device);
                            }
                        }
                    });
                }

            }



            @Override
            public int getItemCount() {
                return mDeviceList.size();
            }

            public void setOnClickListener(oneDeviceClickListener mClickListener) {
                this.mClickListener = mClickListener;
            }

            public class MyViewHolder extends RecyclerView.ViewHolder
            {
                public TextView mac;
                public ImageView photo;
                public boolean IsBinded;
                public TextView ip;
                public MyViewHolder(View view)
                {
                    super(view);
                    mac = (TextView) view.findViewById(R.id.device_mac);
                    ip = (TextView) view.findViewById(R.id.ip_address);
                    photo = (ImageView) view.findViewById(R.id.icon);
                }


            }

            public interface oneDeviceClickListener
            {
                void onDeviceClicked(CommunicationThread device);
            }


        }


        public static void addDevice(CommunicationThread device)
        {
            deviceList.put(device.getMacAddress(),device);


        }

        public static void removeDevice(CommunicationThread device)
        {
            deviceList.remove(device.getMacAddress());


        }


    @Override
   protected void onDestroy()
   {
       super.onDestroy();

       turnOffHotSpot();
       try {

           Thread.sleep(20000);

       } catch (Exception e) {
           e.printStackTrace();
       }
        Server.onDestroy();
   }

    @Override
   protected void onResume()
   {
       super.onResume();
       loadDevice();
   }




    private void turnOnHotSpot()
    {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            MyOnStartTetheringCallback callback = new MyOnStartTetheringCallback() {
                @Override
                public void onTetheringStarted() {

                }

                @Override
                public void onTetheringFailed() {

                }
            };

            HotSpotIntentService.mMyWifiManager.startTethering(callback, null);

        }else{
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method[] methods = wifiManager.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("setWifiApEnabled")) {
                    WifiConfiguration myConfig = new WifiConfiguration();
                    myConfig.SSID = "RBS1";
                    myConfig.preSharedKey = "12345678";
                    myConfig.status = WifiConfiguration.Status.ENABLED;
                    myConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    myConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    myConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiManager.setWifiEnabled(false); //Turning off wifi because tethering requires wifi to be off
                    try {
                        method.invoke(wifiManager, myConfig, true); //Activating tethering
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void turnOffHotSpot()
    {
       if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
       {
           HotSpotIntentService.mMyWifiManager.stopTethering();

       }else{
           WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
           Method[] methods = wifiManager.getClass().getDeclaredMethods();
           for (Method method : methods)
           {
               if (method.getName().equals("setWifiApEnabled"))
               {
                   try {
                       method.invoke(wifiManager, null, false);
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   } catch (InvocationTargetException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
    }

}
