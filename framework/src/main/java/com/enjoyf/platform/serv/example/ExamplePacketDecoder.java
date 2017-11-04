/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.example.ExampleConstants;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ExamplePacketDecoder extends PacketDecoder {
    //
    private ExampleLogic processLogic = null;

    //
    public ExamplePacketDecoder(ExampleLogic logic) {
        processLogic = logic;

        this.setTransContainer(ExampleConstants.getTransContainer());
    }

    /**
     * This function should be implemented to call the server's logic.
     */
    @Override
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            //
            case ExampleConstants.CREATE:
                wp.writeSerializable(processLogic.create((Example) rPacket.readSerializable()));
                break;

            case ExampleConstants.GET:
                wp.writeSerializable(processLogic.get((QueryExpress) rPacket.readSerializable()));
                break;

            case ExampleConstants.QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.query((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            case ExampleConstants.QUERY_BY_RANGE:
                wp.writeSerializable(processLogic.query((QueryExpress) rPacket.readSerializable(), (Rangination) rPacket.readSerializable()));
                break;

            case ExampleConstants.MODIFY:
                wp.writeSerializable(processLogic.modify((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            default:
                GAlerter.lab("ExamplePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}