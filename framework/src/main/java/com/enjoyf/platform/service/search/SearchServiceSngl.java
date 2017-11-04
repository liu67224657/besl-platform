package com.enjoyf.platform.service.search;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 11-8-25
 * Time: 上午8:24
 * To change this template use File | Settings | File Templates.
 */
public class SearchServiceSngl {
    private static SearchService service = null;

    public static synchronized void set(SearchService service) {
        SearchServiceSngl.service = service;
    }

    /**
     * Gets the RateCheckService
     *
     * @return RateCheckService - the RateCheckService
     */
    public static synchronized SearchService get() {
        if (service == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    SearchConstants.SERVICE_SECTION, SearchConstants.SERVICE_TYPE);

            //set the conn chooser type.
//            cfg.setConnChooser(new ConnChooserRingPartition(
//                    EnvConfig.get().getServicePartitionNum(SearchConstants.SERVICE_SECTION),
//                    EnvConfig.get().getServicePartitionFailoverNum(SearchConstants.SERVICE_SECTION),
//                    EnvConfig.get().getRequestTimeoutMsecs()));

            service = new SearchServiceImpl(cfg);
        }

        return service;
    }

}
