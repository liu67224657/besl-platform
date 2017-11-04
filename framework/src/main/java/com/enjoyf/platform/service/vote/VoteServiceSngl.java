package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-18
 * Time: 下午6:01
 * To change this template use File | Settings | File Templates.
 */
public class VoteServiceSngl {
    private static VoteService instance;

    public static VoteService get() {
        if (instance == null) {
            synchronized (VoteServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(VoteConstants.SERVICE_SECTION, VoteConstants.SERVICE_TYPE);

                    try {
                        int partitionNum = EnvConfig.get().getServicePartitionNum(VoteConstants.SERVICE_SECTION);
                        int partitionFailoverNum = EnvConfig.get().getServicePartitionFailoverNum(VoteConstants.SERVICE_SECTION);

                        instance = new RPCClient<VoteService>(cfg, partitionNum, partitionFailoverNum).createService(VoteService.class);
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
