/**
 * Copyright (c) EA.com 2001.  All rights reserved.
 */

package com.enjoyf.platform.serv.thrserver;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.enjoyf.platform.io.DataInputCustom;
import com.enjoyf.platform.io.DataInputStreamWrapper;
import com.enjoyf.platform.io.DataOutputCustom;
import com.enjoyf.platform.io.DataOutputStreamWrapper;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A wrapper class around a Socket-like object.  This is to support
 * both the java.io and java.nio schemes of doing socket connections,
 * since we may be accepting either object from our server.
 */

public class SocketWrapperIo extends SocketWrapperDef {
    /**
     * Construct the object with a Socket.
     *
     * @param socket The socket to constuct this wrapper with.
     */
    public SocketWrapperIo(Socket socket) {
        this.socket = socket;
        if (this.socket == null) {
            return;
        }

        p_setAddresses(this.socket);
    }


    /**
     * Returns the DataInputCustom from which we read.
     * Be careful, as the input is live and any reads from it will
     * disrupt reads by this object.  We need to return this object,
     * because under the NIO scheme the input is really a buffer, and
     * we need some method to fill the buffer with data as we get it.
     *
     * @return The DataInputCustom this conn reads from.
     */
    public DataInputCustom getDataInputCustom() {
        synchronized (this) {
            if (in == null) {
                try {
                    in = new DataInputStreamWrapper(new DataInputStream(
                            socket.getInputStream()));
                }
                catch (IOException e) {
                    //--
                    // If we can't get the input/output streams, close the
                    // socket since it's no good.
                    //--
                    GAlerter.lab("SocketWrapper.p_setDataIO: can't get input/output "
                            + " streams from Socket.  Closing.");
                    try {
                        socket.close();
                    }
                    catch (IOException e2) {
                    }
                }
            }
            return in;
        }
    }

    /**
     * Returns the DataOutputCustom to which we write.
     * Be careful, as the output is live and any writes to it will
     * send data out to the client in an unpredictable fashion (as we
     * may be writing to it concurrently).  We need to return this object,
     * because under the NIO scheme the output is really a buffer, and
     * we need some method to get data out of the buffer as the socket
     * becomes available for writing.
     *
     * @return The DataOutputCustom we write to.
     */
    public DataOutputCustom getDataOutputCustom() {
        synchronized (this) {
            if (out == null) {
                try {
                    out = new DataOutputStreamWrapper(new DataOutputStream(
                            new BufferedOutputStream(socket.getOutputStream())));
                }
                catch (IOException e) {
                    //--
                    // If we can't get the input/output streams, close the
                    // socket since it's no good.
                    //--
                    GAlerter.lab("SocketWrapper.p_setDataIO: can't get input/output "
                            + " streams from Socket.  Closing.");
                    try {
                        socket.close();
                    }
                    catch (IOException e2) {
                    }
                }
            }
            return out;
        }
    }
}
