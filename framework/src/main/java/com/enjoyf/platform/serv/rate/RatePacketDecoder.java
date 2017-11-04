package com.enjoyf.platform.serv.rate;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.rate.Rate;
import com.enjoyf.platform.service.rate.RateConstants;
import com.enjoyf.platform.service.rate.RateLimit;
import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Yin Pengyi
 */
class RatePacketDecoder extends PacketDecoder {
    private RateLogic logic;

    public RatePacketDecoder(RateLogic logic) {
        this.logic = logic;
        setTransContainer(RateConstants.getTransContainer());
    }

    protected WPacket logicProcess(ConnThreadBase conn, RPacket rp) throws ServiceException {
        byte type = rp.getType();
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        Rate rate = null;
        RateKey key = null;
        RateLimit limit = null;

        switch (type) {
            case RateConstants.GET_RATE:
                limit = (RateLimit) rp.readSerializable();
                key = (RateKey) rp.readSerializable();

                rate = logic.getRate(limit, key);

                wp.writeSerializable(rate);
                break;

            case RateConstants.RECORD_COUNT:
                limit = (RateLimit) rp.readSerializable();
                key = (RateKey) rp.readSerializable();

                rate = logic.recordCount(limit, key);

                wp.writeSerializable(rate);
                break;

            default:
            	GAlerter.lab("PacketDecoder.p_process: Unrecognized type: ", String.valueOf(type));
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
