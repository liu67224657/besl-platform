/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageService;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class NoticeServiceSngl {
    //
    private static NoticeService instance;

    public static NoticeService get() {
        if (instance == null) {
            synchronized (NoticeServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(NoticeConstants.SERVICE_SECTION, NoticeConstants.SERVICE_TYPE);

                    try {
                        instance = new RPCClient<NoticeService>(cfg).createService(NoticeService.class);
                    } catch (InstantiationException e) {
                        GAlerter.lab("Cannot instantiate client:", e);
                    } catch (IllegalAccessException e) {
                        GAlerter.lab("Cannot instantiate client:", e);
                    }
                }
            }
        }

        return instance;
    }
}
