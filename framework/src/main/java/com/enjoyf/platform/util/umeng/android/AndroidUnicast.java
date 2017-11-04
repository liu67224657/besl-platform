package com.enjoyf.platform.util.umeng.android;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.umeng.AndroidNotification;

public class AndroidUnicast extends AndroidNotification {
	public AndroidUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " set type exception.e", e);
		}
	}
}