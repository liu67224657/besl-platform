package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRR;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午11:17
 * Desc:
 */
public class UserCenterServiceSngl {

    //
    private static UserCenterService instance;

    public static synchronized UserCenterService get() {
        if (instance == null) {
//            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
//                    UserCenterConstants.SERVICE_SECTION, UserCenterConstants.SERVICE_TYPE);

            //set the conn chooser type.
//            cfg.setConnChooser(new ConnChooserRingPartition(
//                    EnvConfig.get().getServicePartitionNum(UserCenterConstants.SERVICE_SECTION),
//                    EnvConfig.get().getServicePartitionFailoverNum(UserCenterConstants.SERVICE_SECTION),
//                    EnvConfig.get().getRequestTimeoutMsecs()));

//            cfg.setConnChooser(new ConnChooserRR(EnvConfig.get().getRequestTimeoutMsecs()));

          //  instance = new UserCenterServiceImpl(cfg);
            instance = new UserCenterServiceMicro();
        }

        return instance;
    }
}
