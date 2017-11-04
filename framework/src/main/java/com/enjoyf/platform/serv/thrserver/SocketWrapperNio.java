/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.nio.channels.SocketChannel;

import com.enjoyf.platform.io.DataInputStreamWrapperNIOBuffer;
import com.enjoyf.platform.io.DataOutputStreamWrapperNIOBuffer;

/**
 * A wrapper class around a Socket-like object.  This is to support
 * both the java.io and java.nio schemes of doing socket connections,
 * since we may be accepting either object from our server.
 */

public class SocketWrapperNio extends SocketWrapperDef {
    private SocketChannel socketChannel = null;

    /**
     * Construct the object with a SocketChannel
     */
    SocketWrapperNio(SocketChannel channel, int inputBufferSize, int maxInputBufferSize, int outputBufferSize, int maxOutputBufferSize) {
        socketChannel = channel;

        if (channel != null) {
            socket = channel.socket();

            p_setAddresses(socket);
            p_setDataIO(socketChannel, inputBufferSize, maxInputBufferSize, outputBufferSize, maxOutputBufferSize);
        }
    }

    /**
     * Returns the underlying socket channel, if one exists (only for NIO mode).
     * You should only be using this method if you are in an NIO-only class.
     * The method exists so that we don't have to write wrapper functions
     * for all of SocketChannel's calls, and just ask that the callee make
     * the calls themselves.
     *
     * @return the underlying socket channel we are wrapping around
     */
    public SocketChannel getChannel() {
        return socketChannel;
    }

    /**
     * Sets our DataInput/Output members from a SocketChannel.
     * Actually, we're don't use the Channel itself currently, but it's
     * there if we need it.
     */
    private void p_setDataIO(SocketChannel channel, int inputBufferSize, int maxInputBufferSize, int outputBufferSize, int maxOutputBufferSize) {
        in = new DataInputStreamWrapperNIOBuffer(inputBufferSize, maxInputBufferSize);
        out = new DataOutputStreamWrapperNIOBuffer(outputBufferSize, maxOutputBufferSize);
    }
}
