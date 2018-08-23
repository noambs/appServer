package com.barantech.noamb.appserver.services;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.barantech.noamb.appserver.R;
import com.barantech.noamb.appserver.screen.DeviceControl;

public class AsyncDialog extends AsyncTask<Void, Void, Boolean> {

    private DeviceControl mContext;
    private  Dialog dialog;
    public AsyncDialog(DeviceControl context)
    {
        mContext = context;
        dialog = new Dialog(mContext);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.mipmap.no_connection);
        dialog.setContentView(imageView);

        dialog.setCancelable(false);
       // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
    protected void onPreExecute()
    {
        super.onPreExecute();
        this.dialog.show();


    }

    @Override
    protected Boolean doInBackground(Void... booleans) {

        int indx = 0;
        try {
            while (indx < 100) {
                indx += 10;

                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute(Boolean showingDialog) {

        super.onPostExecute(showingDialog);
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();

            mContext.finish();
        }

    }
}
