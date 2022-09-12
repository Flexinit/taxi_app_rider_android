package com.quawlebs.drupp.util;

import com.quawlebs.drupp.models.UserInfo;

import java.util.LinkedList;
import java.util.Queue;

public class PoolRideManager {

    private static volatile PoolRideManager fragmentSingleton;

    private static final Queue<UserInfo> poolRiderQueue = new LinkedList<>();

    private PoolRideManager() {
    }

    //Using Synchronized thread safe singleton
    public static PoolRideManager getInstance() {
        if (fragmentSingleton == null) {
            fragmentSingleton = new PoolRideManager();
        }

        synchronized (fragmentSingleton) {
            return fragmentSingleton;
        }
    }

    private void addToQueue(UserInfo rider) {
        poolRiderQueue.add(rider);
    }

    private void removeFromQueue(UserInfo rider) {
        poolRiderQueue.remove(rider);
    }


}
