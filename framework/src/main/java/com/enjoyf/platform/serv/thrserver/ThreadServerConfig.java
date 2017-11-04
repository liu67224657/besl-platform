/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.net.InetAddress;

import com.enjoyf.platform.util.FiveProps;

/**
 * A class to encapsulate the config values for a ServerThread.
 */
public class ThreadServerConfig {
    private int port;
    private ConnThreadBaseFactory connFactory;

    private InetAddress ip;
    private FiveProps props;

    private boolean nio = false;

    //
    public ThreadServerConfig(int port, ConnThreadBaseFactory connFactory) {
        this.port = port;
        this.connFactory = connFactory;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setConnFactory(ConnThreadBaseFactory connFactory) {
        this.connFactory = connFactory;
    }

    public ConnThreadBaseFactory getConnFactory() {
        return connFactory;
    }

    public void setNio(boolean nio) {
        this.nio = nio;
    }

    /**
     * By default, this server will run on the "localhost". On multi-homed
     * machines, one can choose a different ip.
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getIp() {
        return ip;
    }

    public boolean isNio() {
        return nio;
    }

    public FiveProps getProps() {
        return props;
    }

    /**
     * This will pull out any "structured" props, but also keep the object
     * around for unstructured props for use by specific strategies within
     * the ServerThread object (notably the IoInit abstraction).
     */
    public void setProps(FiveProps props) {
        if (props == null) {
            return;
        }

        this.props = props;
        nio = props.getBoolean("nio.enabled", false);
    }
}
