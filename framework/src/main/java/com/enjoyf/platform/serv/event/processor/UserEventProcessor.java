package com.enjoyf.platform.serv.event.processor;

import com.enjoyf.platform.service.event.user.UserEvent;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public interface UserEventProcessor {
    //
    public void process(UserEvent event);
}
