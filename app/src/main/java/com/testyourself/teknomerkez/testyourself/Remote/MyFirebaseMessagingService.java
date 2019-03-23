package com.testyourself.teknomerkez.testyourself.Remote;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.testyourself.teknomerkez.testyourself.Common.Common;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        handleNotification(remoteMessage.getNotification().getBody());

    }

    private void handleNotification(String body) {

        Intent pushNotification = new Intent(Common.STR_NOTİFİCATİON);
        pushNotification.putExtra("message", body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

    }
}
