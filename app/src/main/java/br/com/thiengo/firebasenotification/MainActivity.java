package br.com.thiengo.firebasenotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.thiengo.firebasenotification.domain.NotificationEvent;
import br.com.thiengo.firebasenotification.domain.TokenEvent;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvToken;
    private int[] checkBoxIds = {
            R.id.id_chk_soccer,
            R.id.id_chk_basketball,
            R.id.id_chk_volley,
            R.id.id_chk_swimming,
            R.id.id_chk_news
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);
        initViews();
        intentData( getIntent() );
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initViews(){
        tvTitle = (TextView) findViewById(R.id.id_title);
        tvContent = (TextView) findViewById(R.id.id_content);
        tvToken = (TextView) findViewById(R.id.id_token);
        tvToken.setText( FirebaseInstanceId.getInstance().getToken() );
        Log.i("log", "token: "+FirebaseInstanceId.getInstance().getToken());

        initCheckBoxes();
    }

    private void initCheckBoxes(){
        for( int id : checkBoxIds ){
            retrieveSPCheckboxValue( id );
            setOnCheckedChangeListener( id );
        }
    }

    private void setOnCheckedChangeListener( int viewId ){
        ((CheckBox) findViewById( viewId )).setOnCheckedChangeListener(this);
    }

    private void retrieveSPCheckboxValue( int id ){
        CheckBox checkBox = (CheckBox) findViewById(id);
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        boolean value = sp.getBoolean( String.valueOf( id ), false );

        checkBox.setChecked( value );
    }

    private void setSPCheckboxValue( int id, boolean value ){
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        edit.putBoolean( String.valueOf( id ), value );
        edit.apply();
    }

    private void intentData(Intent intent){
        if( intent == null || intent.getExtras() == null ){
            return;
        }

        tvTitle.setText( intent.getExtras().getString("title") );
        tvContent.setText( intent.getExtras().getString("content") );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String topic = getTopic( buttonView );

        if( isChecked ){
            FirebaseMessaging.getInstance().subscribeToTopic( topic );
            displayMessage( "Vc foi inscrito no tópico: "+topic );
        }
        else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic( topic );
            displayMessage( "Vc foi removido do tópico: "+topic );
        }

        setSPCheckboxValue( buttonView.getId(), isChecked );
    }

    private String getTopic( CompoundButton buttonView ){
        switch( buttonView.getId() ){
            case R.id.id_chk_soccer:
                return "soccer";
            case R.id.id_chk_basketball:
                return "basketball";
            case R.id.id_chk_volley:
                return "volley";
            case R.id.id_chk_swimming:
                return "swimming";
            default:
                return "news";
        }
    }

    private void displayMessage( String message ){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewNotification(NotificationEvent notification){
        intentData( notification.getContent() );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewToken(TokenEvent tokenEvent){
        tvToken.setText( tokenEvent.getToken() );
    }
}
