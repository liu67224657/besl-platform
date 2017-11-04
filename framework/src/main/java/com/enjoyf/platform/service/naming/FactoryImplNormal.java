/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceInfoFile;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Bridge implementation for "normal" naming service objects.
 */
public class FactoryImplNormal implements FactoryImpl {
    /**
     * Create a default naming service.
     *
     * @param hostname The hostname where the service is found.
     */
    public NamingService create(String hostname) {
        return create(hostname, 7500);
    }

    /**
     * Create a default naming service.
     *
     * @param hostname The hostname where the service is found.
     * @param port     The port for the service.
     */
    public NamingService create(String hostname, int port) {
        ServiceAddress[] addr = new ServiceAddress[1];

        addr[0] = new ServiceAddress(hostname, port);

        NamingServiceAddress nsaddr = new NamingServiceAddress();
        nsaddr.set(addr);

        return new NamingServiceNormal(nsaddr);
    }

    /**
     * Use properties to decide what kind of naming service to create.
     *
     * @throws IllegalArgumentException Thrown if the props argument
     *                                  is null, or if the prop file is missing the 'namingservice.hostname'
     *                                  property.
     */
    public NamingService createFromProps(FiveProps props) {
        if (props == null) {
            throw new IllegalArgumentException("Prop file is null!");
        }

        //the hostname
        String name = props.get("namingservice.hostname");
        if (name == null) {
            GAlerter.lab("The naming service configure file is missed.");
            
            throw new IllegalArgumentException("The property 'namingservice.hostname' is null!!");
        }

        //the host port.
        int port = props.getInt("namingservice.port", 7500);

        NamingServiceAddress nsaddr = new NamingServiceAddress();
        nsaddr.setFromProp(name, port);

        return new NamingServiceNormal(nsaddr);
    }

    public Registrant createRegistrant(ServiceInfo info) {
        return new RegistrantNormal(info);
    }

    public Registrant createRegistrant(String sectionName, FiveProps props) {
        ServiceInfo info = new ServiceInfoFile(sectionName, props);

        return new RegistrantNormal(info);
    }
}
