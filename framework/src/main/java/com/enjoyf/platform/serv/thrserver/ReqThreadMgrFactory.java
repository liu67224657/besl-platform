/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.thread.RequestThreadManager;

public abstract class ReqThreadMgrFactory {
    //
    public abstract RequestThreadManager allocate();
}
