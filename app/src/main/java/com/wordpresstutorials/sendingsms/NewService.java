package com.wordpresstutorials.sendingsms;

/**
 * Created by satish on 11/4/2017.
 */

        import android.app.IntentService;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.os.IBinder;
        import android.support.annotation.Nullable;
        import android.support.v7.app.NotificationCompat;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.widget.RemoteViews;
        import android.widget.Toast;


/**
 * Created by dell on 3/14/2017.
 */

public class NewService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service is created","Service is created");

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Entered Service","Entered Service");
        /*
        if(intent.hasExtra("contactNumber")&&intent.hasExtra("messageContent")) {
            smsManager.sendTextMessage(intent.getExtras().getString("contactNumber"), null, intent.getExtras().getString("messageContent"), null, null);
        }else{*/
        SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage("+918939376946", null, "Sent through service", null, null);
        /*}*/
            return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onCreate();
    }
}
