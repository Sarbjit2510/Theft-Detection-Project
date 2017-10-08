package com.pragyaware.sarbjit.jkpddapp.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by sarbjit on 06/24/2017.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
//    final SmsManager sms = SmsManager.getDefault();


    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object aPdusObj : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    if (senderNum.contains("PRAGYA")) {
                        String message = currentMessage.getDisplayMessageBody().split("is ")[1];
                        Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        // Show Alert
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }

    }
}
