/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

//
public interface RequestHandler {
    //
    public boolean isOverloaded();

    //
    public void handle(ProcRequestPack request);
}
