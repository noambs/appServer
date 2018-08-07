package com.barantech.noamb.appserver.screen;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.services.CommunicationThread;
import com.barantech.noamb.appserver.services.HotSpot;
import com.barantech.noamb.appserver.services.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class DeviceConnected extends AppCompatActivity {
    private static SwipeRefreshLayout swipeRefreshLayout;
    public static LinkedHashMap<String, CommunicationThread> deviceList;
    private static Context context;
    private static RecyclerView mRecyclerView;
    private static ProgressBar mProgressBar;
    private String SSID;
    private String passWord;
    private HotSpot mHotSpot;
    private Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_connected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SSID = getIntent().getExtras().getString("SSID");
        passWord = getIntent().getExtras().getString("password");

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

        context = DeviceConnected.this;
        loadDevice();

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
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.device_list_row, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
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



    protected void onDestroy()
    {
        super.onDestroy();
        server.onDestroy();
        HotSpot.onDestroy();
    }
}
