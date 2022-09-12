package com.quawlebs.drupp.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.ICancelCallBack;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static AlertDialog showDialogProgressBar(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_loading);
        return builder.create();
    }

    public static Dialog showDialogCancelProgressBar(Context context, ICancelCallBack cancelCallBack) {
        JJSearchView logo;
        View alertLayout = LayoutInflater.from(context).inflate(R.layout.dialog_with_cancel, null);
        logo = alertLayout.findViewById(R.id.logo);
        logo.setController(new JJAroundCircleBornTailController());
        Handler handler = new Handler();
        final Dialog dialog = new Dialog(context) {
            @Override
            public void show() {
//                logo.startAnim();
                handler.postDelayed(logo::startAnim,2000);

                super.show();
            }
        };

        dialog.setContentView(alertLayout);


//        Glide.with(context).asGif().load(R.raw.search).into(logo);


//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.setCancelable(false);
//        alertDialog.setView(alertLayout);


//        Animation anim = new CircularRotateAnimation(image)


        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            cancelCallBack.onCancel();
            dialog.dismiss();
        });
        return dialog;
    }


    public static PopupMenu showPopUpMenu(Context context, View view, @MenuRes int menuRes) {
        //Creating the instance of PopupMenu and setting anchor
        PopupMenu popup = new PopupMenu(context, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());
        //showing popup menu
        popup.show();

        return popup;
    }

    public static AlertDialog showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.delete_history))
                .setTitle(context.getString(R.string.delete_title))
                .setCancelable(true);
        return builder.create();

    }

    public static AlertDialog showCustomAlertDialog(Context context, @LayoutRes int view, AppConstants.NotificationType notificationType) {
        View alertLayout = LayoutInflater.from(context).inflate(view, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        alertLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());
        return alertDialog;
    }
    public static AlertDialog showCustomAlertDialog(Context context, @LayoutRes int view) {
        View alertLayout = LayoutInflater.from(context).inflate(view, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        alertLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());
        return alertDialog;
    }
    public static PopupWindow showPopUpWindow(Context context, @LayoutRes int view) {
        View popUpView = LayoutInflater.from(context).inflate(view, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, popUpView.getWidth(), popUpView.getHeight(), true);
        // dismiss the popup window when touched
        popUpView.setOnTouchListener((view1, motionEvent) -> {
            popupWindow.dismiss();
            return true;
        });
        return popupWindow;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Bitmap takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
