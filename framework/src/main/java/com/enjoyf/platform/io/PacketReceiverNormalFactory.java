/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

public class PacketReceiverNormalFactory extends PacketReceiverFactory {
    
    public PacketReceiver allocate(Connection conn, PacketReader pr) {
        return new PacketReceiver(conn, pr);
    }
}
