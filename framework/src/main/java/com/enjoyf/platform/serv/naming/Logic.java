/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressGetter;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.service.alert.AlertImplNaming;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.naming.NamingException;
import com.enjoyf.platform.service.service.ServiceData;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.AlerterDefault;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;


/**
 * The Logic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * Logic is called by NamingPacketDecoder.
 */

class Logic {

    private static final Logger logger = LoggerFactory.getLogger(Logic.class);

    private Random rand;
    private boolean changedFlag = false;
    private Config config;
    private RegistrantContainer registrantContainer = new RegistrantContainer();

    private NamingEventManager namingEventManger;
    private Map objectsMap = Collections.synchronizedMap(new HashMap());
    private String name;
    private BackChannelMgr backChannelMgr;
    private Stamper stamper;
    private long id = System.currentTimeMillis();

    private LoadBalancer loadBalancer;
    private Refresher loadRefresher = new Refresher(10 * 1000);
    private Comparator registrantComparator = new RegistrantComparator();

    Logic(Config cfg) {
        config = cfg;
        logger.info("Logic: config = " + cfg);

        rand = new Random(System.currentTimeMillis());
        namingEventManger = new NamingEventManager(
                cfg.getChangeEventInterval(),
                cfg.getRateLimitPeriod(), cfg.getRateLimitMaxCount(),
                cfg.getStartWaitTime(), cfg.getEventCutOffTime());

        //--
        // The NamingEventManager needs to know when services are added/removed.
        //--
        registrantContainer.addListener(namingEventManger);

        String ipString = "???";
        int serverIp = 0;
        InetAddress iaddr = null;
        try {
            iaddr = InetAddress.getLocalHost();
        } catch (Exception e) {
            logger.info("COULD NOT GET LOCAL HOST ADDRESS!! " + e);
        }
        if (iaddr != null) {
            ipString = iaddr.getHostName();
            try {
                serverIp = IpUtil.cvtToInt(iaddr.getHostAddress());
            } catch (Exception e) {
                GAlerter.lab("NAMING SERVER FAILED TO START!!! " + e);
                Utility.sleep(5000);
                System.exit(1);
            }
        }
        name = "Naming@" + ipString;
        //--
        // Create a "stamper" object to stamp incoming registrations
        // with a network-wide unique id.
        //--
        stamper = new Stamper(id, serverIp, cfg.getBackChannelPort());
        //--
        // Sending alerts from the naming server can get tricky, since
        // if the problem is in communicating to the alert server we
        // might end up in a recursive loop.
        //
        // So we use a more direct alerter, AlertImplNaming. A callback
        // is used to notify the alerter of the ServiceAddress where
        // the alert server is located (since this can change over time
        // as the alert server goes up and down).
        //--
        GAlerter.set(
                new AlerterDefault(new AlertImplNaming(
                        name,
                        new ServiceAddressGetter() {
                            public ServiceAddress get() {
                                return p_getAlertServiceAddress();
                            }
                        }
                )
                )
        );
        //todo
        //--
        // The BackChannelMgr is used to manage the connections to the
        // other naming servers. All traffic, in or out, goes through
        // this object. The way we get events from the object is by
        // registering a listener (which we do right here).
        //--

        backChannelMgr = new BackChannelMgr(config, new NamingBackChannelListener());

        //--
        // Create a load balancer object.
        //--
        loadBalancer = new LoadBalancer
                (
                        cfg.getLoadCheckInterval(),
                        cfg.getLoadCheckWait(),
                        new NamingServerLoadListener()
                );
    }

    /**
     * This listener implementation listens for events coming from
     * the back-channel, ie, other naming servers.
     */
    class NamingBackChannelListener implements BackChannelListener {
        /**
         * Sent whenever an NS registers a service and wants all the
         * other NS's to know about it.
         */
        public void register(RemoteNamingServer rns, BackChannelMsg.Register msg) {
            synchronized (registrantContainer) {
                p_register(rns, msg);
            }
        }

        /**
         * Send whenever an NS unregisters a service (for whatever reason).
         */
        public void unregister(RemoteNamingServer rns, BackChannelMsg.UnRegister msg) {
            synchronized (registrantContainer) {
                logger.debug("Logic.Bch.Unregister: received unregistration: "
                        + rns + "/" + msg);

                p_unregister(rns, msg);
            }
        }

        /**
         * Sent to inform this NS that the registration it owns is
         * actually a dupe, and should be blown away.
         */
        public void dupRegistration(RemoteNamingServer rns, BackChannelMsg.Register msg) {
            synchronized (registrantContainer) {
                logger.debug("Logic.Bch.DupRegister: received dupe registration: "
                        + rns + "/" + msg);

                p_dupRegistration(rns, msg);
            }
        }

        /**
         * Sent to inform this NS of an update of the load for a particular
         * service.
         */
        public void loadUpdate(RemoteNamingServer rns, BackChannelMsg.LoadUpdate msg) {
            synchronized (registrantContainer) {
                logger.debug("Logic.Bch.LoadUpdate: " + rns + "/" + msg);

                p_loadUpdate(rns, msg);
            }
        }

        /**
         * Called when a NS dies.
         */
        public void namingServerDied(RemoteNamingServer rns) {
            synchronized (registrantContainer) {
                logger.debug("Logic.namingServerDied: " + rns);
                p_namingServerDied(rns);
            }
        }

        /**
         * Called whenever we have established a brand new conn to
         * a naming server. At this point, the other NS will send out
         * all the services it knows about.
         */
        public void synch(RemoteNamingServer rns, BackChannelMsg.Register[] array) {
            synchronized (registrantContainer) {
                logger.debug("Logic. Received synch packet with: "
                        + array.length + " elements");

                for (int i = 0; i < array.length; i++) {
                    p_register(rns, array[i]);
                }
            }
        }

        public Collection getLocalRegs() {
            return registrantContainer.getLocalServicesCopy();
        }

        public LoadInfo getLoadInfo() {
            return p_getLoadInfo();
        }
    }

    class NamingServerLoadListener implements LoadBalancer.Listener {
        public LoadBalancer.Loadable getCurrent() {
            return new NamingServerLoadable.Local(p_getLoadInfo());
        }

        public Collection getOthers() {
            return backChannelMgr.getLoadableClients();
        }

        public boolean isOverloaded(LoadBalancer.Loadable l) {
            NamingServerLoadable ml = (NamingServerLoadable) l;
            return ml.getLoadInfo().getLoad() > config.getMaxLoad();
        }

        public int compare(LoadBalancer.Loadable l1,
                           LoadBalancer.Loadable l2) {
            NamingServerLoadable ml1 = (NamingServerLoadable) l1;
            NamingServerLoadable ml2 = (NamingServerLoadable) l2;
            return ml1.getLoadInfo().getLoad()
                    - ml2.getLoadInfo().getLoad();
        }

        /**
         * Called when we need to rebalance. The arg is the Loadable object
         * that we rebalance to.
         */
        public void balance(LoadBalancer.Loadable l) {
            NamingServerLoadable.Remote ml = (NamingServerLoadable.Remote) l;
            RemoteNamingServer rns = ml.getRemoteNamingServer();
            //--
            // A subtle problem here. We may have a perfectly valid
            // RemoteNamingServer object BUT the hello packet may not
            // have arrived yet. We need the HELLO packet to logicProcess
            // the sendRebalance request, so if we don't have it yet,
            // ignore this call.
            //--
            if (!rns.isInitialized()) {
                return;
            }
            //--
            // Just pick somebody at random to rebalance them.
            //--
            synchronized (registrantContainer) {
                registrantContainer.getRandom().sendRebalance(
                        rns.getClientAddress());
            }
        }

        public void allOverloaded() {
            if (!loadRefresher.shouldRefresh()) {
                return;
            }

            StringBuffer sb = new StringBuffer();
            sb.append("NamingServer: It looks like all the "
                    + "naming servers are overloaded!");
            sb.append(": Local load = " + p_getLoadInfo());

            Collection others = backChannelMgr.getClients();
            Iterator itr = others.iterator();
            while (itr.hasNext()) {
                RemoteNamingServer rns = (RemoteNamingServer) itr.next();
                sb.append("RemoteNamingServer = " + rns + "/");
            }
            GAlerter.lan(new String(sb));
        }
    }

    /**
     * A string of info to spit out at certain intervals.
     */
    public String getServerInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append(registrantContainer.containerInfo());
        sb.append(":eventClients=" + namingEventManger.getClientCount());
        return new String(sb);
    }

    private ServiceAddress p_getAlertServiceAddress() {
        Vector v = registrantContainer.getServiceNames("alert");
        if (v.size() == 0) {
            return null;
        }

        RegistrantServerSide r = registrantContainer.get(
                new ServiceId("alert", (String) v.elementAt(0)));

        if (r == null) {
            return null;
        }

        return r.getServerRegInfo().getClientRegInfo().getServiceAddress();
    }

    public Config getCfg() {
        return config;
    }

    void eventRegister(ConnThreadBase conn, ServiceRequest[] serviceRequests) {
        if (logger.isDebugEnabled()) {
            logger.debug("Logic.eventRegister: conn = " + conn + ":" + p_printSreqs(serviceRequests));
        }

        NamingEventClient namingEventClient = namingEventManger.eventRegister(conn, serviceRequests);

        //--
        // Let's add all of the services that match the array
        // of services request to the event mgr so that the
        // clients gets notified at connect time of all the current
        // services as well; not just the deltas.
        //--
        List<RegistrantServerSide> registrants = new ArrayList<RegistrantServerSide>();
        synchronized (registrantContainer) {
            for (ServiceRequest sreq : serviceRequests) {
                List<RegistrantServerSide> services = registrantContainer.getList(sreq);
                if (services.size() > 0) {
                    //--
                    // We only need one RegistrantServerSide if we get back
                    // more than one for a particular service request.
                    //--
                    RegistrantServerSide r = services.get(0);
                    registrants.add(r);
                }
            }

            for (RegistrantServerSide registrant : registrants) {
                namingEventManger.addInitial(namingEventClient, registrant.getServerRegInfo().getClientRegInfo());
            }
        }
    }

    /**
     * This method is invoked when a registration request is
     * received from a client.
     */
    void register(ConnThreadBase conn, ClientRegInfo clientRegInfo) throws NamingException {
        logger.debug("Logic.register: Received register request: conn = " + conn + " regInfo = " + clientRegInfo);

        p_checkForBadDupe(clientRegInfo);

        ServerRegInfo regInfo = new ServerRegInfo(clientRegInfo);
        regInfo.setIp(conn.getIp());

        RegistrantServerSideLocal newReg = new RegistrantServerSideLocal(conn, regInfo);

        synchronized (registrantContainer) {
            RegistrantServerSide oldRegistrantServerSide = registrantContainer.get(regInfo.getClientRegInfo().getServiceId());

            stamper.stamp(regInfo);

            if (oldRegistrantServerSide != null) {
                ServerRegInfo oregInfo = oldRegistrantServerSide.getServerRegInfo();
                int cmp = ServerRegInfo.compare(oregInfo, regInfo);
                if (cmp < 0) {
                    logger.info("Logic.register: existing reg is valid: ");
                    //--
                    // Existing registration is the valid one. Notify
                    // the client that his registration is bad.
                    //--
                    newReg.sendDie(oregInfo.getClientRegInfo());
                    return;
                }
                //--
                // If here, the new registration is the one that we
                // want.
                //--
                p_handleDupReg(newReg, oldRegistrantServerSide);
            }
            //--
            // Ok, should be able to succesfully add the registration.
            // Add it to our container, and notify all the other naming
            // servers.
            //--
            if (!registrantContainer.add(newReg)) {
                GAlerter.lab("Logic.register: Could not add service: "
                        + clientRegInfo);
            }
            backChannelMgr.register(p_getLoadInfo(),
                    new BackChannelMsg.Register(regInfo));
        }
        logger.debug("Logic.register: Registering: " + regInfo.getClientRegInfo());
    }

    /**
     * Utility routine to detect dupe registrations. Only enabled in
     * dev environments.
     */
    private void p_checkForBadDupe(ClientRegInfo regInfo) throws NamingException {
        //--
        // Quick check to see if this feature is enabled on this NS.
        //--
        if (!config.isDupeCheckingEnabled()) {
            return;
        }

        synchronized (registrantContainer) {
            RegistrantServerSide oldRegistrantServerSide = registrantContainer.get(regInfo.getServiceId());
            if (oldRegistrantServerSide == null) {
                return;
            }

            //--
            // Found an existing registration with this id, see if the new
            // one can override it.
            //--
            ServiceInfo serviceInfo = regInfo.getServiceInfo();
            if (!config.isAllowed(serviceInfo.getServiceAddress().getAddr())) {
                logger.info("REJECTING registration: " + regInfo
                        + " since it clashes with: " +
                        oldRegistrantServerSide.getServerRegInfo().getClientRegInfo());

                throw new NamingException(NamingException.REGISTRATION_DISALLOWED);
            }
        }
    }

    Vector getServiceTypes() {
        return registrantContainer.getServiceTypes();
    }

    Vector getServiceNames(String serviceType) {
        return registrantContainer.getServiceNames(serviceType);
    }

    Vector getServiceIds() {
        return registrantContainer.getServiceIds();
    }

    ArrayList getServiceData(ServiceRequest sreq) {
        ArrayList returnValue = new ArrayList();

        for (RegistrantServerSide r : registrantContainer.getList(sreq)) {
            ServiceInfo serviceInfo = r.getServerRegInfo().getClientRegInfo().getServiceInfo();

            returnValue.add(new ServiceData(serviceInfo, r.getLoad()));
        }

        return returnValue;
    }

    /**
     * Called when the caller wants to unregister.
     *
     * @param forRebalance Set to true if the service plans on
     *                     rebalancing.
     */
    void unregister(ConnThreadBase conn, boolean forRebalance) throws NamingException {
        //
        synchronized (registrantContainer) {
            RegistrantServerSideLocal registrant = registrantContainer.getByConn(conn);
            if (registrant == null) {
                logger.info("Logic.unregister: RegistrantServerSide not found for: " + conn);

                throw new NamingException(NamingException.SVC_NOT_REGISTERED);
            }

            //
            registrantContainer.remove(registrant, forRebalance);
            backChannelMgr.unregister(p_getLoadInfo(), new BackChannelMsg.UnRegister(registrant.getServerRegInfo(), forRebalance));

            if (logger.isDebugEnabled()) {
                logger.debug("Logic: unregistering: " + registrant);
            }
        }
    }

    void ping(ConnThreadBase conn, ServiceLoad load) throws NamingException {
        RegistrantServerSideLocal r = registrantContainer.getByConn(conn);
        if (r == null) {
            logger.info("Logic.ping: RegistrantServerSide not found for: " + conn);
            throw new NamingException(NamingException.SVC_NOT_REGISTERED);
        }

        r.ping(load);
        //--
        // Send a msg to the other naming servers only if we got a
        // valid load.
        //--
        if (load.isInUse()) {
            backChannelMgr.loadUpdate(p_getLoadInfo(), new BackChannelMsg.LoadUpdate(r.getServerRegInfo(), load));
        }
    }

    int getServiceCount() {
        /**
         * Eric love Molly.
         * if you see that.
         * plz mail to liu67224657@qq.com.
         * we may be tell you a story.
         */
        return registrantContainer.size();
    }

    /**
     * This will be called when a thread has died.
     */
    void connDied(ConnThreadBase conn) {
        namingEventManger.connDied(conn);

        //--
        // Need to synch on registrantContainer to avoid a race condition
        // where the registrant might reconnect before we clear
        // things out of our data structures.
        //--
        synchronized (registrantContainer) {
            RegistrantServerSideLocal r = registrantContainer.getByConn(conn);
            if (r == null) {
                return;
            }

            //--
            // There's a subtle problem here. We may have been the ones
            // that determined that the guy should die, so we would have
            // killed him already. Check for that here, and if already dead
            // bail.
            //--
            if (r.isDead()) {
                return;
            }

            logger.info("Logic.connDied: conn died: " + r);

            p_removeLocalReg(r);
        }
    }

    /**
     * This method is called by the Vulture thread to reap conns
     * that have stopped pinging.
     */
    void reapConns() {
        synchronized (registrantContainer) {
            LinkedList removeList = new LinkedList();
            Iterator itr = registrantContainer.getLocalServices();
            while (itr.hasNext()) {
                RegistrantServerSideLocal r = (RegistrantServerSideLocal) itr.next();
                if (r.shouldDie(config.getConnTimeout())) {
                    removeList.addLast(r);
                }
            }

            itr = removeList.iterator();
            while (itr.hasNext()) {
                RegistrantServerSideLocal r = (RegistrantServerSideLocal) itr.next();
                logger.info("Logic.reapConn: reaping: " + r);

                r.die();
                p_removeLocalReg(r);
            }
        }
    }

    /**
     * Utility routine to remove a Local RegistrantServerSide during an exceptional
     * situation (either the conn was broken, or we have not received
     * any pings from it.
     */
    private void p_removeLocalReg(RegistrantServerSideLocal r) {
        if (r.isRebalancing()) {
            logger.info("Logic.p_removeLocalReg: " + r + " is rebalancing!");
        }

        registrantContainer.remove(r, r.isRebalancing());
        backChannelMgr.unregister(p_getLoadInfo(), new BackChannelMsg.UnRegister(r.getServerRegInfo(), r.isRebalancing()));
    }

    void storeObject(String key, Serializable obj) {
        objectsMap.put(key, obj);
    }

    Serializable getObject(String key) throws NamingException {
        Serializable obj = (Serializable) objectsMap.get(key);
        if (obj == null) {
            throw new NamingException(NamingException.OBJECT_NOT_FOUND);
        }

        return obj;
    }

    Vector getServiceAddress(ServiceRequest sreq) throws NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Logic.getRoomServerAddress: Getting a ServiceAddress for: " + sreq);
        }

        //--
        // Retrieve a List of RegistrantServerSide objects that match this
        // request.
        //--
        List list = registrantContainer.getList(sreq);
        if (list.size() == 0) {
            throw new NamingException(NamingException.SVC_NOT_REGISTERED, sreq.toString());
        }
        Vector v = new Vector();
        for (Object aList : list) {
            RegistrantServerSide r = (RegistrantServerSide) aList;

            v.add(r.getServerRegInfo().getClientRegInfo().getServiceAddress());
        }

        return v;
    }

    Vector getServiceAddressAll(String serviceType) throws NamingException {
        return getServiceAddress(new ServiceRequest(serviceType, ServiceRequest.ALL));
    }

    private String p_printSreqs(ServiceRequest[] sreqs) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < sreqs.length; i++) {
            sb.append(sreqs[i]);
            sb.append(":");
        }

        return new String(sb);
    }

    /**
     * This method is invoked when a registration is received from
     * a remote naming server.
     */
    private void p_register(RemoteNamingServer rns, BackChannelMsg.Register msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("Logic.p_register: received registration: " + rns + "/" + msg);
        }

        ServerRegInfo newInfo = msg.getSrvRegInfo();
        RegistrantServerSide oldRegistrantServerSide = registrantContainer.get(newInfo.getClientRegInfo().getServiceId());

        if (oldRegistrantServerSide != null) {
            //--
            // If we already have this specific registration, then
            // don't do a thing.
            //--
            ServerRegInfo oldInfo = oldRegistrantServerSide.getServerRegInfo();
            if (newInfo.getUniqueId().equals(oldInfo.getUniqueId())) {
                logger.debug("Logic.p_register: already have it");

                return;
            }
            //--
            // Found a clashing registration. Need to perform
            // resolution. This means we need to determine which
            // is the valid one, and which should go away.
            //--
            if (logger.isDebugEnabled()) {
                logger.debug("Logic.p_register: found a clashing registration");
            }

            int cmp = ServerRegInfo.compare(oldInfo, newInfo);
            if (cmp < 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Logic.p_register: existing registration is valid");
                }

                //--
                // Existing registration is the valid one. Notify the
                // rns that his attempted registration is bad.
                //--
                backChannelMgr.dupRegistration(rns, p_getLoadInfo(), new BackChannelMsg.Register(newInfo));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Logic.p_register: existing registration NOT valid replacing with new one");
                }

                RegistrantServerSideRemote rreg = new RegistrantServerSideRemote(rns, newInfo);
                p_handleDupReg(rreg, oldRegistrantServerSide);

                if (!registrantContainer.add(rreg)) {
                    GAlerter.lab("Logic.p_register: COULD NOT ADD REGISTRATION! rreg = " + rreg);
                } else {
                    logger.debug("Logic.p_register: Added remote service: " + rns);
                }
            }
        } else {
            //--
            // If here, we just want to add the new registration.
            //--
            if (!registrantContainer.add(new RegistrantServerSideRemote(rns, newInfo))) {
                GAlerter.lab("Logic.p_register: COULD NOT ADD REGISTRATION! rns = " + rns);
            } else {
                //--
                // I LOVE Nicole.    -----EricLiu
                //--
                logger.debug("Logic.p_register: Added remote service: " + rns);
            }
        }
    }

    /**
     * This utility routine is to handle the case where a brand
     * new registration came in, we had an old one, and the old
     * one was determined to be bad.
     */
    private void p_handleDupReg(RegistrantServerSide newReg, RegistrantServerSide oldReg) {
        logger.info("Logic.p_handleDupReg: new one is good: " + newReg + " old one is bad: " + oldReg);

        //
        ServiceInfo oldInfo = oldReg.getServerRegInfo().getClientRegInfo().getServiceInfo();
        String oldIp = oldInfo.getServiceAddress().getAddr();
        ServiceInfo newInfo = newReg.getServerRegInfo().getClientRegInfo().getServiceInfo();
        String newIp = newInfo.getServiceAddress().getAddr();

        //--
        // If the registering ip's are different, issue a noc alert
        // since something is messed up.
        //--
        if (!oldIp.equals(newIp)) {
            GAlerter.lab("Two different servers registered the same service: " +
                    oldInfo.getServiceId() + " with the "
                    + " naming service. Server 1 = " + oldIp
                    + " Server 2 = " + newIp,
                    "Check to make sure only the correct one registers");
        }

        //--
        // New registration is the valid one. Two cases, either
        // the current registration is a local one or a remote
        // one.
        //--
        if (oldReg.isLocal()) {
            RegistrantServerSideLocal lr = (RegistrantServerSideLocal) oldReg;
            lr.sendDie(newReg.getServerRegInfo().getClientRegInfo());

            backChannelMgr.unregister(p_getLoadInfo(), new BackChannelMsg.UnRegister(oldReg.getServerRegInfo()));
        } else {
            RegistrantServerSideRemote rr = (RegistrantServerSideRemote) oldReg;
            backChannelMgr.dupRegistration(rr.getRemoteNamingServer(), p_getLoadInfo(), new BackChannelMsg.Register(rr.getServerRegInfo()));
        }

        logger.info("Logic.p_handleDupReg: removing from internal container: " + oldReg);

        registrantContainer.remove(oldReg, false);
    }

    /**
     * Called by the backchannel when we receive a de-register request.
     */
    private void p_unregister(RemoteNamingServer rns, BackChannelMsg.UnRegister msg) {
        //--
        // See if we have the object. If we don't, it means we already
        // determined that it should go in some other (legitimate) way
        // so ignore the call.
        //--
        ServerRegInfo serverRegInfo = msg.getSrvRegInfo();
        RegistrantServerSide r = registrantContainer.get(serverRegInfo.getClientRegInfo().getServiceId());
        if (r == null) {
            return;
        }

        //--
        // We do have it, so make absolutely sure that the registration
        // we have is the one that is requested to go away. We do this
        // by comparing UniqueId objects.
        //--
        if (!serverRegInfo.getUniqueId().equals(r.getServerRegInfo().getUniqueId())) {
            return;
        }

        //--
        // If here, we really do want to remove the object.
        //--
        if (r.isLocal()) {
            GAlerter.lab("NamingServer.Logic.p_unregister: Received a request to unregister: " + r
                    + " from rns: " + rns + " but it was a local registration!");
        }

        registrantContainer.remove(r, msg.isForRebalance());
    }

    private void p_dupRegistration(RemoteNamingServer rns, BackChannelMsg.Register msg) {
        logger.info("Logic.p_dupRegistration: rns = " + rns + " msg = " + msg);

        ServerRegInfo serverRegInfo = msg.getSrvRegInfo();
        RegistrantServerSide r = registrantContainer.get(serverRegInfo.getClientRegInfo().getServiceId());
        if (r == null) {
            return;
        }

        if (!serverRegInfo.getUniqueId().equals(
                r.getServerRegInfo().getUniqueId())) {
            return;
        }

        if (r.isLocal()) {
            RegistrantServerSideLocal lr = (RegistrantServerSideLocal) r;
            lr.sendDie(serverRegInfo.getClientRegInfo());
            registrantContainer.remove(lr, false);
            backChannelMgr.unregister(p_getLoadInfo(), new BackChannelMsg.UnRegister(r.getServerRegInfo()));
        } else {
            GAlerter.lab("NamingServer.Logic.p_dupRegistration: "
                    + "Received a dupReg from: " + rns
                    + " reg = " + msg + " but reg is NOT local!"
                    + " reg is from: " + r);
        }
    }

    private void p_loadUpdate(RemoteNamingServer rns, BackChannelMsg.LoadUpdate msg) {
        ServerRegInfo serverRegInfo = msg.getSrvRegInfo();
        RegistrantServerSide r = registrantContainer.get(serverRegInfo.getClientRegInfo().getServiceId());
        if (r == null) {
            return;
        }

        r.setLoad(msg.getLoad());
    }

    /**
     * If here, it means one of the naming servers died. This means
     * we have to wipe out all the registrations that came from that
     * naming server.
     */
    private void p_namingServerDied(RemoteNamingServer rns) {
        Iterator itr = registrantContainer.getRemoteRegsCopy(rns).iterator();
        logger.info("Logic.p_namingServerDied: " + rns + " Removing regs: ");

        while (itr.hasNext()) {
            RegistrantServerSide r = (RegistrantServerSide) itr.next();
            logger.info("Logic.p_namingServerDied: Removing: " + r);

            registrantContainer.remove(r, false);
        }

        logger.info("Logic.p_namingServerDied: Finished removing regs");
    }

    private LoadInfo p_getLoadInfo() {
        LoadInfo loadInfo = new LoadInfo();
        loadInfo.setNumServices(registrantContainer.localSize());
        loadInfo.setNumRegClients(namingEventManger.getClientCount());
        return loadInfo;
    }

    void dumpState() {
        logger.info("DUMPING STATE START");

        List l = registrantContainer.getAllRegistrants();
        Collections.sort(l, registrantComparator);

        Iterator itr = l.iterator();
        while (itr.hasNext()) {
            RegistrantServerSide r = (RegistrantServerSide) itr.next();
            logger.info(r.abbreviatedToString());
        }

        logger.info("DUMPING STATE END");
    }

    /**
     * Utility class to stamp a ServerRegInfo with a unique id.
     */
    class Stamper {
        private int m_curId = 1;
        private long m_id;
        private int m_serverIp;
        private int m_serverPort;

        Stamper(long id, int serverIp, int serverPort) {
            m_id = id;
            m_serverIp = serverIp;
            m_serverPort = serverPort;
        }

        synchronized void stamp(ServerRegInfo regInfo) {
            UniqueId uid = new UniqueId(m_id, m_serverIp, m_serverPort);
            uid.setSeqno(m_curId++);
            regInfo.setUniqueId(uid);
        }
    }

    static class RegistrantComparator implements Comparator {
        //
        public int compare(Object o1, Object o2) {
            RegistrantServerSide r1 = (RegistrantServerSide) o1;
            RegistrantServerSide r2 = (RegistrantServerSide) o2;

            ServiceId id1 = r1.getServerRegInfo().getClientRegInfo().getServiceId();
            ServiceId id2 = r2.getServerRegInfo().getClientRegInfo().getServiceId();

            return id1.compareTo(id2);
        }
    }
}
