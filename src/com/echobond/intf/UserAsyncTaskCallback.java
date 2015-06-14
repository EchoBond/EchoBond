package com.echobond.intf;

import org.json.JSONObject;

public interface UserAsyncTaskCallback {
	public void onLoadUsersResult(JSONObject result);
	public void onUpdateUserResult(JSONObject result);
}
