package com.enjoyf.platform.util.umeng.ios;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.umeng.IOSNotification;

public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " set type exception.e", e);
		}
	}
}
