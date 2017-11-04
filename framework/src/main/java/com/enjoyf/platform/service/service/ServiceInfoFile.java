/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.net.InetAddress;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.util.FiveProps;

/**
 * Class that pulls out the ServiceRegInfo from a Props object.
 */
@SuppressWarnings("serial")
public class ServiceInfoFile extends ServiceInfo {
    /**
     * This is so that serialization works. Apparently, if you implement
     * your own readExternal()/writeExternal() methods, you need to provide
     * a public default ctor.
     */
    public ServiceInfoFile() {
        super();
    }

    public ServiceInfoFile(String serviceSection, FiveProps props) {
        super();

        String sval = null;
        ServiceId sid = new ServiceId();

        sval = props.get(serviceSection + ".TYPE");
        if (sval != null) {
            sid.setServiceType(sval);
        } else {
            sid.setServiceType(serviceSection);
        }

        sval = props.get(serviceSection + ".NAME");
        if (sval != null) {
            sid.setServiceName(sval);
        }

        setServiceId(sid);

        //
        InetAddress iaddr = null;
        sval = props.get(serviceSection + ".IP");
        try {
            if (sval != null) {
                iaddr = InetAddress.getByName(sval);
            } else {
                iaddr = InetAddress.getLocalHost();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Could not get local host!");
        }
        
        int port = props.getInt(serviceSection + ".PORT", 12345);

        ServiceAddress saddr = new ServiceAddress(iaddr.getHostName(), port);

        //
        setServiceAddress(saddr);
    }
}
