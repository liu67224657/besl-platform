/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.shorturl;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrlConstants;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
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
class ShortUrlPacketDecoder extends PacketDecoder {
    private ShortUrlLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    ShortUrlPacketDecoder(ShortUrlLogic logic) {
        processLogic = logic;

        setTransContainer(ShortUrlConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            case ShortUrlConstants.SHORTURL_GET:
                wp.writeSerializable(processLogic.getUrl(rPacket.readString()));

                break;
            case ShortUrlConstants.SHORTURL_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryUrls((Set<String>) rPacket.readSerializable()));

                break;
            case ShortUrlConstants.SHORTURL_GENERATE_ONE:
                wp.writeSerializable(processLogic.generateUrl((String) rPacket.readSerializable(), rPacket.readString()));

                break;
            case ShortUrlConstants.SHORTURL_GENERATE_BATCH:
                wp.writeSerializable((Serializable) processLogic.generateUrls((Set<String>) rPacket.readSerializable(), rPacket.readString()));

                break;
            case ShortUrlConstants.SHORTURL_UPDATE:
                wp.writeSerializable(processLogic.updateShortUrl(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));

                break;

            default:
                GAlerter.lab("ShortUrlPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
