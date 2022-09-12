package com.quawlebs.drupp.helpers;

import com.google.firebase.database.DataSnapshot;

public interface INotifyEvent {
    void onNotificationReceived(String title, String message);

    void onChatReceived(DataSnapshot message);
}
