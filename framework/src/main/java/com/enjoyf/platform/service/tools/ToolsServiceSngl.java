package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午11:17
 * Desc:
 */
public class ToolsServiceSngl {

     //
    private static ToolsService instance;

    public static synchronized ToolsService get() {
        if (instance == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    ToolsConstants.SERVICE_SECTION, ToolsConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRingPartition(
                    EnvConfig.get().getServicePartitionNum(ToolsConstants.SERVICE_SECTION),
                    EnvConfig.get().getServicePartitionFailoverNum(ToolsConstants.SERVICE_SECTION),
                    EnvConfig.get().getRequestTimeoutMsecs()));

            instance = new ToolsServiceImpl(cfg);
        }

        return instance;
    }
}
