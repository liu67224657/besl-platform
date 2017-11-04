/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A PacketHandler implementation that processes the packet using a pool of request threads.
 */
public class PacketHandlerReqThread extends PacketHandlerDefault {
    private RequestHandler requestHandler = null;

    private Refresher refresher = new Refresher(10 * 1000);

    /**
     * Construct this object with a PacketProcessor.  The PacketProcessor
     * represents the business logic that needs to be done on the packet.
     *
     * @param processor  A PacketProcessor to use for this PacketHandler.
     * @param reqHandler A class managing the pool of RequestThreads.
     */
    public PacketHandlerReqThread(PacketProcessor processor, RequestHandler reqHandler) {
        super(processor);
        this.requestHandler = reqHandler;
    }

    /**
     * Handles a packet.  This implementation uses a RequestHandler
     * abstraction to logicProcess a packet. These abstractions typically
     * handle packets using a separate set of threads.
     *
     * @param bt The ConnThreadBase this packet came in on.
     * @param rp The packet to handle.
     * @return null Since packet handling is another thread, a null
     *         is always returned.
     */
    public WPacketBase handle(ConnThreadBase bt, RPacketBase rp) {
        //--
        // Call the super function.  Note that we are getting the
        // WPacket ourselves, so we ignore the return value from
        // our parent class.
        //--
        super.handle(bt, rp);

        //--------------------------------------------------------
        // Check to see if we are overloaded. If we are,
        // we return an exception saying so.
        //--------------------------------------------------------
        if (requestHandler.isOverloaded()) {
            WPacket wp = new WPacket();

            wp.setTid(((RPacket) rp).getTid());
            wp.writeByteNx(ServiceConstants.NOTOK);
            
            ServiceException se = new ServiceException(ServiceException.OVERLOADED);
            wp.writeSerializable(se);

            if (refresher.shouldRefresh()) {
                GAlerter.lab("Server is Overload.");
            }
            
            return wp;
        }

        //--
        // Forward to the RequestHandler.
        //--
        requestHandler.handle(new ProcRequestPack(bt, packetProcessor, rp));

        //
        return null;
    }
}
