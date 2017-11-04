/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.util.FiveProps;

/**
 * This class is used to configure a set of services that are
 * logically related (via a server props for example). While most
 * logical services expose one port to listen for transactions on, some
 * expose more than one (typically 2 max).
 * <p/>
 * In prod, a jvm would be configured with one of these objects. In a dev
 * environment N of these objects can be used within one jvm if using the
 * serv.container package to contain multiple services.
 */
public class ServiceConfig {
    private boolean etricsEnabledFlag = true;
    /**
     * A List of ServerWrap objects.
     */
    private List<ServerWrap> serviceWrapList = new ArrayList<ServerWrap>();

    /**
     * This is used to register a service with the NS, but no actual
     * service object is fired up to listen for transactions. It is used
     * so that we know that a particular service has started up.
     */
    public ServiceConfig(FiveProps props, String servicePrefix) {
        //--
        // Adds a Registrant, but no service (ie, doesn't listen
        // for transactions.
        //--
        serviceWrapList.add(new ServerWrap(servicePrefix, props));
    }

    /**
     * This is used to register a service with the NS, as well as
     * firing up a ServerThread instance to logicProcess transactions.
     */
    public ServiceConfig(FiveProps props, String servicePrefix, ServerThread server) {
        serviceWrapList.add(new ServerWrap(server, servicePrefix, props));
    }

    /**
     * Use this to configure one logical server.
     *
     * @param serverWrap This object can contain info that allows one to
     *                   a) register a service with the NS, b) fire up a ServerThread object
     *                   for processing transactions. Either is optional, but when both
     *                   are specified we need to manage them together. This is used when
     *                   creating these object by hand (as opposed to from prop files).
     */
    public ServiceConfig(ServerWrap serverWrap) {
        serviceWrapList.add(serverWrap);
    }

    /**
     * Add a ServerWrap object to manage.
     */
    public void addServerWrap(ServerWrap serverWrap) {
        serviceWrapList.add(serverWrap);
    }

    public List<ServerWrap> getServiceWraps() {
        return serviceWrapList;
    }

    boolean areMetricsEnabled() {
        return true;
    }

    /**
     * Set whether we want to enable esm metrics for these services.
     */
    public void setMetricsEnabled(boolean metricsEnabled) {
        etricsEnabledFlag = metricsEnabled;
    }
}
