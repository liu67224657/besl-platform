/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.List;

/**
 * Interface to be implemented by objects that will find the ip/port
 * of a service.
 */
public interface ServiceFinder {
    /**
     * Return a container of ServiceData objects that can
     * satisfy requests.
     *
     * @param reallyNeedSome If true, it means the caller really
     *                       needs to get a hold of some objects, since all the ones
     *                       it knows about are dead/unusable.
     */
    public List<ServiceData> getServiceData(boolean reallyNeedSome);

    /**
     * Return a string identifying the kind of service we are looking
     * for. This is for diagnostic purposes.
     */
    public String getId();

    /**
     * Called to shut down any persistent resources used by this object.
     */
    public void close();
}
