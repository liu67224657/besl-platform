package com.enjoyf.platform.serv.thrserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.NamingServiceFactory;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.service.naming.Registrant;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.util.FiveProps;

/**
 * This object is used to wrap a ServerThread object and optionally
 * pair it up with a Registrant. This allows the start/stop of the
 * objects to be done in tandem in order to minimize problems (if
 * a Registrant registers with the naming service prior to having
 * the ServerThread start up, clients may get errors when trying to
 * connect to it).
 * <p/>
 * The ServerWrap objects are registered with either MainInit or MainConfig
 */
public class ServerWrap {

    private static final Logger logger = LoggerFactory.getLogger(ServerWrap.class);
    
    private ServerThread serverThread;

    private Registrant registrant;
    private boolean exitOnDieFlag = true;

    /**
     * Use this to initialize an object with just a Registrant to
     * the naming service; no associated service.
     */
    public ServerWrap(String servicePrefix, FiveProps serverProps) {
        this(null, servicePrefix, serverProps);
    }

    /**
     * Ctor this object with the ServerThread to manage, but no
     * registrant.
     */
    public ServerWrap(ServerThread server) {
        this(server, null, null);
    }

    /**
     * Ctor the object with a ServerThread to manage as well
     * as the corresponding service info for registering with the
     * naming service.
     *
     * @param server        The ServerThread to manage.
     * @param servicePrefix The prefix in the servProps object for
     *                      finding the service information that will be registered with the
     *                      naming service. The information that needs to be in the servProps
     *                      object is: <servicePrefix>.TYPE, <servicePrefix>.NAME,
     *                      <servicePrefix>.PORT
     * @param servProps     The server props file.
     */
    public ServerWrap(ServerThread server, String servicePrefix, FiveProps servProps) {
        serverThread = server;

        if (servicePrefix != null) {
            registrant = NamingServiceFactory.instance().createRegistrant(servicePrefix, servProps);
        }
    }

    public ServerWrap(ServerThread server, ServiceInfo serviceInfo) {
        serverThread = server;
        registrant = NamingServiceFactory.instance().createRegistrant(serviceInfo);
    }

    /**
     * Ctor the object with a ServerThread to manage as well
     * as the corresponding Registrant.
     *
     * @param server     The ServerThread to manage.
     * @param registrant The Registrant object to be registered
     *                   with the naming service.
     */
    public ServerWrap(ServerThread server, Registrant registrant) {
        serverThread = server;
        this.registrant = registrant;
    }

    void start() {
        if (registrant != null) {
            logger.info("ServerWrap.start: registering: " + registrant);

            try {
                NamingServiceSngl.get().registerService(registrant);
            } catch (IllegalStateException e) {
                logger.info("ServerWrap. Caught illegal state exception due to a bad ServiceInfo object.");
            }
        }

        if (serverThread != null) {
            logger.info("ServerWrap.start: starting: " + serverThread);
            serverThread.start();
        }
    }

    public void die() {
        if (registrant != null) {
            NamingServiceSngl.get().unregisterService(registrant);
        }

        if (serverThread != null) {
            serverThread.dieAll();
        }
    }

    /**
     * @param exitOnDie Set to true if you want the server to die
     *                  if this particular ServerThread dies.
     */
    public void setExitOnDie(boolean exitOnDie) {
        exitOnDieFlag = exitOnDie;
    }

    /**
     * Set a LoadListener object to be associated with the Registrant.
     */
    public void setLoadListener(Registrant.LoadListener loadListener) {
        if (registrant != null) {
            registrant.setLoadListener(loadListener);
        }
    }

    ServerThread getServer() {
        return serverThread;
    }

    boolean shouldExitOnDie() {
        return exitOnDieFlag;
    }

    public Registrant getRegistrant() {
        return registrant;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("server=" + (serverThread == null ? "null" : serverThread.toString()));
        sb.append(":registrant=" + (registrant == null ? "null" : registrant.toString()));

        return new String(sb);
    }
}
