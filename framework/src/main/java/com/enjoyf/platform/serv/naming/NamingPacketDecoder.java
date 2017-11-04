/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.naming.NamingConstants;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.log.GAlerter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;


/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */

class NamingPacketDecoder extends PacketDecoder {
    private Logic logic;

    /**
     * Constructor takes the Logic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    NamingPacketDecoder(Logic logic) {
        this.logic = logic;
        setTransContainer(NamingConstants.getTransContainer());
    }

    /**
     * Called when a packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rp) throws ServiceException {
        byte type = rp.getType();
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        Vector vout = null;
        ClientRegInfo regInfo = null;
        String serviceType;
        ServiceRequest sreq;
        ServiceAddress saddr;
        String key;
        Serializable obj;
        ServiceLoad serviceLoad;
        ServiceRequest[] sreqs = null;
        ArrayList list;

        switch (type) {
            case NamingConstants.REGISTER:
                regInfo = (ClientRegInfo) rp.readSerializable();
                logic.register(conn, regInfo);
                break;
            case NamingConstants.UNREGISTER:
                boolean forRebalance = rp.readBooleanNx();
                logic.unregister(conn, forRebalance);
                break;
            case NamingConstants.PING:
                serviceLoad = (ServiceLoad) rp.readSerializable();
                logic.ping(conn, serviceLoad);
                break;
            case NamingConstants.GET_SERVICE_TYPES:
                vout = logic.getServiceTypes();
                wp.writeSerializable(vout);
                break;
            case NamingConstants.GET_SERVICE_NAMES:
                serviceType = (String) rp.readSerializable();
                vout = logic.getServiceNames(serviceType);
                wp.writeSerializable(vout);
                break;
            case NamingConstants.GET_SERVICE_IDS:
                vout = logic.getServiceIds();
                wp.writeSerializable(vout);
                break;
            case NamingConstants.GET_SERVICE_ADDRESS:
                sreq = (ServiceRequest) rp.readSerializable();
                vout = logic.getServiceAddress(sreq);
                wp.writeIntNx(vout.size());
                Enumeration itr = vout.elements();
                while (itr.hasMoreElements()) {
                    wp.writeString(((ServiceAddress) itr.nextElement()).deconstitute());
                }
                break;
            case NamingConstants.GET_SERVICE_DATA:
                sreq = (ServiceRequest) rp.readSerializable();
                list = logic.getServiceData(sreq);
                wp.writeSerializable(list);
                break;
            case NamingConstants.STORE_OBJECT:
                key = (String) rp.readSerializable();
                obj = rp.readSerializable();
                logic.storeObject(key, obj);
                break;
            case NamingConstants.GET_OBJECT:
                key = (String) rp.readSerializable();
                obj = logic.getObject(key);
                wp.writeSerializable(obj);
                break;
            case NamingConstants.EVENT_REGISTER:
                sreqs = (ServiceRequest[]) rp.readSerializable();
                logic.eventRegister(conn, sreqs);
                break;
            default:
                GAlerter.lab("PacketDecoder.p_process: Unrecognized type: ", String.valueOf(type));
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
