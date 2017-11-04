/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceLoad;

/**
 * Abstract interface to represent an object to be used by services
 * wanting to register with the naming service. Implementations will
 * most likely contain threads.
 */
public interface Registrant extends RegistrantPriv {
    /**
     * Initialize the object with the registration info.
     *
     * @param serviceInfo The info which describes the service that wishes
     *                    to register.
     */
    public void init(ServiceInfo serviceInfo);

    /**
     * Set the ping interval in seconds. Normally, this method need
     * not be called since an appropriate default will be used and
     * since it depends on what the naming server is doing as well.
     *
     * @param secs The ping interval in seconds.
     */
    public void setPingInterval(int secs);

    /**
     * Set the wait time before reconnects in case there was a
     * disconnect from the naming service.
     *
     * @param secs The wait time in seconds.
     */
    public void setWaitBetweenReconnects(int secs);

    /**
     * Retrieve the registration info.
     */
    public ServiceInfo getServiceInfo();

    /**
     * Register a LoadListener to use for retrieving load information
     * from a service.
     */
    public void setLoadListener(LoadListener l);

    /**
     * Update the load for this service.
     */
    public void updateLoad(ServiceLoad serviceLoad);

    /**
     * The listener for getting back load info from a server. A service
     * can optionally set one of these listener objects for a service.
     * When the ping thread pings the naming service, it will send up
     * a ServiceLoad object to the naming service. The naming service
     * will then have load knowledege for a service.
     */
    public static interface LoadListener {
        /**
         * This method is invoked by the ping thread in order to retrieve
         * a load object to send up to the naming service.
         */
        public ServiceLoad get();
    }
}
