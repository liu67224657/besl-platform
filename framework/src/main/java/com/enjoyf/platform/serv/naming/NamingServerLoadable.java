/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

/**
 * A hierarchy representing objects that have some notion of a load.
 */
public abstract class NamingServerLoadable implements LoadBalancer.Loadable {
    public abstract LoadInfo getLoadInfo();

    /**
     * This class represents the current naming server.
     */
    static class Local extends NamingServerLoadable {
        private LoadInfo loadInfo;

        Local(LoadInfo loadInfo) {
            this.loadInfo = loadInfo;
        }

        public LoadInfo getLoadInfo() {
            return loadInfo;
        }

        public String toString() {
            return "Local";
        }
    }

    /**
     * This class represents a remote naming server.
     */
    static class Remote extends NamingServerLoadable {
        private RemoteNamingServer remoteNamingServer;

        Remote(RemoteNamingServer rns) {
            remoteNamingServer = rns;
        }

        public RemoteNamingServer getRemoteNamingServer() {
            return remoteNamingServer;
        }

        public LoadInfo getLoadInfo() {
            return remoteNamingServer.getLoadInfo();
        }

        public String toString() {
            return "Remote:" + remoteNamingServer;
        }
    }
}
