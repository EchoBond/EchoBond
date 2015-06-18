package com.echobond.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService{
	@Override
	public void onTokenRefresh() {
//		InstanceID instanceID = InstanceID.getInstance(this);
//		String regId = (String) SPUtil.get(this, MyApp.PREF_TYPE_SYSTEM, MyApp.SYS_GCM_ID, "", String.class);
	}
}
