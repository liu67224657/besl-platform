/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.ServiceType;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:29
 * Description:
 */
public interface EventDispatchService {

    //dispatch the event to all the services which need the event.
    public boolean dispatch(Event event) throws ServiceException;

    //dispatch the event to a selected service.
    public boolean dispatchSelected(Event event, ServiceType serviceType) throws ServiceException;
}
