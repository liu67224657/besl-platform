/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

/**
 * A class that represents a RegistrantServerSide that is "remote", meaning it
 * is registered with some other service.(the RegistrantServerSide oauth from other NS)
 */
class RegistrantServerSideRemote extends RegistrantServerSide {
    private RemoteNamingServer remoteNamingServer;

    RegistrantServerSideRemote(RemoteNamingServer rns, ServerRegInfo regInfo) {
        super(regInfo);
        remoteNamingServer = rns;
    }

    RemoteNamingServer getRemoteNamingServer() {
        return remoteNamingServer;
    }

    boolean isLocal() {
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(":remoteNamingServer=" + remoteNamingServer);
        return new String(sb);
    }
}
