/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.userprops;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsConstants;
import com.enjoyf.platform.util.log.GAlerter;

import java.io.Serializable;
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
class UserPropsPacketDecoder extends PacketDecoder {
    private UserPropsLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    UserPropsPacketDecoder(UserPropsLogic logic) {
        processLogic = logic;

        setTransContainer(UserPropsConstants.getTransContainer());
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
            case UserPropsConstants.USER_PROPS_GET:
                wp.writeSerializable(processLogic.getUserProperty((UserPropKey) rPacket.readSerializable()));

                break;
            case UserPropsConstants.USER_PROPS_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryUserProperties((UserPropKey) rPacket.readSerializable()));

                break;
            case UserPropsConstants.USER_PROPS_SET:
                wp.writeBooleanNx(processLogic.setUserProperty((UserProperty) rPacket.readSerializable()));

                break;
            case UserPropsConstants.USER_PROPS_INCREASE:
                wp.writeSerializable(processLogic.increaseUserProperty((UserPropKey) rPacket.readSerializable(), rPacket.readLongNx()));

                break;
            case UserPropsConstants.USER_PROPS_UPDATE:
                wp.writeSerializable(processLogic.modifyUserProperty((UserProperty) rPacket.readSerializable()));

                break;

            case UserPropsConstants.USER_PROPS_DELETE:
                wp.writeBooleanNx(processLogic.deleteUserProperty((UserPropKey) rPacket.readSerializable()));

                break;
            default:
                GAlerter.lab("UserPropsPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
