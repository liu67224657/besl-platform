/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressKey;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Contains BackChannelServiceWrap objects.
 * this is a min server for other nss to connect.
 */
class BackChannelContainer {
    
    private static final Logger logger = LoggerFactory.getLogger(BackChannelContainer.class);
    /**
     * Maps a ServiceAddress to a BackChannelServiceWrap object.
     */
    private Map<ServiceAddressKey, BackChannelServiceWrap> backChannelServicesMap = 
	new ConcurrentHashMap<ServiceAddressKey, BackChannelServiceWrap>();
    private BackChannelServiceWrap[] backChannelServices;
    private BackChannelListener backChannelListener;

    /**
     * The ctor reads the RateConfig object and creates the
     * BackChannelServiceWrap objects we will need (one per
     * NS we will communicate with).
     *
     * @param cfg The server's config object.
     * @param l   A listener used by all BackChannelService objects
     *            to communicated with the server's business logic.
     * @param cbk A callback used to retrieve the RegistrantServerSideLocal
     *            objects for synching up with the other NS's.
     */
    BackChannelContainer(Config cfg, BackChannelListener l, BackChannelService.RegInfoCallback cbk) {
        backChannelListener = l;
        ServiceAddress myAddr = new ServiceAddress(cfg.getIp(), cfg.getBackChannelPort());

        //Get all the naming service back chanel service addresses from configure file except self.
        ServiceAddress[] namingServices = cfg.getNamingServices();
        backChannelServices = new BackChannelServiceWrap[namingServices.length];

        for (int i = 0; i < backChannelServices.length; i++) {
            logger.info("BackChannelContainer.ctor: Adding: " + namingServices[i]);
            backChannelServices[i] =
                    new BackChannelServiceWrap(
                            new LoadInfo.Getter() {
                                public LoadInfo getLoadInfo() {
                                    return backChannelListener == null ? null : backChannelListener.getLoadInfo();
                                }
                            },
                            cfg.getBackChannelPing(),
                            new BackChannelService(cbk, myAddr, cfg.getPort(), namingServices[i])
                    );

            ServiceAddressKey key = null;
            try {
                key = new ServiceAddressKey(namingServices[i]);
            }
            catch (UnknownHostException uhe) {
                GAlerter.lab("ERROR! Caught UnknownHostException for: "
                        + namingServices[i] + ": logicProcess will DIE and will "
                        + "need to be restarted!");
                Utility.sleep(5000);
                System.exit(1);
            }

            backChannelServicesMap.put(key, backChannelServices[i]);
        }

        backChannelListener = l;
    }

    BackChannelServiceWrap[] getServices() {
        return backChannelServices;
    }

    BackChannelServiceWrap get(ServiceAddress saddr) {
        ServiceAddressKey key = null;
        try {
            key = new ServiceAddressKey(saddr);
        }
        catch (UnknownHostException uhe) {
            logger.warn("BackChannelContainer: Could not find: " + saddr
                    + ": Got UnknownHostException");
            return null;
        }
        return backChannelServicesMap.get(key);
    }

    void close() {
        for (Iterator<BackChannelServiceWrap> itr = backChannelServicesMap.values().iterator(); itr.hasNext();) {
            BackChannelServiceWrap bchWrap = itr.next();
            bchWrap.close();
        }
    }
}
