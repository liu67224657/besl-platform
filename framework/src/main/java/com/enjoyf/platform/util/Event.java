/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.util.Iterator;
import java.util.Vector;


/**
 * Events are objects that can be dispatched by an EventDispatcher.
 * Implementing classes must call notifyListeners() after the event
 * has been sent successfully.
 */
public abstract class Event {

    public interface Listener {
        public void onEventSent(Event e);

        public void onEventFailed(Event e);
    }

    //the listener of the event
    private Vector<Listener> listeners = new Vector<Listener>();

    public abstract void send();

    public void attachListener(Listener l) {
        listeners.add(l);
    }

    public void detachListener(Listener l) {
        listeners.remove(l);
    }

    protected void notifySent() {
        Vector<Listener> copy = new Vector<Listener>(listeners);

        for (Iterator<Listener> i = copy.iterator(); i.hasNext();) {
            i.next().onEventSent(this);
        }
    }

    protected void notifyFailed() {
        Vector<Listener> copy = new Vector<Listener>(listeners);

        for (Iterator<Listener> i = copy.iterator(); i.hasNext();) {
            i.next().onEventFailed(this);
        }
    }
}
