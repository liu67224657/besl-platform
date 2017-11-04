/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.service.service.load.LoadMonitorDefFactory;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thread.DiePoolThread;

/*
* The base class for request processors. Abstract class, needs the
* logicProcess method to be implemented.
*/
public class ReqProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(ReqProcessor.class);
	
    //
    private int timeoutTime;
    private int connDelayTime;

    //
    private EventConfig eventConfig = null;
    private Greeter greeter = null;
    private ConnChooser connChooser = null;
    private ProcessStrategy processStrategy;

    //the connect estiblish thread.
    private ConnectThread connectThread = null;
    private Request connectThreadRequest = new Request();

    //
    private LoadMonitorDefFactory loadMonitorFactory = new LoadMonitorDefFactory();
    private PerformanceDataCollector performanceDataCollector = null;
    private TransProfileContainer transProfileContainer = null;
    private Refresher statsRefresher;
    private ConnLoadChecker connLoadChecker;

    private int connTimeoutTime = 30 * 1000;

    /**
     * Ctor the object with the passed in args.
     *
     * @param chooser  A ConnChooser object must be specified
     *                 in order to figure out how to get a connection over which to
     *                 communicate.
     * @param strategy A logicProcess strategy defining how the
     *                 request will be handled.
     * @param timeout  The timeout in msecs for transactions that
     *                 go over the wire.
     */
    public ReqProcessor(ConnChooser chooser, ProcessStrategy strategy, int timeout) {
        setTimeout(timeout);
        setConnChooser(chooser);

        processStrategy = strategy;
    }

    /**
     * Ctor a req processor with a default timeout and a null
     * ServiceFinder object.
     */
    public ReqProcessor(ConnChooser chooser, ProcessStrategy processStrategy) {
        this(chooser, processStrategy, 30 * 1000);
    }

    public ReqProcessor(ConnChooser chooser) {
        this(chooser, new ProcessStrategyStandard(), 30 * 1000);
    }

    /**
     * Use a "standard" conn load checker to throttle back. It
     * uses default settings to throttle back as necessary.
     */
    public void setStdConnLoadChecker() {
        setConnLoadChecker(new ConnLoadCheckerStd(5000, new WaitTimeRandom(2, 10, 120 * 1000)));
    }

    public void setConnLoadChecker(ConnLoadChecker checker) {
        if (connChooser != null) {
            connChooser.setConnLoadChecker(checker);
        }

        connLoadChecker = checker;
    }

    public RPacket process(Request req) throws ServiceException {
        //
        if (processStrategy == null) {
            throw new IllegalStateException("ReqProcessor must be ctor'ed with a non-null ProcessStrategy object!");
        }

        if (connChooser == null) {
            throw new IllegalStateException("ReqProcessor must be ctor'ed with a non-null ConnChooser object!");
        }

        //process the request.
        long t1 = System.currentTimeMillis();
        RPacket rp = processStrategy.process(req, connChooser);

        //
        if (transProfileContainer != null && req.isBlocking()) {
            TransProfile tp = transProfileContainer.getNotNull(req.getUsableMetricsType());
            performanceDataCollector.incTrans(tp, (int) (System.currentTimeMillis() - t1));

            if (statsRefresher.shouldRefresh()) {
                String displays[] = performanceDataCollector.getDisplay("BESL STATS:" + getId());

                for (int i = 0; i < displays.length; i++) {
                	logger.info(displays[i]);
                }
            }
        }

        //
        return rp;
    }

    public void setLoadMonitorFactory(LoadMonitorDefFactory factory) {
        loadMonitorFactory = factory;

        if (connChooser != null) {
            connChooser.setLoadMonitorFactory(factory);
        }
    }

    public LoadMonitorDefFactory getLoadMonitorFactory() {
        return loadMonitorFactory;
    }

    /**
     * Set the timeout (msecs) for processing requests.
     */
    public void setTimeout(int timeout) {
        timeoutTime = timeout;
    }

    /**
     * Retrieve the default timeout used for processing requests.
     */
    public int getTimeout() {
        return timeoutTime;
    }

    /**
     * Returns the timeout that will be used given a particular Request.
     */
    public int getTimeout(Request req) {
        if (req == null) {
            throw new IllegalArgumentException("ReqProcess.getTimeout: req is null!");
        }

        return req.isTimeoutSet() ? req.getTimeout() : timeoutTime;
    }

    /**
     * Set a greeter object to use.
     */
    public void setGreeter(Greeter greeter) {
        this.greeter = greeter;

        //--
        // Forward to the ConnChooser since that's who creates the conns.
        //--
        if (connChooser != null) {
            connChooser.setGreeter(greeter);
        }
    }

    /**
     * Set a ConnChooser object, it is assumed that the
     * service finder has been set in the chooser as well.
     */
    public void setConnChooser(ConnChooser chooser) {
        connChooser = chooser;
        connChooser.setEventCfg(eventConfig);
        connChooser.setGreeter(greeter);
        connChooser.setLoadMonitorFactory(loadMonitorFactory);
        connChooser.setConnTimeout(connTimeoutTime);

        if (connLoadChecker != null) {
            connChooser.setConnLoadChecker(connLoadChecker);
        }
    }

    /**
     * Set the timeout for making a connection.
     */
    public void setConnTimeout(int timeout) {
        connTimeoutTime = timeout;
        if (connChooser != null) {
            connChooser.setConnTimeout(connTimeoutTime);
        }
    }

    /**
     * Ask if this service is up. In this implementation, it means
     * that we have at least one possible connection to a server
     * implementing  this service. The connection will be made.
     */
    public boolean isAvailable() {
        //--
        // Just ask for a service conn. If we do get one then
        // we know that we have at least one avaialble. The
        // input request object doesn't matter.
        //--
        ServiceConn sconn = null;
        try {
            sconn = connChooser.get(connectThreadRequest);
        } catch (ServiceException e) {
            //
        }

        return sconn != null;
    }

    /**
     * Makes a connection to any and all servers specified by
     * this service type.
     */
    private void checkAllConnections() {
        logger.info("Checking all connections");
        //--
        // By asking the conn chooser for all connections, we will
        // establish the conns.
        //--
        connChooser.getAll(connectThreadRequest);
    }

    /**
     * Perform a connect to the service. Returns true if we could connect
     * successfully, false otherwise.
     */
    public boolean connect() {
        return isAvailable();
    }

    /**
     * Set up for events. When a conn is established, a thread
     * that listens for events will be fired up. The events will be
     * forwarded to the EventListener in EventConfig.
     */
    public void setEventCfg(EventConfig eventConfig) {
        this.eventConfig = eventConfig;

        if (connChooser != null) {
            connChooser.setEventCfg(eventConfig);
        }
    }

    public void setConnThreadRequest(Request req) {
        connectThreadRequest = req;
    }

    /**
     * Most services establish their connections on demand. However,
     * some services want to do it right away because they
     * receive events. Indeed, some don't send out any packets at
     * all and simply receive events. For those services, establishing
     * the connection, and continual retrying if it's down, is necessary.
     * <p/>
     * This method is used to fire up a thread that tries to maintain
     * a connection at all times.
     *
     * @param connDelay The delay in msecs to wait between connection
     *                  attempts. This should be at least 10 secs, in fact, it will
     *                  reset it to at least 10 seconds.
     */
    public void useConnThread(int connDelay) {
        useConnThread(connDelay, false);
    }

    /**
     * Setup a thread that maintains a connection to the service.
     *
     * @param connDelay    The delay in msecs between checking to make
     *                     sure a connection has been established.
     * @param connectToAll If true, it will establish a connection to all
     *                     the underlying servers; if false, it will only check to see that
     *                     it can establish the connection to one of them.
     */
    public void useConnThread(int connDelay, boolean connectToAll) {
        connDelayTime = connDelay;
        if (connDelayTime < 10 * 1000) {
            connDelayTime = 10 * 1000;
        }

        if (connectThread == null || connectThread.shouldDie()) { // start if it hasn't, respawn if it is dead already
            connectThread = new ConnectThread(connectToAll);
            connectThread.start();
        }
    }

    /**
     * Shut this service down.
     */
    public void close() {
        if (connChooser != null) {
            connChooser.close();
        }

        if (connectThread != null) {
            connectThread.die();
        }
    }

    public String getId() {
        return connChooser == null ? "UNKNOWN" : connChooser.getId();
    }

    protected EventListener getEventListener() {
        if (eventConfig == null) {
            return null;
        }

        return eventConfig.getListener();
    }

    private class ConnectThread extends DiePoolThread {
        private boolean connectToAll;

        ConnectThread(boolean connectToAll) {
            this.connectToAll = connectToAll;
            
            setName("ConnectThread:" + getId() + ":" + hashCode());
        }

        public void run() {
            while (!shouldDie()) {
                try {
                    if (connectToAll) {
                        checkAllConnections();
                    } else {
                        isAvailable();
                    }
                } catch (Exception e) {
                    GAlerter.lab("ReqProcessor.ConnectThread: exc = " + e, e);
                } catch (Error err) {
                    GAlerter.lab("ReqProcessor.ConnectThread: err = " + err, err);
                }

                Utility.sleep(connDelayTime);
            }

            logger.info("ConnThread exiting!");
        }
    }

    public ConnChooser getConnChooser() {
        return connChooser;
    }

    /**
     * Enable the collection of transaction profile times.
     *
     * @param transProfileContainer A container of TransProfile
     *                              objects so we can collect stats.
     */
    public void setTransProfileContainer(TransProfileContainer transProfileContainer) {
        this.transProfileContainer = transProfileContainer;
        this.performanceDataCollector = new PerformanceDataCollector();
        this.statsRefresher = new Refresher(5 * 60 * 1000);
    }
}
