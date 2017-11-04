package com.enjoyf.platform.serv.alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.enjoyf.platform.util.log.AlertType;

/**
 * A concrete class for processing alerts in an abstract way. The
 * caller registers N listeners per AlertType and the messages are
 * processed accordingly.
 */
class AlertProcessor {
    /**
     * This is Map(AlertType->List(SenderListener)).
     */
    private Map<AlertType, List<Listener>> alertTypeListenersMap = new HashMap<AlertType, List<Listener>>();

    AlertProcessor() {
        //--
        // Prepopulate the maps with the alert types.
        //--
        for (Iterator<AlertType> itr = AlertType.iterator(); itr.hasNext();) {
            AlertType alertType = itr.next();
            alertTypeListenersMap.put(alertType, new ArrayList<Listener>());
        }
    }

    /**
     * Register a listener for ALL alert types. Ie, if this method
     * is called, all alerts will go this listener regardless of the
     * type.
     */
    void addListener(Listener l) {
        for (Iterator<AlertType> itr = alertTypeListenersMap.keySet().iterator(); itr.hasNext();) {
            List<Listener> list = alertTypeListenersMap.get(itr.next());
            if (list != null) {
                list.add(l);
            }
        }
    }

    /**
     * Register a listener for a specific alert type.
     */
    void addListener(AlertType type, Listener l) {
        List<Listener> list = alertTypeListenersMap.get(type);
        if (list != null) {
            list.add(l);
        }
    }

    /**
     * Run through all the listeners that satisfy this type.
     */
    void process(ServerAlert alert) {

        // look for a listener for this particular alert.
        List<Listener> list1 = alertTypeListenersMap.get(alert.getAlert().getType());
        if (list1 == null) {
            return;
        }

        for (Iterator<Listener> itr = list1.iterator(); itr.hasNext();) {
            Listener listener = itr.next();
            listener.process(alert);
        }
    }

    /**
     * Implement this interface for processing alerts.
     */
    static interface Listener {
        public void process(ServerAlert alert);
    }
}