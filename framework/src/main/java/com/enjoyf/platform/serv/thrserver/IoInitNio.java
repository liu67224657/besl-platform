/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A class to help with initialization for a server running NIO.
 * Useful for keeping all of our NIO-specific objects in one bag,
 * so that we can compile in jdk's less than 1.4 by simply editing
 * this class.
 */
public class IoInitNio implements IoInit {
    private static final String INITIAL_INPUT_BUFFER_SIZE_PROP = "nio.buffer.input.size.initial";
    private static final String INITIAL_OUTPUT_BUFFER_SIZE_PROP = "nio.buffer.output.size.initial";
    private static final String MAX_INPUT_BUFFER_SIZE_PROP = "nio.buffer.input.size.max";
    private static final String MAX_OUTPUT_BUFFER_SIZE_PROP = "nio.buffer.output.size.max";

    private int initialInputBufferSize = 4096;
    private int initialOutputBufferSize = 4096;
    private int maxInputBufferSize = 131072;
    private int maxOutputBufferSize = 131072;
    private LinkedList<NioRegistrationReq> readRegistrations = null;
    private LinkedList<NioRegistrationReq> writeRegistrations = null;
    private Selector readSelector = null;
    private Selector writeSelector = null;

    public IoInitNio() {
    }

    public void init(FiveProps servProps) {
        initialInputBufferSize = servProps.getInt(INITIAL_INPUT_BUFFER_SIZE_PROP, 4096);
        initialOutputBufferSize = servProps.getInt(INITIAL_OUTPUT_BUFFER_SIZE_PROP, 4096);
        maxInputBufferSize = servProps.getInt(MAX_INPUT_BUFFER_SIZE_PROP, 131072);
        maxOutputBufferSize = servProps.getInt(MAX_OUTPUT_BUFFER_SIZE_PROP, 131072);

        readRegistrations = new LinkedList<NioRegistrationReq>();
        writeRegistrations = new LinkedList<NioRegistrationReq>();

        try {
            readSelector = Selector.open();
            writeSelector = Selector.open();
        }
        catch (IOException e) {
            // fatal error if we can't open Selectors
            GAlerter.lab("IoInitNio: can't open Selectors because of exception :: " + e);

            throw new RuntimeException("COULD NOT INITIALIZE NIO!!");
        }

    }

    public ServerSocketFactory makeServerSocketFactory() {
        return new ServerSocketNioFactory(initialInputBufferSize, maxInputBufferSize, initialOutputBufferSize, maxOutputBufferSize);
    }

    public void openConn(ConnThreadBase conn, SocketWrapper socket) {
        // add the read registration
        SocketChannel channel = ((SocketWrapperNio) socket).getChannel();

        //the read
        NioRegistrationReq rreq = new NioRegistrationReq(SelectionKey.OP_READ, conn, channel);
        synchronized (readRegistrations) {
            readRegistrations.add(rreq);
        }

        //the write
        NioRegistrationReq wreq = new NioRegistrationReq(SelectionKey.OP_WRITE, conn, channel);
        synchronized (writeRegistrations) {
            writeRegistrations.add(wreq);
        }

        // configure the conn to non-blocking mode
        try {
            channel.configureBlocking(false);
        }
        catch (IOException e) {
            GAlerter.lab("IoInitNio.openReadConn: can't configure channel to non-blocking because of IOException :: " + e);
        }

        // nudge our read and write selectors if they're sleeping
        readSelector.wakeup();
        writeSelector.wakeup();
    }

    public void startup() {

        NioReaderThread rt = new NioReaderThread(readSelector, readRegistrations);
        rt.setName("NioReaderThread");
        rt.start();

        NioWriterThread wt = new NioWriterThread(writeSelector, writeRegistrations);
        wt.setName("NioWriterThread");
        wt.start();
    }
}
