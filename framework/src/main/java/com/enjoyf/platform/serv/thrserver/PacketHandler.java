/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;

/**
 * A handler interface for incoming packets.  The handle() call should
 * be used take care of anything that needs to be done on a packet after it
 * has been received.  This is different from PacketProcessor, which represents
 * the business logic that is done on the information inside the packet.
 * PacketHandler is a container for a PacketProcessor, among other things.
 */
public interface PacketHandler {
    /**
     * Handles a packet. If the function returns a non-null
     * WPacket object, then this object should be sent out as
     * a response.
     *
     * @param bt The ConnThreadBase this packet came in on.
     * @param rp The packet to handle.
     * @return A response packet, if one is needed for this
     *         particular packet.  May be null.
     */
    public WPacketBase handle(ConnThreadBase bt, RPacketBase rp);

    /**
     * Sets the PacketProcessor to use for this handler.
     *
     * @param processor The PacketProcessor associated with this handler.
     */
    public void setPacketProcessor(PacketProcessor processor);
}
