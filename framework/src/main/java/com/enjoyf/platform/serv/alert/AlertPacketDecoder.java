package com.enjoyf.platform.serv.alert;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.alert.AlertConstants;
import com.enjoyf.platform.service.service.HelloInfo;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.Alert;
import com.enjoyf.platform.util.log.GAlerter;


/**
 * This interface receives packets from the remote processes. It
 * translates them into method calls which may call further into
 * the business logic.
 * <p/>
 * The process() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class AlertPacketDecoder extends PacketDecoder {
	
    private Logic logic;

    /**
     * Constructor takes the Logic object that we're going to use
     * to process the packets.
     *
     * @param logic our logical friend
     */
    AlertPacketDecoder(Logic logic) {
        this.logic = logic;
        setTransContainer(AlertConstants.getTransContainer());
    }

    /**
     * Called when a packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rp) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        byte type = rp.getType();

        switch (type) {
            case ServiceConstants.HELLO:
                logic.hello(conn, (HelloInfo) rp.readSerializable());
                
                break;
            case AlertConstants.REPORT:
                logic.log(conn, (Alert) rp.readSerializable());
                
                break;
            default:
                GAlerter.lab("AlertPacketDecoder.p_process: Unrecognized type: ", String.valueOf(type));
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }

}
