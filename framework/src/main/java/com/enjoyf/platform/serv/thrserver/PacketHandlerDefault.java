/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;

/**
 * A default packetHandler to do set up in.  Shouldn't use this as a full blown implementation,
 * since it doesn't do any real work.
 */
public abstract class PacketHandlerDefault implements PacketHandler {
    protected PacketProcessor packetProcessor = null;

    /**
     * Construct this object with a PacketProcessor.  The PacketProcessor
     * represents the business logic that needs to be done on the packet.
     *
     * @param processor A PacketProcessor to use for this PacketHandler.
     */
    public PacketHandlerDefault(PacketProcessor processor) {
        packetProcessor = processor;
    }

    /**
     * Handles a packet.  The default implementation doesn't do anything except
     * set the ip on the packet from the connection.
     *
     * @param bt The ConnThreadBase this packet came in on.
     * @param rp The packet to handle.
     * @return A response packet, if one is needed for this
     *         particular packet.  May be null.
     */
    public WPacketBase handle(ConnThreadBase bt, RPacketBase rp) {
        if (rp == null) {
            return null;
        }

        if (bt != null) {
            rp.setSource(bt.getIp());
        }

        return null;
    }

    /**
     * Sets the PacketProcessor to use for this handler.
     *
     * @param processor The PacketProcessor associated with this handler.
     */
    public void setPacketProcessor(PacketProcessor processor) {
        packetProcessor = processor;
    }
}
