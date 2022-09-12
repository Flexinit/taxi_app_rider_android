package com.quawlebs.drupp.util;

import android.widget.Toast;

import com.quawlebs.drupp.DruppApp;

public class UiUtils {

    public static void showToast(String msg) {
        Toast.makeText(DruppApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(DruppApp.getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
