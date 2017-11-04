/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * A class that describes the load on a service in an abstract way.
 */
public interface ServiceLoad extends java.io.Serializable {
    /**
     * Ask if the object is overloaded.
     */
    public boolean isOverloaded();

    /**
     * Ask if the object can accept more loaded.
     */
    public boolean canAcceptMoreLoad();

    /**
     * Return the current load as an integer. This is just some arbitrary
     * value defined by the implementing object.
     */
    public int getCurLoad();

    /**
     * Return the max load as an integer. This is just some arbitrary
     * value defined by the implementing object.
     */
    public int getMaxLoad();

    /**
     * This supports the notion that load monitoring is not in use
     * without having to deal with a null object.
     */
    public boolean isInUse();
}
