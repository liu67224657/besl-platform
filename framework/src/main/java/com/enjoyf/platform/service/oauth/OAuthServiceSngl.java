/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRR;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class OAuthServiceSngl {
    private static OAuthService instance;

    public static synchronized void set(OAuthService service) {
        instance = service;
    }

    public static synchronized OAuthService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(OAuthConstants.SERVICE_SECTION, OAuthConstants.SERVICE_TYPE);

        //set the conn chooser type.
/*        cfg.setConnChooser(new ConnChooserRingPartition(
                EnvConfig.get().getServicePartitionNum(OAuthConstants.SERVICE_SECTION),
                EnvConfig.get().getServicePartitionFailoverNum(OAuthConstants.SERVICE_SECTION),
                EnvConfig.get().getRequestTimeoutMsecs()));*/

        cfg.setConnChooser(new ConnChooserRR(EnvConfig.get().getRequestTimeoutMsecs()));

        instance = new OAuthServiceCache(new OAuthServiceImpl(cfg));
    }

}
