package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author: Daniel
 */
public class StatServiceSngl {
	
	private static volatile StatService instance;

    public static StatService get() {
        if (instance == null) {
        	synchronized (StatServiceSngl.class) {
        		if (instance == null) {
        			ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                            StatConstants.SERVICE_SECTION, StatConstants.SERVICE_TYPE);
        			
        			try {
						instance = new RPCClient<StatService>(cfg).createService(StatService.class);
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
