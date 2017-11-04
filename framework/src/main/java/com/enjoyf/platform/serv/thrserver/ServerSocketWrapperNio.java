/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * The concrete implementation for NIO.
 */

public class ServerSocketWrapperNio extends ServerSocketWrapperDef {
    private ServerSocketChannel serverSocketChannel = null;
    private int inputBufferSize = 4096;
    private int outputBufferSize = 4096;
    private int maxInputBufferSize = 131072;
    private int maxOutputBufferSize = 131072;

    public ServerSocketWrapperNio(ServerSocketChannel channel, int inputBufferSize, int maxInputBufferSize, int outputBufferSize, int maxOutputBufferSize) {
        serverSocketChannel = channel;
        serverSocket = channel.socket();

        this.inputBufferSize = inputBufferSize;
        this.outputBufferSize = outputBufferSize;
        this.maxInputBufferSize = maxInputBufferSize;
        this.maxOutputBufferSize = maxOutputBufferSize;
    }

    /**
     * Perform the accept() call.
     * A ServerSocket will always block until we get a Connection, but a
     * ServerSocketChannel will only block if it is set to blocking mode.
     *
     * @throws IOException Thrown if there is a network error.
     */
    public SocketWrapper accept() throws IOException {
        SocketChannel channel = serverSocketChannel.accept();

        return new SocketWrapperNio(channel, inputBufferSize, maxInputBufferSize, outputBufferSize, maxOutputBufferSize);
    }
}
