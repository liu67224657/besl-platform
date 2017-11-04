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
public class UserCenterServiceTestSngl {

     //
    private static UserCenterService instance;

    public static synchronized UserCenterService get() {
        if (instance == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    UserCenterConstants.SERVICE_SECTION_TEST, UserCenterConstants.SERVICE_TYPE_TEST);

            cfg.setConnChooser(new ConnChooserRR(EnvConfig.get().getRequestTimeoutMsecs()));

            instance = new UserCenterServiceImpl(cfg);
        }

        return instance;
    }
}
