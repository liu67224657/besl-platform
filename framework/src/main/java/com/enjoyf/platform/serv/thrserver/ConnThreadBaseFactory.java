/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

/**
 * An abstract class for all ConnThreadBaseFactory subclasses.
 * The actual implementation here isn't very useful, so clients
 * should use one of the subclasses.
 */
public interface ConnThreadBaseFactory {
    /**
     * Allocates a ConnThreadBase object from a SocketWrapper.
     *
     * @param wrap         The SocketWrapper object to allocate a connection from
     * @param useRunThread Set to true to start a Thread when opening the connection.
     * @return A ConnThreadBase object using that socket.
     */
    public ConnThreadBase allocate(SocketWrapper wrap, boolean useRunThread);

    /**
     * This routine is called when the owning ServerThread object
     * is about to die. It is intended to be overriden by
     * subclasses that need to do special cleanup.
     */
    public void die(ServerThread srv);
}
