package com.echobond.intf;

import com.echobond.entity.User;

public interface StartPageFragmentsSwitchAsyncTaskCallback {
	public int onFragmentSelected(int index);
	public void onButtonSelected(int type, User user);
}
