/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.webapps.image.serv;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.image.ImageConstants;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class ImageServerPacketDecoder extends PacketDecoder {
    private ImageServerLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    ImageServerPacketDecoder(ImageServerLogic logic) {
        processLogic = logic;
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        //
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        //
        switch (type) {
            case ImageConstants.RESOURCE_FILE_REMOVE:
                //
                wp.writeBooleanNx(processLogic.removeResourceFile(rPacket.readString()));

                break;
            default:
                GAlerter.lab("ImageServerPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        //
        return wp;
    }
}
