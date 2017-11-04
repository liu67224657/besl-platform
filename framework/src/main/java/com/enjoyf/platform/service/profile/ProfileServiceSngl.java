/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class ProfileServiceSngl {
    //
    private static ProfileService instance;

    public static synchronized ProfileService get() {
        if (instance == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    ProfileConstants.SERVICE_SECTION, ProfileConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRingPartition(
                    EnvConfig.get().getServicePartitionNum(ProfileConstants.SERVICE_SECTION),
                    EnvConfig.get().getServicePartitionFailoverNum(ProfileConstants.SERVICE_SECTION),
                    EnvConfig.get().getRequestTimeoutMsecs()));

            instance = new ProfileServiceImpl(cfg);
        }

        return instance;
    }
}
