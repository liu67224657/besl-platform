/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.thread.RequestThreadManager;

/**
 * Convenience class which creates a java server that processes requests
 * by forwarding them to a RequestThread from a pool.
 */
public class ServerThreadRequestPool extends ServerThreadDefault {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerThreadRequestPool.class);
	
    private int maxReqThreads = 0;
    private String poolTag = "ServerThreadRequestPool";

    /**
     * If -1, fast-fail checking is not enabled.
     */
    private int fastFailTimeoutMillis = 0;
    private int fastFailMaxQueueSize = 0;

    /**
     * Construct the object.
     */
    public ServerThreadRequestPool(String servicePrefix, FiveProps servProps) {
        //--------------------------------------------------------
        // Initialize the number of request threads from
        // the server prop file.
        //--------------------------------------------------------

        maxReqThreads = servProps.getInt("server.maxReqThreads", 20);
        //--
        // See if fast fail is enabled.
        //--
        fastFailTimeoutMillis = servProps.getInt("server.fastFailTimeoutSecs", 0) * 1000;

        fastFailMaxQueueSize = servProps.getInt("server.fastFailMaxQueueSize", 0);

        configurePort(servicePrefix, servProps);

        setWriteStrategy(servProps);
    }

    public ServerThreadRequestPool(int port, int maxThreads) {
        super.setPort(port);
        maxReqThreads = maxThreads;
    }

    public ServerThreadRequestPool(int port, int maxThreads, FiveProps servProps) {
        super.setPort(port);
        maxReqThreads = maxThreads;
        setWriteStrategy(servProps);
    }

    /**
     * Set a string tag to differentiate the thread pool used by this
     * object from other thread pools used by other objects. The tag
     * appears in debug log statements.
     */
    public void setPoolTag(String tag) {
        poolTag = tag;
    }

    /**
     * Retrieve the max request threads.
     */
    public int getMaxReqThreads() {
        return maxReqThreads;
    }

    /**
     * Set the max request threads. Note that by default this value
     * is set from the property in Props.instance().getTenProps().
     */
    public void setMaxReqThreads(int maxThreads) {
        maxReqThreads = maxThreads;
    }

    /**
     * Start this object.
     */
    public void start() {
        super.setConnFactory(p_getConnFactory());
        super.start();
    }

    private ConnThreadBaseFactory p_getConnFactory() {
        //--
        // Instantiate the proper RequestHandler given the config.
        //--
        RequestHandler requestHandler = null;
        if (fastFailTimeoutMillis > 0) {
            requestHandler = new RequestHandlerQueued(maxReqThreads, fastFailTimeoutMillis, fastFailMaxQueueSize, poolTag);
            if (logger.isTraceEnabled()) {
            	logger.trace("ServerThreadRequestPool: Creating a queued request handler: " + requestHandler);
            }
        } else {
            requestHandler = new RequestHandlerStd(new RequestThreadManager(maxReqThreads, poolTag));
            if (logger.isTraceEnabled()) {
            	logger.trace("ServerThreadRequestPool: Creating a std request handler with maxReqThreads=" + maxReqThreads);
            }
        }

        //--
        // Create the connection factory.
        //--
        ConnThreadBaseFactory factory = getConnFactory();
        if (factory == null) {
            PacketHandlerReqThread ph = new PacketHandlerReqThread(getPacketProcessor(), requestHandler);

            factory = new ConnThreadFactoryDefault(getPacketWriter(), getPacketReader(), ph, getWriteStrategy());
        }

        return factory;
    }
}
