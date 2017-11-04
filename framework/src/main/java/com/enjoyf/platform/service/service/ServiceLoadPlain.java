/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * A class that describes the load on a service with a couple of integers.
 */
public class ServiceLoadPlain implements ServiceLoad {
    private int curLoad;
    private int maxLoad;

    public ServiceLoadPlain(int curLoad, int maxLoad) {
        this.curLoad = curLoad;
        this.maxLoad = maxLoad;
    }

    public void setCurLoad(int curLoad) {
        this.curLoad = curLoad;
    }

    public boolean isOverloaded() {
        return curLoad > maxLoad;
    }

    public boolean canAcceptMoreLoad() {
        return curLoad < maxLoad;
    }

    public int getCurLoad() {
        return curLoad;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    /**
     * Increment the max load by a percentage.
     */
    public void incrementMaxLoad(int percentage) {
        int increment = (int) (((double) percentage) / 100.0 * (double) maxLoad);
        if (increment > 0) {
            maxLoad += increment;
        }
    }

    /**
     * This supports the notion that load monitoring is not in use
     * without having to deal with a null object.
     */
    public boolean isInUse() {
        return maxLoad != 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        ServiceLoadPlain load = (ServiceLoadPlain) obj;
        if (load.maxLoad != this.maxLoad) {
            return false;
        }

        return load.curLoad == this.curLoad;
    }

    public int hashCode() {
        return curLoad + maxLoad;
    }

    public String toString() {
        return "cur=" + curLoad + ":" + "max=" + maxLoad;
    }
}
