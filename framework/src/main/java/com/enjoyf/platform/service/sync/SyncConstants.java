package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

import java.util.List;

/**
 * <p/>
 * Description:单例类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncConstants {
    public static final String SERVICE_SECTION = "syncservice";
    public static final String SERVICE_PREFIX = "syncserver";
    public static final String SERVICE_TYPE = "syncserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }

}
