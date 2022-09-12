package com.quawlebs.drupp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quawlebs.drupp.models.ResponseData;
import com.quawlebs.drupp.models.UserInfo;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.Helper;
import com.quawlebs.drupp.helpers.INotifyEvent;
import com.quawlebs.drupp.helpers.SessionManager;

public class NotificationService extends Service {

    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference;
    private INotifyEvent iNotifyEvent;
    private FirebaseAuth mFirebaseAuth;
    private int riderId;
    private String mUsername;


    public NotificationService() {

    }

    //Instance of inner class created to provide access  to public methods in this class
    private final IBinder localBinder = new LocalBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mDatabase = FirebaseDatabase.getInstance();
            String chatId = "";
            ResponseData driverData = Helper.getInstance(this).readFromJson(AppConstants.DRIVER_DETAILS, ResponseData.class);
            if (SessionManager.getInstance().getUserModel() != null) {
                UserInfo userInfo = SessionManager.getInstance().getUserModel().getUserInfo();
                if (driverData != null) {
                    chatId = userInfo.getId() + "_" + driverData.getDriverId();
                    mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(chatId);
                    attachDatabaseReadListener();
                }
            }

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "ERROR");
        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        detachDatabaseReadListener();
        return super.onUnbind(intent);
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    public void setiNotifyEvent(INotifyEvent iNotifyEvent) {
        this.iNotifyEvent = iNotifyEvent;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Received Messages
                    iNotifyEvent.onChatReceived(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesReference.addChildEventListener(mChildEventListener);
        }
//        if (mValueEventListener == null) {
//            mValueEventListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    for (DataSnapshot value : dataSnapshot.getChildren()) {
////                        Log.d("Locations updated", "location: " + value);
////                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            };
//            mMessagesReference.addValueEventListener(mValueEventListener);
//        }
    }


    public class LocalBinder extends Binder {

        public NotificationService getService() {
            return NotificationService.this;

        }
    }


}
