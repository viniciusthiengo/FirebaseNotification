package br.com.thiengo.firebasenotification.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import br.com.thiengo.firebasenotification.domain.NotificationEvent;


public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.i("log", "From ---> "+remoteMessage.getFrom());
        Log.i("log", "MessageId ---> "+remoteMessage.getMessageId());
        Log.i("log", "To ---> "+remoteMessage.getTo());
        Log.i("log", "SentTime ---> "+remoteMessage.getSentTime());
        Log.i("log", "Ttl ---> "+remoteMessage.getTtl());
        Log.i("log", "CollapseKey ---> "+remoteMessage.getCollapseKey());
        Log.i("log", "MessageType ---> "+remoteMessage.getMessageType());
        Log.i("log", "Data ---> "+remoteMessage.getData());
        Log.i("log", "Notification : Body ---> "+remoteMessage.getNotification().getBody());

        Intent intent = getIntentBody(remoteMessage );
        NotificationEvent notificationEvent = new NotificationEvent( intent );
        EventBus.getDefault().post(notificationEvent);
    }

    private Intent getIntentBody(RemoteMessage remoteMessage ){
        Intent intent = new Intent();
        Bundle extras = new Bundle();

        for( String key : remoteMessage.getData().keySet() ){
            extras.putString( key, remoteMessage.getData().get( key ) );
        }
        intent.putExtras(extras);
        return intent;
    }
}
