/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * An implementation of ServiceLoad that is intended as a replacement for
 * the null object.
 */
public class ServiceLoadNull implements ServiceLoad {
    public boolean isOverloaded() {
        return false;
    }

    public boolean canAcceptMoreLoad() {
        return true;
    }

    public int getCurLoad() {
        return 0;
    }

    public int getMaxLoad() {
        return 0;
    }

    /**
     * This supports the notion that load monitoring is not in use
     * without having to deal with a null object.
     */
    public boolean isInUse() {
        return false;
    }
}
