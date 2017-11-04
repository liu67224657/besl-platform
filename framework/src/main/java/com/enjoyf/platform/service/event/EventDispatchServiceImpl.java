package com.enjoyf.platform.service.event;

import com.enjoyf.platform.props.hotdeploy.EventHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.ServiceType;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventTypeConfig;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventTypeConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.collection.QueueList;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class EventDispatchServiceImpl implements EventDispatchService {
    //
    private static final Logger logger = LoggerFactory.getLogger(EventDispatchServiceImpl.class);

    private QueueThreadN eventDispatchQueueThreadN = null;

    EventDispatchServiceImpl() {
        eventDispatchQueueThreadN = new QueueThreadN(8, new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                }
            }
        }, new QueueList());
    }

    private boolean processQueuedEvent(Event event) {
        boolean result = true;

        //
        if (event instanceof UserEvent) {
            UserEventTypeConfig config = HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class).getUserEventTypeConfig(((UserEvent) event).getEventType());
            if (config != null) {
                if (!CollectionUtil.isEmpty(config.getReceivers())) {
                    for (EventReceiver receiver : config.getReceivers().values()) {
                        if (logger.isTraceEnabled()) {
                            logger.trace("Dispatch user event to Receiver:" + event);
                        }

                        result = dispatch(receiver, event) && result;
                    }
                } else {
                    result = false;
                    logger.warn("No Receiver For the User Event Type:" + event);
                }
            } else {
                result = false;
                GAlerter.lan("Not Support For the User Event Type:" + event);
            }
        } else if (event instanceof SystemEvent) {
            SystemEventTypeConfig config = HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class).getSystemEventTypeConfig(((SystemEvent) event).getEventType());
            if (config != null) {
                if (!CollectionUtil.isEmpty(config.getReceivers())) {
                    for (EventReceiver receiver : config.getReceivers().values()) {
                        if (logger.isTraceEnabled()) {
                            logger.trace("Dispatch system event to Receiver:" + event);
                        }

                        result = dispatch(receiver, event) && result;
                    }
                } else {
                    result = false;
                    logger.warn("No Receiver For the System Event Type:" + event);
                }
            } else {
                result = false;
                GAlerter.lan("Not Support For the System Event Type:" + event);
            }
        }else if (event instanceof PageViewEvent) {
            try {
                EventServiceSngl.get().reportPageViewEvent((PageViewEvent) event);
            } catch (Exception e) {
                //
                GAlerter.lan("Report pageview event error:" + event);
            }
        }else {
            logger.warn("Unknown event object:" + event);
        }

        return result;
    }

    public boolean dispatch(Event event) throws ServiceException {
        boolean result = true;

        if (event == null) {
            return result;
        }

        eventDispatchQueueThreadN.add(event);

        return result;
    }

    public boolean dispatchSelected(Event event, ServiceType serviceType) throws ServiceException {
        boolean result = true;

        if (event == null) {
            return result;
        }

        if (event instanceof UserEvent) {
            if (HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class).isSupport(((UserEvent) event).getEventType())) {
                EventReceiver r = serviceType.getEventReceiver();

                result = r.receiveEvent(event);
            } else {
                result = false;
                GAlerter.lan("Not Support For the User Event Type:" + event);
            }
        } else if (event instanceof SystemEvent) {
            if (HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class).isSupport(((SystemEvent) event).getEventType())) {
                EventReceiver r = serviceType.getEventReceiver();

                result = r.receiveEvent(event);
            } else {
                result = false;
                GAlerter.lan("Not Support For the User Event Type:" + event);
            }
        }

        return result;
    }

    private boolean dispatch(EventReceiver receiver, Event event) {
        try {
            return receiver.receiveEvent(event);
        } catch (Exception e) {
            GAlerter.lab("The EventReceiver receiveEvent error.", e);

            return false;
        }
    }
}