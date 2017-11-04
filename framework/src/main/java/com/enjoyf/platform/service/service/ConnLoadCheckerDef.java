/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * An implementation of ConnLoadChecker that acts as a NOP.
 */
public class ConnLoadCheckerDef implements ConnLoadChecker {
    public boolean isOverloaded(ServiceAddress saddr) {
        return false;
    }

    public void connAttempt(ServiceAddress saddr) {
    }

    public void connSucceeded(ServiceAddress saddr) {
    }
}
