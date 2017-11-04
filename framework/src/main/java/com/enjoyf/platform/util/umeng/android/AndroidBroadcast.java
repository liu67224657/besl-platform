package com.enjoyf.platform.util.umeng.android;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.umeng.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
	public AndroidBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " set type exception.e", e);
		}
	}
}
