/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.io.PacketReaderBase;
import com.enjoyf.platform.io.PacketWriterBase;

public class ConnThreadFactoryDefault implements ConnThreadBaseFactory {
    private PacketWriterBase packetWriter = null;
    private PacketReaderBase packetReader = null;
    private PacketHandler packetHandler = null;
    private WriteStrategy writeStrategy;

    public ConnThreadFactoryDefault(PacketWriterBase pw, PacketReaderBase pr, PacketHandler ph) {
        this(pw, pr, ph, new WriteStrategyDef());
    }

    public ConnThreadFactoryDefault(PacketWriterBase pw, PacketReaderBase pr, PacketHandler ph, WriteStrategy strategy) {
        packetWriter = pw;
        packetReader = pr;
        packetHandler = ph;
        writeStrategy = strategy;
    }

    /**
     * Allocates a ConnThreadDefault object from a SocketWrapper.  Uses the packet reader, writer,
     * and handler objects given to this factory.
     *
     * @param wrap         The SocketWrapper object to allocate a connection from
     * @param useRunThread Set to true to create a RunThread for the ConnThreadDefault.
     * @return A ConnThreadDefault using that socket.
     */
    public ConnThreadBase allocate(SocketWrapper wrap, boolean useRunThread) {
        if (wrap == null) {
            return null;
        }

        ConnThreadDefault conn = new ConnThreadDefault(wrap, packetWriter, packetReader, packetHandler);
        if (useRunThread) {
            conn.setThread(new ConnThreadDefault.RunThread(conn));
        }
        conn.setWriteStrategy(writeStrategy);

        return conn;
    }

    public void die(ServerThread srv) {
    }
}
