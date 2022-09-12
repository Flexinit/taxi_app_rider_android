package com.quawlebs.drupp.connectivity;

import android.content.Context;

public class SocketManager {

    private static volatile SocketManager instance;

    private SocketManager(Context context) {
        //Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static SocketManager getInstance(Context context) {
        //Double check locking pattern
        if (instance == null) { //Check for the first time
            synchronized (SocketManager.class) { //Check for the second time.
                //if there is no instance available... create new one
                if (instance == null) {
                    return new SocketManager(context);
                }
            }
        }
        return instance;
    }


}
