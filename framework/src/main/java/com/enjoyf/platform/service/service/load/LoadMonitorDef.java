/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service.load;

/**
 * A default implementation that does nothing.
 */
public class LoadMonitorDef implements LoadMonitor {
    public boolean isOverloaded() {
        return false;
    }

    public void event() {
    }
}
