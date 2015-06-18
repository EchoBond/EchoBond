package com.echobond.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GcmReceiver;

public class MyGcmReceiver extends GcmReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                MyGcmListenerService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        //make sure that when app stopped, it still can recv the broadcast, hence waking up the intent
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        setResultCode(Activity.RESULT_OK);
	}
}
