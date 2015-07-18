package com.echobond.intf;

import org.json.JSONObject;

public interface CommentCallback {
	public void onDialogConfirmed(String comment);
	public void onCommentResult(JSONObject result);
}
