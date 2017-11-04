/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

//
public abstract class PacketReceiverFactory {
    //
    public abstract PacketReceiver allocate(Connection conn, PacketReader pr);
}
