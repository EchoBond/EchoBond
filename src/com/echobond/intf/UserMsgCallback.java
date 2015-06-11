package com.echobond.intf;

import org.json.JSONObject;

public interface UserMsgCallback {
	public void onSendResult(JSONObject result);
	public void onLoadResult(JSONObject result);
}
