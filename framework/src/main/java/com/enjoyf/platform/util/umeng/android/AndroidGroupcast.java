package com.enjoyf.platform.util.umeng.android;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.umeng.AndroidNotification;

public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " set type exception.e", e);
		}
	}

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
