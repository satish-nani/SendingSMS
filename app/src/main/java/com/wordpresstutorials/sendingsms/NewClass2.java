package com.wordpresstutorials.sendingsms;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by satish on 22/7/2017.
 */

public class NewClass2 extends IntentService {

    public NewClass2() {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("NewService", "About to execute ");

    }

}
