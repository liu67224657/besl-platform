/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class UserPropsServiceBeslImpl implements UserPropsService {
    private ReqProcessor reqProcessor = null;

    public UserPropsServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("UserPropsServiceBeslImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    ///////////////////////////////////////////////////////////////

    public UserProperty getUserProperty(UserPropKey key) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(key);

        Request req = new Request(UserPropsConstants.USER_PROPS_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (UserProperty) rPacket.readSerializable();
    }

    public List<UserProperty> queryUserProperties(UserPropKey key) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(key);

        Request req = new Request(UserPropsConstants.USER_PROPS_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<UserProperty>) rPacket.readSerializable();
    }

    public Map<Integer, UserProperty> queryUserPropertiesMap(UserPropKey key) throws ServiceException {
        Map<Integer, UserProperty> m = new HashMap<Integer, UserProperty>();

        List<UserProperty> l = queryUserProperties(key);

        if (!CollectionUtil.isEmpty(l)) {
            for (UserProperty prop : l) {
                m.put(prop.getKey().getIdx(), prop);
            }
        }

        return m;
    }

    public boolean setUserProperty(UserProperty userProp) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(userProp);

        Request req = new Request(UserPropsConstants.USER_PROPS_SET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    public UserProperty increaseUserProperty(UserPropKey key, long in) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(key);
        wPacket.writeLongNx(in);

        Request req = new Request(UserPropsConstants.USER_PROPS_INCREASE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (UserProperty) rPacket.readSerializable();
    }


    @Override
    public boolean modifyUserProperty(UserProperty userProperty) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(userProperty);
        Request req = new Request(UserPropsConstants.USER_PROPS_UPDATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    public boolean deleteUserProperty(UserPropKey key) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(key);

        Request req = new Request(UserPropsConstants.USER_PROPS_DELETE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }
}
