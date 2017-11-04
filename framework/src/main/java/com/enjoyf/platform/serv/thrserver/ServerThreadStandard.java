/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.FiveProps;

/**
 * Convenience class which creates a java server that processes requests
 * on the incoming connection thread.
 * <p/>
 * NOTE: In order to guarantee performance, the server should not perform
 * any blocking i/o operations when processing a packet. It should all be
 * i/o.
 */
public class ServerThreadStandard extends ServerThreadDefault {
    /**
     * Construct the object.
     *
     * @param servicePrefix The prefix of the PORT property to use.
     */
    public ServerThreadStandard(String servicePrefix, FiveProps servProps) {
        configurePort(servicePrefix, servProps);
        setWriteStrategy(servProps);
    }

    public ServerThreadStandard(int port) {
        super.setPort(port);
    }

    /**
     * Start this object.
     */
    public void start() {
        super.setConnFactory(p_getConnFactory());
        super.start();
    }

    private ConnThreadBaseFactory p_getConnFactory() {
        ConnThreadBaseFactory factory = getConnFactory();
        if (factory == null) {
            PacketHandlerSimple ph = new PacketHandlerSimple(getPacketProcessor());

            factory = new ConnThreadFactoryDefault(getPacketWriter(), getPacketReader(), ph, getWriteStrategy());
        }

        return factory;
    }
}
