/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.joymeapp.ClientItemDomain;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:30
 * Description:
 */
public class ClientLineWebDataProcessorFactory {
    //
    private Map<ClientItemDomain, ClientLineWebDataProcessor> processorMap = new HashMap<ClientItemDomain, ClientLineWebDataProcessor>();

    //
    private static ClientLineWebDataProcessorFactory instance;

    public static ClientLineWebDataProcessorFactory get() {
        //
        if (instance == null) {
            synchronized (ClientLineWebDataProcessorFactory.class) {
                if (instance == null) {
                    instance = new ClientLineWebDataProcessorFactory();
                }
            }
        }

        return instance;
    }

    public ClientLineWebDataProcessor factory(ClientItemDomain domain) {
        //
        ClientLineWebDataProcessor returnValue;

        //
        synchronized (processorMap) {
            returnValue = processorMap.get(domain);
            //
            if (returnValue == null) {
                //
                if (ClientItemDomain.CMSARTICLE.equals(domain)) {
                    returnValue = new ClientLineCMSWebDataProcessor();
                } else if (ClientItemDomain.GAME.equals(domain)) {
                    returnValue = new ClientLineGameWebDataProcessor();
                } else if (ClientItemDomain.DEFAULT.equals(domain)) {
                    returnValue = new ClientLineDefaultWebDataProcessor();
                } else if (ClientItemDomain.COMMENTBEAN.equals(domain)) {
                    returnValue = new ClientLineCommentBeanWebDataProcessor();
                } else {
                    GAlerter.lab("ViewLineWebDataProcessor found an unknown line item type.");
                }

                if (returnValue != null) {
                    processorMap.put(domain, returnValue);
                }
            }
        }

        //
        return returnValue;
    }
}
