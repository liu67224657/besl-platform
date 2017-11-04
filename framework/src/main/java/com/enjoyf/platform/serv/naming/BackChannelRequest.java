/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.Request;

/**
 * This class represents a request sent from one NS to another via
 * the backchannel.
 */
abstract class BackChannelRequest {
    protected LoadInfo loadInfo;

    BackChannelRequest(LoadInfo info) {
        loadInfo = info;
    }

    abstract Request getRequest();

    /**
     * This represents a registration msg.
     */
    static class Register extends BackChannelRequest {
        private BackChannelMsg.Register bchMsg;

        Register(LoadInfo loadInfo, BackChannelMsg.Register regMsg) {
            super(loadInfo);
            bchMsg = regMsg;
        }

        Request getRequest() {
            WPacket wp = new WPacket();
            wp.writeSerializable(loadInfo);
            wp.writeSerializable(bchMsg);
            Request req = new Request(NamingBackChannelConstants.BCH_REGISTER, wp);
            req.setBlocking(false);
            return req;
        }

        public String toString() {
            return "REGISTER:" + bchMsg + ":" + loadInfo;
        }
    }

    /**
     * This represents an unregistration msg.
     */
    static class Unregister extends BackChannelRequest {
        private BackChannelMsg.UnRegister bchMsg;

        Unregister(LoadInfo loadInfo, BackChannelMsg.UnRegister regMsg) {
            super(loadInfo);
            bchMsg = regMsg;
        }

        Request getRequest() {
            WPacket wp = new WPacket();
            wp.writeSerializable(loadInfo);
            wp.writeSerializable(bchMsg);
            Request req = new Request(NamingBackChannelConstants.BCH_UNREGISTER, wp);
            req.setBlocking(false);
            return req;
        }

        public String toString() {
            return "UNREGISTER:" + bchMsg + ":" + loadInfo;
        }
    }

    /**
     * This represents a ping.
     */
    static class Ping extends BackChannelRequest {
        Ping(LoadInfo loadInfo) {
            super(loadInfo);
        }

        Request getRequest() {
            WPacket wp = new WPacket();
            wp.writeSerializable(loadInfo);
            wp.writeSerializable(new Long(System.currentTimeMillis()));
            Request req = new Request(NamingBackChannelConstants.BCH_PING, wp);
            req.setBlocking(false);
            return req;
        }

        public String toString() {
            return "PING:" + ":" + loadInfo;
        }
    }

    /**
     * This represends a duplicate registration.
     */
    static class DupRegistration extends BackChannelRequest {
        private BackChannelMsg.Register bchMsg;

        DupRegistration(LoadInfo loadInfo, BackChannelMsg.Register regMsg) {
            super(loadInfo);
            bchMsg = regMsg;
        }

        Request getRequest() {
            WPacket wp = new WPacket();
            wp.writeSerializable(loadInfo);
            wp.writeSerializable(bchMsg);
            Request req = new Request(NamingBackChannelConstants.BCH_DUP_REGISTRATION, wp);
            req.setBlocking(false);
            return req;
        }

        public String toString() {
            return "DUP_REGISTRATION:" + bchMsg + ":" + loadInfo;
        }
    }

    /**
     * This represents a service load update. Ie, some service pinged
     * the owning naming server with a load, and it in turn broadcast
     * the load of the service to the other naming servers.
     */
    static class LoadUpdate extends BackChannelRequest {
        private BackChannelMsg.LoadUpdate bchMsg;

        LoadUpdate(LoadInfo loadInfo, BackChannelMsg.LoadUpdate msg) {
            super(loadInfo);
            bchMsg = msg;
        }

        Request getRequest() {
            WPacket wp = new WPacket();
            wp.writeSerializable(loadInfo);
            wp.writeSerializable(bchMsg);
            Request req = new Request(NamingBackChannelConstants.BCH_LOAD_UPDATE, wp);
            req.setBlocking(false);
            return req;
        }

        public String toString() {
            return "LOAD_UPDATE:" + ":" + bchMsg;
        }
    }
}
