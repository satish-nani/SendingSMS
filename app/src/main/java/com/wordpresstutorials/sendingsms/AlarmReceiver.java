package com.wordpresstutorials.sendingsms;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by satish on 9/4/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String INTENT_FILTER="com.wordpresstutorials.sendingsms.intent.action.SMSDISPATCHER";



    @Override
    public void onReceive(Context context, Intent intent) {
           // SmsManager smsManager=SmsManager.getDefault();
           // Log.i("contactNumber",contactNumber);
            //Log.i("messageText",messageText);
           // smsManager.sendTextMessage("+918939376946",null,"nvjkrvk",null,null);

            Log.i("Received Alarm", "Alarm Trigerred");
            Log.i("Context", context.getPackageName());
            Log.i("Action name",intent.getAction());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+918374497624", null, "Sent by alarm", null, null);
    }

}
