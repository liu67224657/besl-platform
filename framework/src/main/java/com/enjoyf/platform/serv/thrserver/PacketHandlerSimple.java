/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;

/**
 * A simple PacketHandler implementation that processes requests on the calling thread.
 * It basically forwards the packet on to the PacketProcessor.
 */
public class PacketHandlerSimple extends PacketHandlerDefault {

    /**
     * Construct this object with a PacketProcessor.  The PacketProcessor
     * represents the business logic that needs to be done on the packet.
     *
     * @param processor A PacketProcessor to use for this PacketHandler.
     */
    public PacketHandlerSimple(PacketProcessor processor) {
        super(processor);
    }

    /**
     * Handles a packet.  This implementation simply forwards the packet to
     * the PacketProcessor on the calling Thread.
     *
     * @param bt The ConnThreadBase this packet came in on.
     * @param rp The packet to handle.
     * @return A response packet, if one is needed for this
     *         particular packet.  May be null.
     */
    public WPacketBase handle(ConnThreadBase bt, RPacketBase rp) {
        // call the super function.  note that we are getting the WPacket ourselves,
        // so we ignore the return value from our parent class
        super.handle(bt, rp);

        if (packetProcessor != null) {
            //--
            // Process the packet and see if we need to return something.
            //--
            WPacketBase wp = packetProcessor.process(bt, rp);
            return wp;
        }

        return null;
    }
}
