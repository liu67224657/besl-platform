/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.BillingConstants;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;

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
class BillingPacketDecoder extends PacketDecoder {
    private BillingLogic logic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    BillingPacketDecoder(BillingLogic logic) {
        this.logic = logic;

        setTransContainer(BillingConstants.getTransContainer());
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
            case BillingConstants.DEPOSIT_LOG_CREATE:
                wp.writeSerializable(logic.createDepositLog((DepositLog) rPacket.readSerializable()));

                break;
            case BillingConstants.DEPOSIT_SYNC_MODIFY:
                wp.writeBooleanNx(logic.modifyDepositLog((IntValidStatus) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case BillingConstants.QUERY_DEPOSITLOG_LIST:
                wp.writeSerializable((java.io.Serializable) logic.queryDepositLogQueryExpressPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case BillingConstants.QUERY_BY_SQL:
                wp.writeStringUTF(logic.queryBySql(rPacket.readStringUTF()));
                break;
            case BillingConstants.CHECK_RECEIPT:
                wp.writeBooleanNx(logic.checkReceipt(rPacket.readStringUTF()));
                break;
            case BillingConstants.SET_RECEIPT:
                wp.writeBooleanNx(logic.setReceipt(rPacket.readStringUTF()));
                break;
            default:
                GAlerter.lab("ProfilePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
