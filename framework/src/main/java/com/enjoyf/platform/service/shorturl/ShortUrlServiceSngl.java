/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class ShortUrlServiceSngl {
    //
    private static ShortUrlService instance;

    public static synchronized ShortUrlService get() {
        if (instance == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    ShortUrlConstants.SERVICE_SECTION, ShortUrlConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRingPartition(
                    EnvConfig.get().getServicePartitionNum(ShortUrlConstants.SERVICE_SECTION),
                    EnvConfig.get().getServicePartitionFailoverNum(ShortUrlConstants.SERVICE_SECTION),
                    EnvConfig.get().getRequestTimeoutMsecs()));

            instance = new ShortUrlServiceImpl(cfg);
        }

        return instance;
    }
}
