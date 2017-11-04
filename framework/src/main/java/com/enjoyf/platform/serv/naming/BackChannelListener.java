/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import java.util.Collection;

/**
 * A SenderListener that hooks up the Back Channel objects to the business logic.
 * The methods are invoked when messages are received by the backchannel,
 * <p/>
 * OR when the backchannel needs some info from the business logic to
 * respond appropriately to the other naming servers.
 */
interface BackChannelListener {
    /**
     * Invoked when a backchannel receives a registration from another
     * NS.
     */
    public void register(RemoteNamingServer rns, BackChannelMsg.Register msg);

    /**
     * Invoked when a backchannel receives an unregistration from another
     * NS.
     */
    public void unregister(RemoteNamingServer rns, BackChannelMsg.UnRegister msg);

    /**
     * Invoked when a backchannel receives a duplicate registration msg
     * from another NS. This means the business logic should remove
     * the referenced registration from its internal containers.
     */
    public void dupRegistration(RemoteNamingServer rns, BackChannelMsg.Register msg);

    /**
     * Called when an NS dies.
     */
    public void namingServerDied(RemoteNamingServer rns);

    /**
     * Called when a synch msg is received by the backchannel. This is
     * essentially equivalent to a register() call, except that we
     * batch up the registrations.
     */
    public void synch(RemoteNamingServer rns, BackChannelMsg.Register[] array);

    /**
     * Received when a peer sends a ServiceLoad update for a particular
     * service.
     */
    public void loadUpdate(RemoteNamingServer rns, BackChannelMsg.LoadUpdate msg);

    /**
     * Called to retrieve the RegistrantServerSideLocal objects registered with
     * this server. Used for synching up with the other NS's.
     */
    public Collection getLocalRegs();

    /**
     * Called to retrieve the LoadInfo from the business logic.
     */
    public LoadInfo getLoadInfo();
}
