package br.com.thiengo.firebasenotification.domain;

import android.content.Intent;

public class NotificationEvent {
    private Intent content;

    public NotificationEvent(Intent content ){
        this.content = content;
    }

    public Intent getContent(){
        return content;
    }
}
