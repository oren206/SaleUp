package com.saleup;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    // ...

    public RegistrationIntentService()
    {
        super("RegistrationIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(/*getString(R.string.gcm_defaultSenderId)*/"50089535523",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        }
        catch (IOException ex){

        }


    }


}