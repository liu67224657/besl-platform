/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

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
public class TimeLineServiceSngl {
    //
    private static TimeLineService instance;

    public static TimeLineService get() {
        if (instance == null) {
            synchronized (TimeLineServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(TimeLineConstants.SERVICE_SECTION, TimeLineConstants.SERVICE_TYPE);

                    try {
                        int partitionNum = EnvConfig.get().getServicePartitionNum(TimeLineConstants.SERVICE_SECTION);
                        int partitionFailoverNum = EnvConfig.get().getServicePartitionFailoverNum(TimeLineConstants.SERVICE_SECTION);

                        instance = new RPCClient<TimeLineService>(cfg, partitionNum, partitionFailoverNum).createService(TimeLineService.class);
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
