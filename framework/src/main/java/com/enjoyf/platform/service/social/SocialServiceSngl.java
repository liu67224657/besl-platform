/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class SocialServiceSngl {
    //
    private static SocialService instance;

    public static synchronized SocialService get() {
        if (instance == null) {
            synchronized (SocialServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(SocialConstants.SERVICE_SECTION, SocialConstants.SERVICE_TYPE);

                    try {
//                        int partitionNum = EnvConfig.get().getServicePartitionNum(SocialConstants.SERVICE_SECTION);
//                        int partitionFailoverNum = EnvConfig.get().getServicePartitionFailoverNum(SocialConstants.SERVICE_SECTION);
                        instance = new RPCClient<SocialService>(cfg).createService(SocialService.class);
//                        instance = new RPCClient<SocialService>(cfg, partitionNum, partitionFailoverNum).createService(SocialService.class);
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
