/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.userprops;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.userprops.UserPropsHandler;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsService;
import com.enjoyf.platform.service.userprops.UserPropsServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The UserPropsLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * UserPropsLogic is called by UserPropsPacketDecoder.
 */
class UserPropsLogic implements UserPropsService {
    //
    private static final Logger logger = LoggerFactory.getLogger(UserPropsLogic.class);

    //
    private Map<UserPropDomain, UserPropsHandler> handlerMap = new HashMap<UserPropDomain, UserPropsHandler>();

    private UserPropsConfig config;

    UserPropsLogic(UserPropsConfig cfg) {
        config = cfg;

        //check the config.
        if (CollectionUtil.isEmpty(config.getDataSourceNameMap())) {
            GAlerter.lab("There isn't database connection pool in the UserPropsConfig.");

            //sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        } else {
            //initialize the db handler
            try {
                //initialize the userprops handlers
                for (UserPropDomain domain : config.getDataSourceNameMap().keySet()) {
                    String dsn = config.getDataSourceNameMap().get(domain);

                    //create the handler and put it into the map.
                    handlerMap.put(domain, new UserPropsHandler(dsn, config.getProps()));
                }
            } catch (DbException e) {
                GAlerter.lab("Initialize the user props handler error.", e);

                //sleep 5 seconds for the system to send out the alert.
                Utility.sleep(5000);
                System.exit(0);
            }
        }
    }

    private UserPropsHandler getUserPropsHandler(UserPropDomain domain) throws UserPropsServiceException {
        UserPropsHandler handler = handlerMap.get(domain);

        if (handler == null) {
            throw new UserPropsServiceException(UserPropsServiceException.NO_DB_HANDLER);
        }

        return handler;
    }


    public UserProperty getUserProperty(UserPropKey key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to get user props, key:" + key);
        }

        UserProperty up = getUserPropsHandler(key.getDomain()).getUserProps(key);

        if (up == null) {
            up = new UserProperty();

            up.setUserPropKey(key);
            up.setValue("");
        }

        return up;
    }

    public List<UserProperty> queryUserProperties(UserPropKey key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to query user props, key:" + key);
        }

        return getUserPropsHandler(key.getDomain()).queryUserProps(key);
    }

    public Map<Integer, UserProperty> queryUserPropertiesMap(UserPropKey key) throws ServiceException {
        //nothing to do.

        return null;
    }

    public boolean setUserProperty(UserProperty userProp) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to set user props, userProp:" + userProp);
        }

        return getUserPropsHandler(userProp.getKey().getDomain()).setUserProps(userProp);
    }

    public UserProperty increaseUserProperty(UserPropKey key, long in) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to increase user props, key:" + key + ", increase value:" + in);
        }

        UserProperty up = getUserPropsHandler(key.getDomain()).increaseUserProps(key, in, config.getIncreaseMaxTryTimes());

        if (up == null) {
            throw new UserPropsServiceException(UserPropsServiceException.INCREASE_FAILED);
        }

        return up;
    }

    public boolean modifyUserProperty(UserProperty property) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to increase user props, property:" + property);
        }

        return getUserPropsHandler(property.getKey().getDomain()).updateUserProperty(property);
    }


    public boolean deleteUserProperty(UserPropKey key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to remove user props, key:" + key);
        }

        return getUserPropsHandler(key.getDomain()).deleteUserProps(key);
    }
}
