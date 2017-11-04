/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Interface used by ConnLoadChecker implementations.
 */
public interface WaitTime {
    public long getNextWaitTime(long curWaitTime);
}
