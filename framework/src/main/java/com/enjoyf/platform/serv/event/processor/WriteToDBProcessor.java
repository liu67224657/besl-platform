package com.enjoyf.platform.serv.event.processor;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.event.EventHandler;
import com.enjoyf.platform.db.event.EventMongoHandler;
import com.enjoyf.platform.props.hotdeploy.EventHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.enjoyf.platform.service.event.user.UserEventTypeConfig;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class WriteToDBProcessor implements UserEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WriteToDBProcessor.class);

    private EventMongoHandler handler;
    private EventHotdeployConfig eventHotdeployConfig;

    public WriteToDBProcessor(EventMongoHandler handler) {
        this.handler = handler;

        //get the config
        eventHotdeployConfig = HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class);
    }

    //
    public void process(UserEvent event) {
        UserEventTypeConfig userEventTypeConfig = eventHotdeployConfig.getUserEventTypeConfig(event.getEventType());

        //the detail logic
        if (userEventTypeConfig != null && userEventTypeConfig.isStoreToDB()) {
            //do the process flow.
            UserEventEntry entry = new UserEventEntry(event.getSrcUno());

            //
            entry.setDestUno(event.getDestUno());

            entry.setEventType(event.getEventType());
            entry.setCount(event.getCount());

            entry.setDescription(event.getDescription());
            entry.setMeta(event.getMeta());

            entry.setEventDate(event.getEventDate());
            entry.setEventIp(event.getEventIp());

            //
            try {
//                entry = handler.insertUserEvent(entry);
                entry = handler.insertUserEventEntry(entry);
            } catch (Exception e) {
                GAlerter.lab("user event logic write event to db error.", entry.toString(), e);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Write the event to db, event:" + event);
            }
        }
    }
}
