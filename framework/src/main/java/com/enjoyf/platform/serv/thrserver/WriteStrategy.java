/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import com.enjoyf.platform.io.WPacketBase;

/**
 * An interface implementing a strategy for writing out packets.
 */
public interface WriteStrategy {
    public void write(ConnThreadBase conn, WPacketBase wp) throws IOException;
}
