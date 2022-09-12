package com.quawlebs.drupp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quawlebs.drupp.models.DriverPostRide;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;

import static android.content.Context.MODE_PRIVATE;

public class Receiver {
    Context context;
    SharedPreferences sharedPreferences;
    private static volatile Receiver receiver;
    private Receiver(Context context) {
        this.context = context;

    }
    public static Receiver getInstance(Context context){
        if(receiver == null)
             receiver = new Receiver(context);

        synchronized (receiver){
            return receiver;
        }
    }

    private void init(){

    }

    private BroadcastReceiver rating = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedPreferences = context.getSharedPreferences("Ratings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //if (!sharedPreferences.getBoolean("status", false)) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ratings, null);
            DriverPostRide driverPostRide = Helper.getInstance(context).readFromJson(AppConstants.DRIVER_POST_RIDE, DriverPostRide.class);
            TextView by = alertLayout.findViewById(R.id.by_ratings);
            TextView msg = alertLayout.findViewById(R.id.msg_ratings);
            RatingBar ratingBar = alertLayout.findViewById(R.id.rateBar_ratings);
            androidx.appcompat.app.AlertDialog.Builder alertDialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
            final androidx.appcompat.app.AlertDialog alertDialog = alertDialogbuilder.create();
            by.setText(driverPostRide.getDriverPost_driverName());
            msg.setText(sharedPreferences.getString("msg", ""));
            ratingBar.setRating(Integer.parseInt(sharedPreferences.getString("rate", "")));
            alertDialog.setView(alertLayout);
            //editor.putBoolean("status",true);
            editor.commit();
            alertDialog.show();
            alertLayout.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
        //}
    };
}
