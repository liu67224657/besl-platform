/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.service.service.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public interface UserPropsService {
    /**
     * getting
     *
     * @param key
     * @return
     * @throws UserPropsServiceException
     */
    public UserProperty getUserProperty(UserPropKey key) throws ServiceException;

    /**
     * queryUserProperties by the key without the index value.
     *
     * @param key
     * @return
     * @throws ServiceException
     */
    public List<UserProperty> queryUserProperties(UserPropKey key) throws ServiceException;

    /**
     * queryUserPropertiesMap by the key without the index value.
     *
     * @param key
     * @return
     * @throws ServiceException
     */
    public Map<Integer, UserProperty> queryUserPropertiesMap(UserPropKey key) throws ServiceException;

    /**
     * setting
     *
     * @param userProp
     * @throws ServiceException
     */
    public boolean setUserProperty(UserProperty userProp) throws ServiceException;

    /**
     * increase the property values;
     *
     * @param key
     * @param in
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     *
     */
    public UserProperty increaseUserProperty(UserPropKey key, long in) throws ServiceException;

    public boolean modifyUserProperty(UserProperty userProperty) throws ServiceException;

    /**
     * deleting
     *
     * @param key
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     *
     */
    public boolean deleteUserProperty(UserPropKey key) throws ServiceException;
}
