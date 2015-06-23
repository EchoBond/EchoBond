package com.echobond.intf;

import org.json.JSONObject;

public interface ViewMoreSwitchCallback {
	public int onTypeSelected(int type);
	public void onSearchSelected(JSONObject jso);
}
