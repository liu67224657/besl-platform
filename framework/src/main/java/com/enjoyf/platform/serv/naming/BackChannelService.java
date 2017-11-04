/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import java.util.Collection;
import java.util.Iterator;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.ConnChooser;
import com.enjoyf.platform.service.service.ConnChooserRR;
import com.enjoyf.platform.service.service.GreetInfo;
import com.enjoyf.platform.service.service.Greeter;
import com.enjoyf.platform.service.service.GreeterDefault;
import com.enjoyf.platform.service.service.ProcessStrategyOnce;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConn;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceFinderOne;
import com.enjoyf.platform.service.service.Syncher;

/**
 * This class represents the connection made by this NS to some other NS.
 * All messages originating in this NS are sent via this object to some
 * specific NS.
 */
class BackChannelService {
    private ReqProcessor reqProcessor;
    private RegInfoCallback callback;
    private final int CHUNK_SIZE = 100;
    private int servicePort;

    /**
     * This is the service address of the remote naming server we will
     * be communicating with.
     */
    private ServiceAddress remoteServiceAddress;

    /**
     * @param cbk        The callback for retrieving the RegistrantServerSideLocal
     *                   objects from the business logic. Used for synching up when the
     *                   conn is established.
     * @param myAddr     This is the address of this server so that
     *                   the other NS can talk to it.
     * @param clientPort This is the port used by clients.
     * @param saddr      This is the address of the destination NS.
     */
    BackChannelService(RegInfoCallback cbk, ServiceAddress myAddr, int clientPort, ServiceAddress saddr) {
        callback = cbk;
        servicePort = clientPort;
        remoteServiceAddress = saddr;

        ConnChooser chooser = new ConnChooserRR(30 * 1000);
        chooser.setServiceFinder(new ServiceFinderOne(saddr));

        reqProcessor = new ReqProcessor(chooser, new ProcessStrategyOnce());

        //--
        // Use a greeter to identify ourselves to the naming service
        // we are communicating with. We also set a Syncher object,
        // so that we can synch up when a new connection is established.
        //--
        Greeter greeter = new GreeterDefault(new MyGreetInfo(myAddr));
        greeter.setSyncher(
                new Syncher() {
                    public void synchUp(ServiceConn sconn, Greeter g) throws ServiceException {
                        p_synchUp(sconn);
                    }
                }
        );

        reqProcessor.setGreeter(greeter);
    }

    ServiceAddress getAddress() {
        return remoteServiceAddress;
    }

    /**
     * An interface for retrieving reg info during synch up.
     */
    interface RegInfoCallback {
        public Collection<RegistrantServerSide> getLocalRegs();
    }

    class MyGreetInfo implements GreetInfo {
        private Request m_req;

        MyGreetInfo(ServiceAddress saddr) {
            WPacket wp = new WPacket();
            wp.writeString(saddr.getAddr());
            wp.writeIntNx(saddr.getPortInt());
            wp.writeIntNx(servicePort);
            m_req = new Request(NamingBackChannelConstants.BCH_HELLO, wp);
            m_req.setBlocking(false);
        }

        public Request getInitHello() {
            m_req.setBlocking(false);
            m_req.setServiceAddress(null);
            return m_req;
        }

        public Request getReconHello() {
            return getInitHello();
        }
    }

    /**
     * Utility function to synch up with a newly connected naming server.
     */
    private void p_synchUp(ServiceConn sconn) throws ServiceException {
        Collection<RegistrantServerSide> c = callback.getLocalRegs();
        int count = c.size();
        Iterator<RegistrantServerSide> itr = c.iterator();
        BackChannelMsg.Register[] array = new BackChannelMsg.Register[count];

        for (int i = 0; i < array.length; i++) {
            RegistrantServerSideLocal r = (RegistrantServerSideLocal) itr.next();
            array[i] = new BackChannelMsg.Register(r.getServerRegInfo());
        }
        //--
        // Dole out the synch packets in chunks, don't try to send
        // everything in one packet.
        //--
        int i = 0;
        BackChannelMsg.Register[] buffer = null;
        while (i < count) {
            int len = Math.min(CHUNK_SIZE, count - i);
            if (buffer == null || len != buffer.length) {
                buffer = new BackChannelMsg.Register[len];
            }

            System.arraycopy(array, i, buffer, 0, len);
            WPacket wp = new WPacket();
            wp.writeSerializable(buffer);
            Request req = new Request(NamingBackChannelConstants.BCH_SYNCH, wp);
            req.setBlocking(false);
            sconn.process(req);
            i += len;
        }
    }

    /**
     * This is the method to use when sending a msg to the other NS.
     */
    void send(BackChannelRequest req) throws ServiceException {
        reqProcessor.process(req.getRequest());
    }

    void close() {
        if (reqProcessor != null) {
            reqProcessor.close();
        }
    }
}
