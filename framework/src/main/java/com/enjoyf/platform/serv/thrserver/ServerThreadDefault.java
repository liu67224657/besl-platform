package com.enjoyf.platform.serv.thrserver;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.PacketReader;
import com.enjoyf.platform.io.PacketReaderAdvttl;
import com.enjoyf.platform.io.PacketWriter;
import com.enjoyf.platform.io.PacketWriterAdvttl;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * An intermediate class for factoring out common code.
 */
public class ServerThreadDefault extends ServerThread {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerThreadDefault.class);
    
    private int maxPacketLength = 0;

    private PacketProcessor packetProcessor;
    private PacketWriter packetWriter = new PacketWriterAdvttl();
    private PacketReader packetReader = new PacketReaderAdvttl();
    private WriteStrategy writeStrategy = new WriteStrategyDef();

    /**
     * Set the max packet length for incoming packets.
     */
    public void setMaxPacketLength(int maxLength) {
        maxPacketLength = maxLength;
    }

    public void setPacketWriter(PacketWriter pw) {
        packetWriter = pw;
    }

    protected PacketWriter getPacketWriter() {
        return packetWriter;
    }

    public void setPacketReader(PacketReader pr) {
        packetReader = pr;
    }

    protected PacketReader getPacketReader() {
        if (packetReader == null) {
            return null;
        }

        if (maxPacketLength > 0) {
            packetReader.setMaxPacketLength(maxPacketLength);
        }

        return packetReader;
    }

    protected int getMaxPacketLength() {
        return maxPacketLength;
    }

    public void setPacketProcessor(PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    protected PacketProcessor getPacketProcessor() {
        return packetProcessor;
    }

    /**
     * Return the write strategy object given a props object.
     */
    protected void setWriteStrategy(FiveProps props) {
        if (props == null) {
            return;
        }

        int nthreads = props.getInt("server.qwrite.nthreads", 0);

        //--
        // If this setting is missing or set to 0, we use a
        // normal WriteStrategy object.
        //--
        if (nthreads == 0) {
            return;
        }

        //--
        // The check interval in seconds, convert to msecs.
        //--
        int checkInterval = props.getInt("server.qwrite.checkInterval", 10) * 1000;
        int timeout = props.getInt("server.qwrite.timeout", 10) * 1000;

        QueuedSender sender = new QueuedSender(checkInterval, timeout, nthreads);
        sender.alertWedges(true);

        logger.debug("Establishing a queued write strategy: checkInterval=" + checkInterval + ":timeout=" + timeout + ":nthreads=" + nthreads);

        writeStrategy = new WriteStrategyQueued(sender);
    }

    protected WriteStrategy getWriteStrategy() {
        return writeStrategy;
    }

    protected void configurePort(String servicePrefix, FiveProps props) {
        //
        int port = props.getInt(servicePrefix + ".PORT");
        super.setPort(port);

        //
        String ip = props.get(servicePrefix + ".IP");
        InetAddress iaddr = null;
        if (ip != null) {
            try {
                iaddr = InetAddress.getByName(ip);
            }
            catch (UnknownHostException uhe) {
                GAlerter.lab("UnknownHost exception for ip: " + ip);
            }
        }

        if (iaddr != null) {
            super.setIp(iaddr);
        }
    }
}
