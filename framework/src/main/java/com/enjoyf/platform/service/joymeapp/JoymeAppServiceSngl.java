/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.service.social.SocialConstants;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class JoymeAppServiceSngl {
    //
    private static JoymeAppService instance;

    public static JoymeAppService get() {
        if (instance == null) {
            synchronized (JoymeAppServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(JoymeAppConstants.SERVICE_SECTION, JoymeAppConstants.SERVICE_TYPE);


                    try {
                        //int partitionNum = EnvConfig.get().getServicePartitionNum(JoymeAppConstants.SERVICE_SECTION);
                        //int partitionFailoverNum = EnvConfig.get().getServicePartitionFailoverNum(JoymeAppConstants.SERVICE_SECTION);

                        instance = new RPCClient<JoymeAppService>(cfg).createService(JoymeAppService.class);
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
