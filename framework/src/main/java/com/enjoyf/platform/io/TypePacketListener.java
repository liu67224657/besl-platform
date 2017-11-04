/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * This interface is to be implemented by clients of the Transactor class
 * whenever they want to be notified of packet arrival of a certain
 * type.
 * NOTE: The reality is that this won't work with arena server or
 * lobby server packets, due to our screwed-up backward compatibility
 * requirements.
 * <p/>
 * A client will implement this interface in whichever object he wants, and
 * then call Transactor.addTypeListener() to register a handler for a
 * particular type. When a packet comes in of the given type, the handler
 * will be invoked. The only way to receive these packets is by calling
 * the Transactor.react() method in some thread. This thread will block
 * until any packet that satisfies the TypePacketListener requirements is
 * received.
 */
public interface TypePacketListener {
    public void typedPacketArrived(RPacket p);
}
