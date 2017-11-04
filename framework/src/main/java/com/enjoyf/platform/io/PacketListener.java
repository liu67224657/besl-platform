/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * Interface to be implemented by the Transactor object. The PacketReceiver
 * thread notifies the transactor of any occurence via this interface.
 * Note that while this is a public class, it should
 * really be package private, but java syntax requires otherwise.
 *
 * @see Transactor
 */
public interface PacketListener {
    /**
     * This method will be called when a packet arrives.
     */
    public void packetArrived(RPacketBase p);

    /**
     * Called when an error was detected reading.
     */
    public void errorNotify(Connection conn);
}
