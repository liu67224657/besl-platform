package com.enjoyf.platform.serv.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.service.service.ServiceLoadNull;

/**
 * A class that represents a registration of some service in naming server side..
 */
abstract class RegistrantServerSide {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrantServerSide.class);
    
    private static ServiceLoad defaultLoad = new ServiceLoadNull();

    private ServiceLoad serviceLoad = defaultLoad;
    private ServerRegInfo serverRegInfo;

    RegistrantServerSide(ServerRegInfo regInfo) {
        serverRegInfo = regInfo;
    }

    ServerRegInfo getServerRegInfo() {
        return serverRegInfo;
    }

    abstract boolean isLocal();

    void setLoad(ServiceLoad load) {
        if (load != null) {
            if (!(load instanceof ServiceLoadNull)) {
                logger.debug("RegistrantServerSide.setLoad: " + load);
            }

            serviceLoad = load;
        }
    }

    ServiceLoad getLoad() {
        return serviceLoad;
    }

    boolean isLoadInUse() {
        return serviceLoad != null && serviceLoad.isInUse();
    }

    public String abbreviatedToString() {
        StringBuffer sb = new StringBuffer(serverRegInfo.getClientRegInfo().getServiceId().toString());
        sb.append(":").append(serverRegInfo.getUniqueId().toString());
        if (serviceLoad.isInUse()) {
            sb.append(":load=[" + serviceLoad + "]");
        }

        return new String(sb);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(serverRegInfo.toString());
        if (serviceLoad.isInUse()) {
            sb.append(":load=[" + serviceLoad + "]");
        }

        return new String(sb);
    }
}


