/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.RPacket;

public class GreeterId extends GreeterDefault {
    private int oldServerId = 0;
    private int newServerId = 0;

    public GreeterId(GreetInfo greetInfo) {
        super(greetInfo);
    }

    public int getOldServerId() {
        return oldServerId;
    }

    public int getNewServerId() {
        return newServerId;
    }

    protected void p_processHelloReturn(RPacket rp) {
        int id = rp.readIntNx();

        oldServerId = newServerId;
        newServerId = id;
    }
}
