/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import com.enjoyf.platform.util.FiveProps;

public class ServerSocketNioFactory implements ServerSocketFactory {
    private int inputBufferSize = 4096;
    private int outputBufferSize = 4096;
    private int maxInputBufferSize = 131072;
    private int maxOutputBufferSize = 131072;

    /**
     * We construct our NIO sockets with input and output buffer sizes, since
     * all reads and writes and buffered, so they can be written out upon
     * socket availability.
     */
    public ServerSocketNioFactory(int inputBufferSize, int maxInputBufferSize, int outputBufferSize, int maxOutputBufferSize) {
        this.inputBufferSize = inputBufferSize;
        this.maxInputBufferSize = maxInputBufferSize;
        this.outputBufferSize = outputBufferSize;
        this.maxOutputBufferSize = maxOutputBufferSize;
    }

    /**
     * Creates a ServerSocketWrapper contianing a ServerSocketChannel.
     *
     * @param port The port to create the ServerSocketChannel on
     */
    public ServerSocketWrapper createSocket(int port, FiveProps props) {
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();

            // This factory creates BLOCKING ServerSocketChannels
            channel.configureBlocking(true);

            // bind our socket to a port
            InetSocketAddress isa = new InetSocketAddress(port);
            channel.socket().bind(isa);

            return new ServerSocketWrapperNio(channel, inputBufferSize, maxInputBufferSize, outputBufferSize, maxOutputBufferSize);
        } catch (IOException e) {
            // this is usually bad news
            return null;
        }
    }

    /**
     * Variant to specify an alternative IP.
     *
     * @param iaddr       The ip on which to listen on.
     * @param port        The port on which to listen on.
     * @param maxBlockers How many connect requests to allow to queue
     *                    up before the underlying jdk returns an error to the client.
     *                    This is primarily here because the java api which takes an
     *                    InetAddress also requires it.
     */
    public ServerSocketWrapper createSocket(InetAddress iaddr, int port, int maxBlockers, FiveProps props) {
        throw new UnsupportedOperationException("Sorry, no support for this variant right now");
    }
}
