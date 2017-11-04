/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

public interface Greeter {
    public void greet(ServiceConn sconn) throws ServiceException;

    public void setSyncher(Syncher syncher);
}
