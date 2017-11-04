/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * A class used to configure the kind of event processing we want
 * to do with a service. Use this when calling ReqProcess.setEventCfg().
 */
public class EventConfig {
    private String eventThreadName;
    private EventListener evnetListener;

    /*
      * This basically consists of two objects, the listener to invoke
      * when an event comes in, and the name of the event thread (for
      * debugging/diagnostic purposes).
      *
      * @param listener	The listener for events.
      * @param eventThreadName	The name to use for the thread.
      */
    public EventConfig(EventListener listener, String threadName) {
        evnetListener = listener;
        eventThreadName = threadName;
    }

    public EventConfig(EventListener listener) {
        this(listener, "EventListener");
    }

    EventListener getListener() {
        return evnetListener;
    }

    String getThreadName() {
        return eventThreadName;
    }
}
