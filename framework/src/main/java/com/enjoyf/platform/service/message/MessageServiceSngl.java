/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.message;

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
public class MessageServiceSngl {
    //
    private static MessageService instance;

    public static MessageService get() {
        if (instance == null) {
            synchronized (MessageServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(MessageConstants.SERVICE_SECTION, MessageConstants.SERVICE_TYPE);

                    try {
                        int partitionNum = EnvConfig.get().getServicePartitionNum(MessageConstants.SERVICE_SECTION);
                        int partitionFailoverNum = EnvConfig.get().getServicePartitionFailoverNum(MessageConstants.SERVICE_SECTION);

                        instance = new RPCClient<MessageService>(cfg, partitionNum, partitionFailoverNum).createService(MessageService.class);
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
