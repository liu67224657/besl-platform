/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.thread.RequestThread;
import com.enjoyf.platform.util.thread.RequestThreadManager;

/**
 * This is the old-school way of handling requests. The request
 * is handed off to a pool of request threads, but if there are no
 * threads available, the call blocks until a thread becomes available.
 */
public class RequestHandlerStd implements RequestHandler {
    private RequestThreadManager requestThreadManager;

    public RequestHandlerStd(RequestThreadManager manager) {
        requestThreadManager = manager;
    }

    public boolean isOverloaded() {
        return false;
    }

    public void handle(ProcRequestPack request) {
        RequestThread rt = requestThreadManager.getThreadBlock();
        rt.process(request);
    }
}
