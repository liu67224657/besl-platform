package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.QueuedSender;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.naming.NamingConstants;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.RateLimiter;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThread;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thin.DieThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class manages the events system for the naming server.
 * It gets notified of changes in the registrations via a listener.
 */
class NamingEventManager implements RegistrantContainer.Listener {
    //
    private static final Logger logger = LoggerFactory.getLogger(NamingEventManager.class);

    //
    private int sleepInterval = 60 * 1000;
    private Map<ConnThreadBase, NamingEventClient> namingEventClientsMap = new HashMap<ConnThreadBase, NamingEventClient>();
    private RateLimiter rateLimiter;

    //
    private Set<NamingEventClient> readyForNamingEventClientsSet = new HashSet<NamingEventClient>();
    private QueuedSender queueSender;
    private int startWaitTime;
    private int cutOffTime;
    private long startTime = System.currentTimeMillis();
    private QueueThread namingEventClientQueueThread;

    /**
     * Events are sent out at discrete intervals as opposed to whenever
     * things change. This is to keep activity down during startup/restart
     * situations.
     *
     * @param sleepInterval     How often to send out events.
     * @param rateLimitPeriod   The period in msecs over which
     *                          we measure the rate.
     * @param rateLimitMaxCount The max count during the rate limit period.
     * @param startWaitTime     How long to wait after startup before sending
     *                          events.
     * @param cutOffTime        How long to spend in the event send thread.
     *                          If the time spent exceeds cutOffTime, then the event thread terminates.
     */
    public NamingEventManager(int sleepInterval, int rateLimitPeriod, int rateLimitMaxCount, int startWaitTime, int cutOffTime) {
        this.startWaitTime = startWaitTime;
        this.cutOffTime = cutOffTime;

        //--
        // Establish a rate object that returns the rate of service
        // additions over the past minute.
        //--
        this.rateLimiter = new RateLimiter(rateLimitPeriod, rateLimitMaxCount);
        this.sleepInterval = sleepInterval;
        this.queueSender = new QueuedSender(30 * 1000, 30 * 1000);
        this.namingEventClientQueueThread = new QueueThread(
                new QueueListener() {
                    public void process(Object obj) {
                        processRegistrantEvent((ClientRegInfo) obj);
                    }
                },
                "namingEventClientQueueThread"
        );

        //--
        // Only start up the event thread if we have a non-zero event
        // interval.
        //--
        if (this.sleepInterval > 0) {
            Sender sender = new Sender();
            sender.start();
        }
    }

    int getClientCount() {
        return namingEventClientsMap.size();
    }

    /**
     * Called to inform this object of an interest in receiving events.
     *
     * @param conn The conn corresponding to the client that wants
     *             events.
     * @parm sreqs    The ServiceRequest objects that the client is
     * interested in listening to.
     */
    NamingEventClient eventRegister(ConnThreadBase conn, ServiceRequest[] serviceRequests) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingEventManager eventRegister, conn: " + conn + ", requests:" + Arrays.asList(serviceRequests));
        }

        NamingEventClient namingClient = null;
        synchronized (namingEventClientsMap) {
            namingClient = namingEventClientsMap.get(conn);
            if (namingClient == null) {
                namingClient = new NamingEventClient(conn);

                namingEventClientsMap.put(conn, namingClient);
            }

            namingClient.setServiceRequests(serviceRequests);
        }

        return namingClient;
    }

    /**
     * Called when an event client dies.
     */
    void connDied(ConnThreadBase conn) {
        synchronized (namingEventClientsMap) {
            NamingEventClient eventClient = namingEventClientsMap.remove(conn);

            if (eventClient != null) {
                logger.debug("NamingEventManager: Removing namingEventClient: " + conn);
            }
        }
    }

    /**
     * Called when a registration is added.
     */
    public void add(ClientRegInfo info) {
        //
        if (logger.isDebugEnabled()) {
            logger.debug("NamingEventManager receive info:" + info);
        }

        //--
        // If we don't have an event thread running, then no need to
        // do anything.
        //--
        if (sleepInterval == 0) {
            return;
        }

        //--
        // Let's add this on a separate thread so as not to block the
        // caller.
        //--
        namingEventClientQueueThread.add(info);
    }

    /**
     * Use this to add initial events after a registration.
     */
    void addInitial(NamingEventClient namingEventClient, ClientRegInfo info) {
        synchronized (namingEventClientsMap) {
            if (namingEventClient.addEvent(info.getServiceId())) {
                readyForNamingEventClientsSet.add(namingEventClient);
            }
        }
    }

    private void processRegistrantEvent(ClientRegInfo info) {
        rateLimiter.isLimited(1);

        synchronized (namingEventClientsMap) {
            for (NamingEventClient namingEventClient : namingEventClientsMap.values()) {
                if (namingEventClient.addEvent(info.getServiceId())) {
                    readyForNamingEventClientsSet.add(namingEventClient);
                }
            }
        }
    }

    /**
     * Called when a registration is removed.
     */
    public void remove(ClientRegInfo info) {
        //--
        // Note that what we are registering is changes, so a remove
        // behaves the same as an add.
        //--
        add(info);
    }

    class Sender extends DieThread {
        Sender() {
            setName("Sender:" + getName());
        }

        public void run() {
            if (logger.isDebugEnabled()) {
                logger.debug("NamingEventManager.send thread running at: " + sleepInterval);
            }

            while (!shouldDie()) {
                Utility.sleepExc(sleepInterval);

                if (shouldDie()) {
                    break;
                }

                long startTime = System.currentTimeMillis();

                if (logger.isDebugEnabled()) {
                    logger.debug("NamingEventManager.Sender: Looking for events to send.");
                }

                if (isTooBusy()) {
                    logger.warn("NamingEventManager.Sender: Too busy to send naming events right now.");
                    continue;
                }

                //--
                // Make a copy of the event namingClients so we don't lock up
                // the server.
                //--
                Collection<NamingEventClient> namingEventClients = null;
                synchronized (namingEventClientsMap) {
                    namingEventClients = new ArrayList<NamingEventClient>(readyForNamingEventClientsSet);

                    readyForNamingEventClientsSet.clear();
                }

                //loop each namingeventclient. to send out the events.
                Iterator<NamingEventClient> itr = namingEventClients.iterator();
                while (itr.hasNext()) {
                    NamingEventClient namingEventClient = itr.next();

                    try {
                        sendNamingEvents(namingEventClient);
                    } catch (Exception e) {
                        GAlerter.lab("NamingServer.Sender: UNEXPECTED EXCEPTION: " + e + ". Please report this immediately", e);
                    }

                    if (System.currentTimeMillis() - startTime > cutOffTime) {
                        logger.warn("Stopping event thread, it's taking too long");

                        //--
                        // Since we aborted the thread, we need to put
                        // back any NamingEventClient objects that we have not
                        // sent events to.
                        //--
                        synchronized (namingEventClientsMap) {
                            while (itr.hasNext()) {
                                readyForNamingEventClientsSet.add(itr.next());
                            }
                        }
                        break;
                    }
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("NamingEventManager thread: cycle completed");
                }
            }
        }
    }

    private void sendNamingEvents(NamingEventClient namgingClient) {
        if (!namgingClient.isAlive()) {
            return;
        }

        ServiceRequest[] reqs = namgingClient.getAndClearEvents();
        if (reqs.length == 0) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("NamingEventManager.sendNamingEvents: Sending " + reqs.length + " events to namgingClient: " + namgingClient);
        }

        queueSender.send(new QueuedSender.MessageDef(namgingClient.getConn(), p_getOutPacket(reqs)));
    }

    private WPacket p_getOutPacket(ServiceRequest[] requests) {
        WPacket wp = new WPacket();

        wp.setType(NamingConstants.EV_SERVICE_CHANGE);
        wp.writeSerializable(requests);

        return wp;
    }

    private boolean isTooBusy() {
        if (System.currentTimeMillis() - startTime < startWaitTime) {
            logger.warn("TOO BUSY because of start time");
            return true;
        }

        if (rateLimiter.isLimited()) {
            logger.warn("TOO BUSY because of rate");
            return true;
        }

        return false;
    }
}
