package com.quawlebs.drupp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.quawlebs.drupp.R;

public class AppUtil {
    public static String generateReference() {
        String keys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = (int) (keys.length() * Math.random());
            sb.append(keys.charAt(index));
        }

        return sb.toString();
    }

    public static String handleDateString(String timeStamp) {
        if (timeStamp.isEmpty()) {
            return "";
        }
        if (timeStamp != null) {
            java.util.Date given_timestamp = new java.util.Date(Long.valueOf(timeStamp) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];

            return (day + "/" + month + "/" + year);
        } else {
            return "";
        }

    }

    public static Drawable setBadgeCount(Context context, int res, int badgeCount) {
        LayerDrawable icon = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.notification_badge);
        Drawable mainIcon = ContextCompat.getDrawable(context, res);
        BadgeDrawable badge = new BadgeDrawable(context);
        badge.setCount(String.valueOf(badgeCount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
        icon.setDrawableByLayerId(R.id.ic_main_icon, mainIcon);

        return icon;
    }

    public static double getDistanceBetweenLatLongs_Kilometers(LatLng latLng1,
                                                               LatLng latLng2) {
        double theta = latLng1.longitude - latLng2.longitude;
        double dist = Math.sin((latLng1.latitude * Math.PI / 180.0))
                * Math.sin((latLng2.latitude * Math.PI / 180.0))
                + Math.cos((latLng1.latitude * Math.PI / 180.0))
                * Math.cos((latLng2.latitude * Math.PI / 180.0))
                * Math.cos((theta * Math.PI / 180.0));
        dist = Math.acos(dist);
        dist = (dist * 180.0 / Math.PI);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        // in Kilometers
        return (dist);
    }


}
