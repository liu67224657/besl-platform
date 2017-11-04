package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * ask service sngl
 * @author <a mailto="yinpengyi@fivewh.com">Ericliu</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class AskServiceSngl {
    //
    private static AskService instance;

    public static AskService get() {
        if (instance == null) {
            synchronized (AskServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(AskConstants.SERVICE_SECTION, AskConstants.SERVICE_TYPE);
                    try {
                        instance = new RPCClient<AskService>(cfg).createService(AskService.class);
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
