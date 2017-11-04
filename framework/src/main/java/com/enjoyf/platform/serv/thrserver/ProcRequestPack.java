/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.util.thread.RequestThread;

/**
 * An implementation for processing packets.
 * Used in conjunction with ConnThreadPackReq class.
 */
public class ProcRequestPack implements RequestThread.Processor {
    private ConnThreadBase connThread = null;
    private PacketProcessor packetProcessor = null;
    private RPacketBase rPacket = null;

    public ProcRequestPack(ConnThreadBase ctb, PacketProcessor p, RPacketBase rp) {
        connThread = ctb;
        packetProcessor = p;
        rPacket = rp;
    }

    public void process() throws IOException {
        WPacketBase wp = packetProcessor.process(
                connThread, rPacket);
        if (wp != null) {
            connThread.write(wp);
        }
    }
}
