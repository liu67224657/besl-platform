/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.service.service.ServiceLoad;

/**
 * The classes in here represent a hierarchy of msgs sent by an NS
 * to other NS's via the back channel.
 * BackChannelMsg is abstract.
 */
class BackChannelMsg implements java.io.Serializable {
    protected ServerRegInfo serverRegInfo;

    protected BackChannelMsg(ServerRegInfo regInfo) {
        serverRegInfo = regInfo;
    }

    public ServerRegInfo getSrvRegInfo() {
        return serverRegInfo;
    }

    public String toString() {
        return serverRegInfo.toString();
    }

    /**
     * This class represents a registration msg.
     */
    static class Register extends BackChannelMsg {
        public Register(ServerRegInfo serverRegInfo) {
            super(serverRegInfo);
        }

        public String toString() {
            return "REGMSG:" + super.toString();
        }
    }

    /**
     * This class represents an unregistration msg.
     */
    static class UnRegister extends BackChannelMsg {
        private boolean forRebalanceFlag = false;

        public UnRegister(ServerRegInfo serverRegInfo) {
            this(serverRegInfo, false);
        }

        public UnRegister(ServerRegInfo serverRegInfo, boolean forRebalance) {
            super(serverRegInfo);
            forRebalanceFlag = forRebalance;
        }

        public boolean isForRebalance() {
            return forRebalanceFlag;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer("UNREGMSG:");
            sb.append(super.toString());
            sb.append(":forRebalance=" + forRebalanceFlag);
            return new String(sb);
        }
    }

    /**
     * This class represents a ping with a load update.
     */
    static class LoadUpdate extends BackChannelMsg {
        private ServiceLoad serviceLoad;

        public LoadUpdate(ServerRegInfo serverRegInfo, ServiceLoad load) {
            super(serverRegInfo);
            serviceLoad = load;
        }

        public ServiceLoad getLoad() {
            return serviceLoad;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer("LOAD_UPDATE:");
            sb.append(super.toString());
            sb.append(":load=" + serviceLoad);
            return new String(sb);
        }
    }
}
