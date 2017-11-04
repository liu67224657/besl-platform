package com.enjoyf.platform.serv.naming;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.PacketReaderAdvttl;
import com.enjoyf.platform.io.PacketWriterAdvttl;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.serv.thrserver.ConnDieListener;
import com.enjoyf.platform.serv.thrserver.ConnMadeListener;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.ConnThreadFactoryDefault;
import com.enjoyf.platform.serv.thrserver.PacketHandlerSimple;
import com.enjoyf.platform.serv.thrserver.PacketProcessor;
import com.enjoyf.platform.serv.thrserver.ServerThread;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * The role of this class is to manage the backchannel conns, ie,
 * the connections to all the other naming servers. Both incoming
 * and outgoing conns/transactions are managed by this class.
 * <p/>
 * This class is the interface between the server's business logic
 * and the back channel.
 * <p/>
 * Note that we maintain two connections between two naming servers.
 * The RemoteNamingServer object represents the connection that is
 * made by the remote NS to this NS, and BackChannelService represents
 * the connection that is made by this NS to the other NS.
 */
class BackChannelMgr implements BackChannelService.RegInfoCallback {
    
    private static final Logger logger = LoggerFactory.getLogger(BackChannelMgr.class);
    
    private Config config;
    private ServerThread serverThread;
    private BackChannelListener backChannelListener;
    private BackChannelContainer backChannelContainer;

    /**
     * Maps ConnThreadBase objects to RemoteNamingServer objects.
     */
    private Map<ConnThreadBase, RemoteNamingServer> remoteNamingServersMap = new ConcurrentHashMap<ConnThreadBase, RemoteNamingServer>();
    private IpValidityChecker ipValidityChecker;

    BackChannelMgr(Config cfg, BackChannelListener l) {
        config = cfg;
        backChannelContainer = new BackChannelContainer(cfg, l, this);
        ipValidityChecker = new IpValidityChecker(config);
        backChannelListener = l;

        //--
        // First thing we do is create a ServerThread object for listening
        // for conns from other naming servers.
        //--
        serverThread = new ServerThread(cfg.getBackChannelPort());

        PacketProcessor processor = new PacketProcessor() {
            public WPacketBase process(ConnThreadBase bt, RPacketBase rp) {
                return p_packetArrived(bt, rp);
            }
        };

        PacketHandlerSimple handler = new PacketHandlerSimple(processor);

        //--
        // The connection factory tells us what kind of connections
        // we want.
        //--
        serverThread.setConnFactory(
                new ConnThreadFactoryDefault(
                        new PacketWriterAdvttl(), new PacketReaderAdvttl(), handler
                )
        );

        //--
        // Use this listener to ctor an object which represents this
        // remote naming server.
        //--
        serverThread.addConnMadeListener(
                new ConnMadeListener() {
                    public void notify(ConnThreadBase ct) {
                        p_connMade(ct);
                    }
                }
        );

        //--
        // Need this listener to clean up our data structures.
        //--
        serverThread.addConnDieListener(
                new ConnDieListener() {
                    public void notify(ConnThreadBase ct) {
                        p_connDied(ct);
                    }
                }
        );

        serverThread.start();
    }

    /**
     * Return a copy of the RemoteNamingServer objects.
     */
    Collection<RemoteNamingServer> getClients() {
        return new LinkedList<RemoteNamingServer>(remoteNamingServersMap.values());
    }

    /**
     * Return a Collection of NamingServerLoadable.Remote objects, one per
     * RemoteNamingServer object.
     */
    Collection<NamingServerLoadable> getLoadableClients() {
        List<NamingServerLoadable> l = new LinkedList<NamingServerLoadable>();

        synchronized (remoteNamingServersMap) {
            Iterator<RemoteNamingServer> itr = remoteNamingServersMap.values().iterator();
            while (itr.hasNext()) {
                RemoteNamingServer c = itr.next();
                l.add(new NamingServerLoadable.Remote(c));
            }
        }
        return l;
    }

    private WPacketBase p_packetArrived(ConnThreadBase ct, RPacketBase p) {
        WPacket wp = null;

        wp = p_packetArrivedImpl(ct, p);
        if (wp != null) {
            try {
                ct.write(wp);
            }
            catch (Exception e) {
                logger.error("BackChannelMgr.p_packetArrived: "
                        + "Error writing packet.");
            }
        }

        return wp;
    }

    public Collection getLocalRegs() {
        return backChannelListener.getLocalRegs();
    }

    /**
     * Called by the business logic to broadcast a registration to all
     * other NS's.
     */
    void register(LoadInfo loadInfo, BackChannelMsg.Register msg) {
        BackChannelRequest req = new BackChannelRequest.Register(loadInfo, msg);
        BackChannelServiceWrap[] services = backChannelContainer.getServices();
        for (int i = 0; i < services.length; i++) {
            services[i].send(req);
        }
    }

    /**
     * Called by the business logic to broadcast an unregistration to all
     * other NS's.
     */
    void unregister(LoadInfo loadInfo, BackChannelMsg.UnRegister msg) {
        BackChannelRequest req = new BackChannelRequest.Unregister(loadInfo, msg);
        BackChannelServiceWrap[] services = backChannelContainer.getServices();
        for (int i = 0; i < services.length; i++) {
            services[i].send(req);
        }
    }

    /**
     * Called by the business logic to broadcast a load update to all other
     * naming services.
     */
    void loadUpdate(LoadInfo loadInfo, BackChannelMsg.LoadUpdate msg) {
        BackChannelRequest req = new BackChannelRequest.LoadUpdate(loadInfo, msg);
        BackChannelServiceWrap[] services = backChannelContainer.getServices();
        for (int i = 0; i < services.length; i++) {
            services[i].send(req);
        }
    }

    /**
     * Called by the business logic to broadcast an duplicate
     * registration to all other NS's.
     */
    void dupRegistration(RemoteNamingServer c, LoadInfo loadInfo, BackChannelMsg.Register msg) {
        BackChannelServiceWrap service =
                backChannelContainer.get(c.getServiceAddress());
        BackChannelRequest req = new BackChannelRequest.DupRegistration(loadInfo, msg);
        service.send(req);
    }

    private WPacket p_packetArrivedImpl(ConnThreadBase conn, RPacketBase p) {
        RPacket rp = (RPacket) p;
        byte type = rp.getType();

        RemoteNamingServer rns = (RemoteNamingServer) remoteNamingServersMap.get(conn);
        if (rns == null) {
            logger.warn("BackChannelMgr.p_packetArrived: no rns for conn: "
                    + conn + " Ignoring packet.");
            return null;
        }

        LoadInfo loadInfo = null;
        BackChannelMsg.Register regMsg = null;
        BackChannelMsg.UnRegister unRegisterMsg = null;
        BackChannelMsg.LoadUpdate loadMsg = null;
        BackChannelMsg.Register[] regs = null;

        WPacket wp = null;
        String ip = null;
        int port = 0;
        int clientPort = 0;

        switch (type) {
            case NamingBackChannelConstants.BCH_HELLO:
                ip = rp.readString();
                port = rp.readIntNx();
                clientPort = rp.readIntNx();
                if (!ipValidityChecker.isValid(ip)) {
                    GAlerter.lab("Received an unexpected backchannel ip!"
                            + " Will not logicProcess requests from this ip: " + ip);
                    break;
                }
                rns.setServiceAddress(new ServiceAddress(ip, port));
                rns.setClientAddress(new ServiceAddress(ip, clientPort));
                logger.debug("BackChannelMgr.packetArrived: processed hello: " + rns.getServiceAddress());
                p_backPing(rns);
                break;
            case NamingBackChannelConstants.BCH_REGISTER:
                loadInfo = (LoadInfo) rp.readSerializable();
                regMsg = (BackChannelMsg.Register) rp.readSerializable();
                rns.setLoadInfo(loadInfo);
                logger.debug("BackChannelMgr.packetArrived: Register msg: " + regMsg);
                backChannelListener.register(rns, regMsg);
                break;
            case NamingBackChannelConstants.BCH_UNREGISTER:
                loadInfo = (LoadInfo) rp.readSerializable();
                unRegisterMsg = (BackChannelMsg.UnRegister) rp.readSerializable();
                logger.debug("BackChannelMgr.packetArrived: Unregister msg: " + unRegisterMsg);
                rns.setLoadInfo(loadInfo);
                backChannelListener.unregister(rns, unRegisterMsg);
                break;
            case NamingBackChannelConstants.BCH_DUP_REGISTRATION:
                loadInfo = (LoadInfo) rp.readSerializable();
                regMsg = (BackChannelMsg.Register) rp.readSerializable();
                rns.setLoadInfo(loadInfo);
                logger.debug("BackChannelMgr.packetArrived: Dupe registration: " + regMsg);
                backChannelListener.dupRegistration(rns, regMsg);
                break;
            case NamingBackChannelConstants.BCH_PING:
                loadInfo = (LoadInfo) rp.readSerializable();
                long serverTime = ((Long) rp.readSerializable()).longValue();
                rns.ping(serverTime, loadInfo);
                break;
            case NamingBackChannelConstants.BCH_SYNCH:
                regs = (BackChannelMsg.Register[]) rp.readSerializable();
                backChannelListener.synch(rns, regs);
                break;
            case NamingBackChannelConstants.BCH_LOAD_UPDATE:
                loadInfo = (LoadInfo) rp.readSerializable();
                loadMsg = (BackChannelMsg.LoadUpdate) rp.readSerializable();
                rns.setLoadInfo(loadInfo);
                backChannelListener.loadUpdate(rns, loadMsg);
                break;
            default:
                GAlerter.lab("NamingService.BackChannelMgr: Unknown packet: " + type);
        }
        return wp;
    }

    /**
     * We send out a ping whenever somebody makes a conn to us. This way,
     * we synch up right away as opposed to waiting for the next ping
     * to be sent out.
     */
    private void p_backPing(RemoteNamingServer c) {
        BackChannelServiceWrap bchWrap = backChannelContainer.get(c.getServiceAddress());
        if (bchWrap != null) {
            bchWrap.send(new BackChannelRequest.Ping(backChannelListener.getLoadInfo()));
        } else {
            logger.error("BackChannelMgr.p_backPing: Could not backping! " + " addr = " + c.getServiceAddress());
        }

    }

    private void p_connMade(ConnThreadBase conn) {
        remoteNamingServersMap.put(conn, new RemoteNamingServer(conn));
    }

    private void p_connDied(ConnThreadBase conn) {
        synchronized (remoteNamingServersMap) {
            RemoteNamingServer rns = (RemoteNamingServer) remoteNamingServersMap.get(conn);
            if (rns != null) {
                backChannelListener.namingServerDied(rns);
                remoteNamingServersMap.remove(conn);
            }
        }
    }

    /**
     * Introduced so that we can shut this object down during an rtest.
     */
    void close() {
        if (serverThread != null) {
            serverThread.dieAll();
        }

        if (backChannelContainer != null) {
            backChannelContainer.close();
        }
    }
}
