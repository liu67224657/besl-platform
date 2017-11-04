package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceException;

public interface EventReceiver {
    //recieve the player event
    public boolean receiveEvent(Event event) throws ServiceException;
}
