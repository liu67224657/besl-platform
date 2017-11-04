/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.search;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.serv.search.SearchConfig;
import com.enjoyf.platform.serv.search.SearchLogic;
import com.enjoyf.platform.serv.search.SearchPacketDecoder;
import com.enjoyf.platform.service.search.SearchConstants;
import com.enjoyf.platform.util.FiveProps;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class SearchInitializer implements ServiceInitializer {
    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(SearchConstants.SERVICE_PREFIX, servProps);

        SearchConfig config = new SearchConfig(servProps);
        //GAlerterLogger.lh(config.toString());

        SearchLogic logic = new SearchLogic(config);
        server.setPacketProcessor(new SearchPacketDecoder(logic));

        return new ServiceConfig(servProps, SearchConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
