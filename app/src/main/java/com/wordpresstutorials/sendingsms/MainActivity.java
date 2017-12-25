package com.wordpresstutorials.sendingsms;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;
    EditText mPhoneNumber;
    EditText mMessage;
    String message=null;
    String phoneNumber=null;
    Button alarmButton;
    int number;
    PendingIntent pIntent;
    Calendar destroyCal;
    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SET_ALARM},2);
        mButton = (Button)findViewById(R.id.send_button);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mMessage = (EditText) findViewById(R.id.message);
        message=mMessage.getText().toString();
        phoneNumber=mPhoneNumber.getText().toString();
        alarmButton=(Button)findViewById(R.id.alarm_button);
        alarmButton.setOnClickListener(this);
        mButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.send_button:
                if(!message.equals(null)&&!phoneNumber.equals(null)){
                    sendSS(mMessage.getText().toString(),mPhoneNumber.getText().toString());
                    Log.i("Phone Number",mPhoneNumber.getText().toString());
                    Log.i("Message",mMessage.getText().toString());
                }
                break;
            case R.id.alarm_button:
                int currentHour,currentMinute;
                Calendar ca = Calendar.getInstance();
                currentHour = ca.get(Calendar.HOUR_OF_DAY);
                currentMinute = ca.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                    sendPickedTime(hourOfDay,minute);
                            }
                        }, currentHour, currentMinute, true);
                tpd.show();
                break;
        }

    }


    public void sendSS(String messageText, String contactNumber){

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.WAKE_LOCK,Manifest.permission.BROADCAST_STICKY},1);
        SmsManager smsManager=SmsManager.getDefault();
        Log.i("contactNumber",contactNumber);
        Log.i("messageText",messageText);
       smsManager.sendTextMessage(contactNumber,null,messageText,sentPI,deliveredPI);
    }

    public void sendPickedTime(final int mHour, final int mMinute){

        int mYear,mMonth,mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar messageDT=Calendar.getInstance();
                        messageDT.set(year, monthOfYear , dayOfMonth);
                        messageDT.set(Calendar.HOUR_OF_DAY, mHour);
                        messageDT.set(Calendar.MINUTE, mMinute);
                        messageDT.set(Calendar.SECOND, 0);
                        sendMessage(messageDT,MainActivity.this);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public void sendMessage(Calendar cal,Context mContext){
        Intent intent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        //Intent intent = new Intent(MainActivity.this, NewService.class);
        String message=mMessage.getText().toString();
        String phoneNumber=mPhoneNumber.getText().toString();
        if(!message.equals(null)&&!phoneNumber.equals(null)){
        intent.putExtra("contactNumber",phoneNumber);
        intent.putExtra("messageContent",message);
        }
        Intent intent2=new Intent(this.getApplicationContext(),AlarmReceiver.class);
        intent2.setAction(AlarmReceiver.INTENT_FILTER);
        pIntent  = PendingIntent.getBroadcast(this.getApplicationContext(), 1234,intent2, 0);
        Toast.makeText(this, "We will takecare of your sms :)", Toast.LENGTH_LONG).show();
        alarmManager = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), pIntent);
        Toast.makeText(this, "Alarm has been set", Toast.LENGTH_LONG).show();
        Log.i("Alarm Time",String.valueOf(cal.getTime().getTime()));

    }


}
