package com.echobond.intf;

import org.json.JSONObject;

public interface ImageCallback {
	public void onUploadImage(JSONObject result);
	public void onDownloadImage(JSONObject result);
}
