/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeappconfig;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class JoymeAppConfigServiceSngl {
    //
    private static JoymeAppConfigService instance;

    public static JoymeAppConfigService get() {
        if (instance == null) {
            synchronized (JoymeAppConfigServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(JoymeAppConfigConstants.SERVICE_SECTION, JoymeAppConfigConstants.SERVICE_TYPE);

                    try {
                        instance = new RPCClient<JoymeAppConfigService>(cfg).createService(JoymeAppConfigService.class);
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
