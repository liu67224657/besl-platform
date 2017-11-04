/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.thread.RequestThreadManager;

/**
 * A concrete factory class for RequestThreadManager factories.
 */
public class ReqThreadMgrStdFactory extends ReqThreadMgrFactory {
    private int count = 10;

    /**
     * Construct the object.
     *
     * @param n Specifies the number of threads the allocated
     *          objects are going to manage.
     */
    public ReqThreadMgrStdFactory(int n) {
        count = n;
    }

    /**
     * Allocate a request thread manager object.
     */
    public RequestThreadManager allocate() {
        return new RequestThreadManager(count);
    }
}
