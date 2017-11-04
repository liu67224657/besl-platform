package com.enjoyf.platform.util.umeng.ios;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.umeng.IOSNotification;

public class IOSUnicast extends IOSNotification {
	public IOSUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " set type exception.e", e);
		}
	}
}
