package com.quawlebs.drupp.service;

import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quawlebs.drupp.helpers.INotifyEvent;

public class LocationTrackingService extends Service {


    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private INotifyEvent iNotifyEvent;
    private FirebaseAuth mFirebaseAuth;
    private int riderId;
    private String mUsername;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
