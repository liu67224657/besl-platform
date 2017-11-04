/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.mail.MailConstants;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Yin Pengyi
 */
class MailPacketDecoder extends PacketDecoder {
    private MailLogic processLogic;

    /**
     * Constructor takes the MailLogic object that we're going to use
     * to process the packets.
     *
     * @param logic our logical friend
     */

    MailPacketDecoder(MailLogic logic) {
        processLogic = logic;
        setTransContainer(MailConstants.getTransContainer());
    }

    /**
     * Called when a packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */

    protected WPacket logicProcess(ConnThreadBase conn, RPacket rp)
            throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        byte type = rp.getType();

        MailMessage msg;
        int delay;

        switch (type) {
            case MailConstants.SEND:
                msg = (MailMessage) rp.readSerializable();
                processLogic.send(msg);
                break;
            case MailConstants.SET_DELAY:
                delay = rp.readIntNx();
                processLogic.setSendRate(delay);
                break;
            default:
                GAlerter.lab("PacketDecoder.p_process: Unrecognized type: ", String.valueOf(type));
                throw new ServiceException(ServiceException.BAD_PACKET);
        }
        return wp;
    }
}
