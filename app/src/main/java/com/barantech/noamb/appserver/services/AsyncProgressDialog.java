package com.barantech.noamb.appserver.services;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Window;
import com.barantech.noamb.appserver.screen.ConfigHotSpot;


public class AsyncProgressDialog extends AsyncTask<Void, Void, Boolean> {

    private ConfigHotSpot mContext;
    private ProgressDialog dialog;

    public AsyncProgressDialog(ConfigHotSpot context)
    {
        mContext = context;
        dialog = new ProgressDialog(mContext);
        //dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
    }

    @Override
    protected void onPreExecute()
    {

        super.onPreExecute();
        mContext.runOnUiThread(new Runnable(){

                                   @Override
                                   public void run() {
                                       dialog.show(mContext,"", "Loading...");
                                   }
                               }
        );


    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int indx = 0;
        try {
            while (indx < 100) {
                indx += 10;

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Boolean showingDialog) {

        super.onPostExecute(showingDialog);
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();

            //mContext.finish();
        }

    }
}
