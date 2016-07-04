package br.com.thiengo.firebasenotification.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;

import br.com.thiengo.firebasenotification.domain.TokenEvent;


public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        TokenEvent tokenEvent = new TokenEvent( token );
        EventBus.getDefault().post(tokenEvent);
    }
}
