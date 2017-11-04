/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.enjoyf.platform.io.DataInputStreamWrapperNIOBuffer;
import com.enjoyf.platform.util.log.GAlerter;

public class NioReaderThread extends Thread {
    // Direct byte buffer for reading
    private ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);

    private Collection<NioRegistrationReq> readRegistrations = null;
    private Selector selector = null;

    /**
     * Constructor
     */
    public NioReaderThread(Selector selector, Collection<NioRegistrationReq> readRegistrations) {
        this.selector = selector;
        this.readRegistrations = readRegistrations;
    }

    /**
     * The body of the thread.  Reads data from all available sockets, and buffers the data for each
     * ConnThreadBase.  Then it calls
     */
    public void run() {
        boolean isReadable = false;
        int numBytes = 0;
        int selected = 0;

        Iterator<NioRegistrationReq> registrationIter = null;
        SocketChannel currSock = null;
        ConnThreadBase currConn = null;

        // initial select call to get things started (the select call blocks until
        // there's something to select)
        try {
            selected = selector.select();
        }
        catch (IOException e) {
            GAlerter.lab("NioReaderThread: caught IOException on initial select ", e);
        }

        // Main running loop
        while (true) {
            SelectionKey key;
	    if (selected > 0) {
                // Get the set of ready-to-read channels
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> readyIter = readyKeys.iterator();

                // iterate through the channels
                while (readyIter.hasNext()) {
                    // The set is always made of SelectionKeys
                    key = readyIter.next();

                    // Remove current entry, since we are processing it.
                    // Otherwise it may be present next time
                    readyIter.remove();

                    try {
                        isReadable = key.isReadable();
                    }
                    catch (CancelledKeyException e) {
                        GAlerter.lab("NioReaderThread: key=" + key + " has been cancelled.");
                        isReadable = false;
                        break;
                    }

                    // make sure we got the right kind of key
                    if (isReadable) {
                        // Get the channel and ConnThreadBase out of the key
                        currSock = (SocketChannel) key.channel();
                        currConn = (ConnThreadBase) key.attachment();

                        // Reset our buffer, and read into it
                        dbuf.clear();
                        try {
                            numBytes = currSock.read(dbuf);
                            if (numBytes < 0) {
                                try {
                                    // conn is dead
                                    currConn.die();
                                }
                                catch (Throwable err) {
                                }
                            } else {
                                dbuf.flip();
                                p_fillBuffer(key, dbuf);
                            }

                            // if we completely filled our buffer, we may have more than
                            // one buffer's worth of bytes.  loop to read them all, and
                            // logicProcess them one bufferfull at a time.
                            while (numBytes >= dbuf.capacity()) {
                                numBytes = currSock.read(dbuf);
                                if (numBytes > 0) {
                                    // only fill our buffer if we got data
                                    dbuf.flip();
                                    p_fillBuffer(key, dbuf);
                                } else {
                                    // no data (fencepost issue), don't rebuffer the old data
                                    dbuf.clear();
                                }
                            }
                        }
                        catch (IOException e) {
                            GAlerter.lab("NioReaderThread: caught IOException :: "
                                    + " processing socketChannel ", currSock.toString(), e);
                        }

                        // Ok, now we've filled our buffer with data.  The conn now
                        // tries to read a packet out of the buffer
                        if (!currConn.read()) {
                            // if the read returns false, we die
                            currConn.die();
                        }
                    } else {
                        // we have the wrong kind of key for some reason
                        GAlerter.lab("NioReaderThread: key " + key + " is not readable");
                    }
                }
            }

            //--
            // Finished our reading; see if we have any socket
            // connections to register on the queue
            //--
            synchronized (readRegistrations) {
                registrationIter = readRegistrations.iterator();
                while (registrationIter.hasNext()) {
                    try {
                        // pop a request off the queue
                        NioRegistrationReq req = registrationIter.next();
                        currSock = req.getSocketChannel();

                        // register the socket channel
                        // the selector will block on this if in use, so make sure no one
                        // else is using this selector
                        key = currSock.register(selector, SelectionKey.OP_READ);

                        // attach the conn (so we can get it back when this socket is selected)
                        key.attach(req.getConn());
                    }
                    catch (ClosedChannelException e) {
                        GAlerter.lab("NioReaderThread: could not regsiter SocketChannel " + currSock + " because it is closed");
                    }
                    catch (Exception e) {
                        GAlerter.lab("NioReaderThread: caught exception, skipping registration for channel ", currSock.toString(),e);
                    }
                    finally {
                        // no matter if we fail or succeed, don't attempt to re-register this guy
                        registrationIter.remove();
                    }
                }
            }

            // looping; select again
            try {
                selected = selector.select();
            }
            catch (IOException e) {
                selected = 0;
                GAlerter.lab("NioReaderThread: caught IOException during selection", e);
            }
        }
    }

    private void p_fillBuffer(SelectionKey key, ByteBuffer data) {
        ConnThreadBase conn = (ConnThreadBase) key.attachment();

        // the NioReaderThread expects that the conn's DataInput is an DataInputStreamWrapperNIOBuffer
        DataInputStreamWrapperNIOBuffer inBuff = (DataInputStreamWrapperNIOBuffer) conn.getDataInputCustom();

        try {
            synchronized (inBuff) {
                inBuff.put(data);
            }
        }
        catch (IOException e) {
            // this is because our buffer has exceeded its maximum capacity.
            // we need to kill the conn
            GAlerter.lab("NioReaderThread.p_fillBuffer: buffer exceeded max capacity.  Killing conn to " + conn.getIp());
            conn.die();
        }
    }
}
