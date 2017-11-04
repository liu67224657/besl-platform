/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;

/**
 * Interface for classes that want to logicProcess messages received.
 */
public interface PacketProcessor {
    /**
     * Process a packet. If the function returns a non-null,
     * WPacket object, then this object should be sent out as
     * a response.
     */
    public WPacketBase process(ConnThreadBase bt, RPacketBase rp);
}
