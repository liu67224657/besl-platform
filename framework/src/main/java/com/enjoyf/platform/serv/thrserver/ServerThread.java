/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.service.load.LoadMonitor;
import com.enjoyf.platform.service.service.load.LoadMonitorDef;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A base class class for "Thread-per-connection" servers.
 * The class will listen for connections on a specified port, and
 * fire off a thread when a connection is established. While this
 * class can be used as is, it can also be derived from to add
 * functionality. A fundamental object needed is a ConnThreadBaseFactory
 * object. This factory object will allocate ConnThreadBase objects
 * to use when establishing a connection. There are some standard
 * ConnThread* objects (with associated factories) that can be used,
 * or you can derive your own.
 */
public class ServerThread extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);
	
    /**
     * Note the state of the thread. There are a couple of states,
     * STATE_OK (covers everything) and STATE_DYING, which is when the server
     * has been told to shut down.
     */
    private static final int STATE_OK = 1;
    private static final int STATE_DYING = 2;

    //--
    // Max queue size of clients waiting to connect on the port
    // before the jdk rejects the attempt.
    //-
    private static final int MAX_BLOCKERS = 50;

    private ConnThreadDie connThreadDieListener = new ConnThreadDie();
    private Vector<ConnMadeListener> connMadeContainer = new Vector<ConnMadeListener>(5);
    private Vector<ServerDieListener> serverDieContainer = new Vector<ServerDieListener>(5);
    private Vector<ConnDieListener> connDieContainer = new Vector<ConnDieListener>(5);
    protected ServerSocketWrapper serverSocket;
    protected ServerSocketFactory serverSocketFactory = null;

    private boolean deadFlag = false;
    private boolean startedFlag = false;
    private int serverState = STATE_OK;

    private Hashtable<Integer, ConnThreadBase> connThreads = new Hashtable<Integer, ConnThreadBase>(100);
    private LoadMonitor loadMonitor = new LoadMonitorDef();
    private IoInit ioInit = null;
    private ThreadServerConfig threadServerConfig;

    /**
     * An optional throttle control for accepting connections. Zero
     * (OFF) by default. If set to a positive value N, the server will
     * only handlePlayerEnter the ServerSocket.accept method every N milliseconds.
     * This was added to protect roomservers from the extremely fast
     * ramp-up conditions we see when new challenges are released.
     */
    private long throttleValue = 0L;

    /**
     * Construct a server that is incomplete. At least a connection
     * factory and perhaps a port will need to be set before
     * starting the server.
     */
    public ServerThread() {
        this(7500, null);
    }

    /**
     * This should be the only ctor used. Hopefully we can eliminate the
     * others soon.
     */
    public ServerThread(ThreadServerConfig config) {
        threadServerConfig = config;
    }

    /**
     * Construct a server that listens on the specified port.
     * setConnFactory() must be called to establish a connection
     * factory.
     */
    public ServerThread(int port) {
        this(port, null);
    }

    /**
     * Construct a server that listens on the specified port
     * and uses the connection factory.
     *
     * @param port The port on which to listen for connections.
     * @param cf   The connection factory object. This object
     *             allocates connection objects that are used whenever
     *             a connection is established to a client.
     */
    public ServerThread(int port, ConnThreadBaseFactory cf) {
        this(port, cf, false);
    }


    public ServerThread(int port, ConnThreadBaseFactory cf, boolean useSSL) {
        setName("ServerThread:" + getName());
        threadServerConfig = new ThreadServerConfig(port, cf);
    }

    public void setIp(InetAddress iaddr) {
        threadServerConfig.setIp(iaddr);
    }

    /**
     * Set the port to use for listening for connections.
     */
    public void setPort(int port) {
        threadServerConfig.setPort(port);
    }

    /**
     * Retrieve the port for the listen socket.
     */
    public int getPort() {
        return threadServerConfig.getPort();
    }

    /**
     * Set the LoadMonitor object to use.
     */
    public void setLoadMonitor(LoadMonitor loadMonitor) {
        this.loadMonitor = loadMonitor;
    }

    /**
     * Call this to set a connection factory. THERE MUST exist
     * a connection factory. Either call this method or pass one
     * in the constructor.
     */
    public void setConnFactory(ConnThreadBaseFactory cf) {
        threadServerConfig.setConnFactory(cf);
    }

    protected ConnThreadBaseFactory getConnFactory() {
        return threadServerConfig.getConnFactory();
    }

    /**
     * Call this to set a server socket factory.
     */
    public void setServerSocketFactory(ServerSocketFactory sf) {
        serverSocketFactory = sf;
    }

    protected ServerSocketFactory getServerSocketFactory() {
        return serverSocketFactory;
    }

    /**
     * Call this to specify whether this server runs in NIO or regular IO
     */
    public void setUseNIO(boolean useNIO) {
        threadServerConfig.setNio(useNIO);
    }

    public boolean isNIO() {
        return threadServerConfig.isNio();
    }

    /**
     * Call this to establish connection throttling, to limit the rate
     * at which connections are accepted.
     *
     * @param throttle the minimum interval between connections, in millis
     */
    public void setThrottle(long throttle) {
        if (throttle > 0L) {
            throttleValue = throttle;

            logger.trace("ServerThread: using throttle setting: " + throttle);
        } else {
            logger.trace("ServerThread: rejecting invalid throttle setting: " + throttle);
        }
    }

    /**
     * Main body of the thread. Will listen for connections, and
     * open the connection for reading as they come in.
     */
    public void run() {
        Monitor socketMonitor = new Monitor();

        //--
        // Choose which scheme we are going to use. Either NIO or regular
        // blocking i/o. NOTE: We can only compile NIO stuff with jdk1.4,
        // so that's why that line is commented out.
        //--
        if (threadServerConfig.isNio()) {
            ioInit = new IoInitNio();
        } else {
            ioInit = new IoInitBlocking();
        }

        if (ioInit == null) {
            GAlerter.lab("COULD INITIALIZE SERVER: IoInit object is NULL! Server will die!");
            Utility.sleep(5000);
            System.exit(1);
        }

        //
        ioInit.init(threadServerConfig.getProps());
        setServerSocketFactory(ioInit.makeServerSocketFactory());

        //
        ioInit.startup();

        //--
        // TODO: Temporary code until we clean up init of this object.
        // The servProps for the config object should not be null, but it
        // will be for a while.
        //--
        FiveProps servProps = threadServerConfig.getProps();
        if (servProps == null) {
            servProps = Props.instance().getServProps();
        }

        if (threadServerConfig.getIp() != null) {
            //--
            // Use the variant that bind to an ip.
            //--
            serverSocket = serverSocketFactory.createSocket(threadServerConfig.getIp(), threadServerConfig.getPort(), MAX_BLOCKERS, servProps);
        } else {
            serverSocket = serverSocketFactory.createSocket(threadServerConfig.getPort(), servProps);
        }

        //initialize the socket server failed, just exit the program.
        if (serverSocket == null || serverSocket.getServerSocket() == null) {
            String msg = "COULD NOT START server on port " + threadServerConfig.getPort()
                    + (threadServerConfig.getIp() != null ? ("ip=" + threadServerConfig.getIp()) : "")
                    + " due to some socket error. Server will die.";

            //send out a bug alert.
            GAlerter.lab(msg);
            threadExiting();

            Utility.sleep(5000);
            System.exit(1);

            return;
        }

        logger.info("ServerThread.run(): Server listening on port " + serverSocket.getServerSocket().getLocalPort());

        try {
            SocketWrapper clientSocket;

            while (true) {
                // used for connection throttling, below
                long t = System.currentTimeMillis();

                //----------------------------------------
                // Listen for connections. When one
                // comes in, allocate a connection thread
                // object and start it up.
                //----------------------------------------
                logger.trace("ServerThread.run(): Listening for client connections.");

                startedFlag = true;
                try {
                    clientSocket = serverSocket.accept();

                    //bug debuger, todo.
                    socketMonitor.onClientConnected(clientSocket);
                } catch (SocketException e) {
                    // This error, we assume, represents a
                    // transient condition, so rerun the loop.
                    socketMonitor.onExceptionCaught(e);

                    logger.warn("Transient listening exception " + e);

                    // Only rerun if we're not dying.
                    if (p_getState() == STATE_DYING) {
                        throw e;
                    } else {
                        //--
                        // Wait a bit so that in case we hit a permanent
                        // error we don't loop.
                        //--
                        Utility.sleep(1000);
                        continue;
                    }
                } catch (IOException e) {
                    logger.warn("Transient listening exception " + e);

                    // Only rerun if we're not dying.
                    if (p_getState() == STATE_DYING) {
                        throw e;
                    } else {
                        //--
                        // Wait a bit so that in case we hit a permanent
                        // error we don't loop.
                        //--
                        Utility.sleep(1000);
                        continue;
                    }
                } catch (Error err) {
                    GAlerter.lab("ServerThread.run: listen port=" + threadServerConfig.getPort() + ":ERROR! " + err);
                    break;
                }

                String ipaddr = IpUtil.cvtToString(clientSocket.getInetAddress().getAddress());

                //load checking. if it's overload, reject the connection.
                if (loadMonitor != null && loadMonitor.isOverloaded()) {
                    try {
                        clientSocket.close();
                    } catch (Exception e) {
                    }

                    logger.warn("ServerThread: OVERLOADED, rejecting conn: " + ipaddr);
                    continue;
                }

                logger.trace("ServerThread.run(): Received a client connection from: " + ipaddr);

                if (p_getState() == STATE_DYING) {
                    logger.trace("ServerThread.run(): Refusing conn because we are dying. ");
                    continue;
                }

                //wrapper the clientSocket
                ConnThreadBase c = threadServerConfig.getConnFactory().allocate(clientSocket, !threadServerConfig.isNio());

                //notify the connection is made.
                connMade(c, clientSocket);
                logger.trace("ServerThread.run(): Firing off client thread");

                //start the thread to loop process the client requests
                c.start();

                // apply connection throttling to limit ramp-up speed
                if (throttleValue > 0L) {
                    long millis = t + throttleValue - System.currentTimeMillis();

                    logger.trace("ServerThread.run(): throttling connections");
                    Utility.sleep(millis);
                }
            }
        } catch (Exception e) {
        	GAlerter.lab("ServerThread error", e);
        }

        threadExiting();
    }

    /**
     * You can call this method to wait for the server to start.
     * It will simply wait in a loop until it's ready.
     */
    public void waitForStart() {
        while (true) {
            //------------------------------------------------
            // Return if we have started or if we are dead.
            //------------------------------------------------

            if (startedFlag || deadFlag) {
                return;
            }

            try {
                sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Call this method to wait for the server to die.
     */
    public void waitForDeath() {
        while (!deadFlag) {
            try {
                sleep(50);
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Add a listener object for when a connection gets made.
     * The object's notify() method will be invoked when a
     * a connection is made, but before the thread gets fired off
     * to logicProcess the connection.
     */
    public void addConnMadeListener(ConnMadeListener l) {
        connMadeContainer.addElement(l);
    }

    /**
     * Remove a previously registererd conn made listener object.
     */
    public void removeConnMadeListener(ConnMadeListener l) {
        connMadeContainer.removeElement(l);
    }

    /**
     * Add a listener object for when the thread dies. It's
     * possible for the thread to die at some point (if there's
     * an error on the listen socket). Any objects registered will
     * then be notified prior to the thread dying.
     */
    public void addServerDieListener(ServerDieListener l) {
        serverDieContainer.addElement(l);
    }

    /**
     * Remove a previously registered listener object.
     */
    public void removeServerDieListener(ServerDieListener l) {
        serverDieContainer.removeElement(l);
    }

    /**
     * Add a listener object for when a connection thread dies.
     * Note that this listener object will be used by all threads.
     */
    public void addConnDieListener(ConnDieListener l) {
        connDieContainer.addElement(l);
    }

    /**
     * Remove a previously registered listener object.
     */
    public void removeConnDieListener(ConnDieListener l) {
        connDieContainer.removeElement(l);
    }

    /**
     * This is called to kill this thread.
     */
    public synchronized void die() {
        logger.info("ServerThread.die(): dying");
        serverState = STATE_DYING;

        //--------------------------------------------------------
        // Hopefully, closing the listen socket will cause
        // an IO exception while it's blocked listening.
        // Otherwise, there will be *no way* to shut down
        // this thread.
        //--------------------------------------------------------

        if (serverSocket != null && serverSocket.getServerSocket() != null) {
            logger.info("ServerThread.die(): Shutting down listen socket");
            try {
                //----------------------------------------
                // What i'm trying to do is to get the
                // goddam thread to exit *cleanly* by
                // somehow aborting the accept() call.
                // I found that just doing a close()
                // on the socket won't do it. I guessed
                // at the interrupt(), and that seemed to
                // do the trick. So close the socket, and
                // then interrupt the thread.
                //----------------------------------------

                serverSocket.getServerSocket().close();
                this.interrupt();
            }
            catch (IOException e) {
                //
            }
        }
    }

    /**
     * This is called to kill this thread and all connection threads
     * that this server has fired off in the meantime.
     */
    public synchronized void dieAll() {
        serverState = STATE_DYING;

        logger.info("ServerThread.dieAll(): Removing " + connThreads.size()
                + " conn threads");

        Enumeration<ConnThreadBase> itr = connThreads.elements();
        while (itr.hasMoreElements()) {
            ConnThreadBase ct = itr.nextElement();
            ct.die();
        }

        die();
    }

    public String toString() {
        return "ServerThread:" + threadServerConfig.getPort();
    }

    private void connMade(ConnThreadBase cthread, SocketWrapper sock) {
        connThreads.put(cthread.hashCode(), cthread);

        //--------------------------------------------------------
        // Notify all the listener objects that a connection
        // has been made.
        //--------------------------------------------------------
        Enumeration<ConnMadeListener> itr = connMadeContainer.elements();
        while (itr.hasMoreElements()) {
            ConnMadeListener l = itr.nextElement();

            l.notify(cthread);
        }

        //--------------------------------------------------------
        // Whenever a connection thread dies, we want to know
        // about it. We register a listener object ( a member
        // class) and when that gets called, we will inform
        // any listeners registered with this object.
        //--------------------------------------------------------
        cthread.addConnDieListener(connThreadDieListener);

        // Run any initialization we need to do for this conn
        ioInit.openConn(cthread, sock);
    }

    private class ConnThreadDie implements ConnDieListener {
        public void notify(ConnThreadBase cthread) {
            connDie(cthread);
        }
    }

    private void connDie(ConnThreadBase cthread) {
        connThreads.remove(new Integer(cthread.hashCode()));

        Enumeration<ConnDieListener> itr = connDieContainer.elements();
        while (itr.hasMoreElements()) {
            ConnDieListener l = itr.nextElement();
            l.notify(cthread);
        }
    }

    /**
     * Called whenever the thread is going to exit.
     */
    private void threadExiting() {
        //first set the flag to die.
        deadFlag = true;
        if (logger.isDebugEnabled()) {
        	logger.debug("Thread Exiting: " + this);
        }
        //--------------------------------------------------------
        // Notify all the listener objects that we are dying.
        //--------------------------------------------------------
        Enumeration<ServerDieListener> itr = serverDieContainer.elements();
        while (itr.hasMoreElements()) {
            ServerDieListener l = itr.nextElement();
            l.notify(this);
        }

        //--------------------------------------------------------
        // Notify the conn thread factory that we are dying
        // so that it does any cleanup that might be necessary.
        // Ideally we would do this by having the factory register
        // a listener, but the factory knows nothing about the
        // ServerThread object. In essence, the factory is used
        // to construct the ServerThread object, so it gets
        // constructed first.
        //--------------------------------------------------------
        threadServerConfig.getConnFactory().die(this);
    }

    private synchronized int p_getState() {
        return serverState;
    }
}
