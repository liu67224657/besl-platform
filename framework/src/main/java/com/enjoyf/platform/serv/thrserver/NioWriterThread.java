package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.enjoyf.platform.io.DataOutputStreamWrapperNIOBuffer;
import com.enjoyf.platform.util.log.GAlerter;

public class NioWriterThread extends Thread {

    private Collection<NioRegistrationReq> writeRegistrations = null;
    private Selector selector = null;

    /**
     * Constructor
     */
    public NioWriterThread(Selector selector, Collection<NioRegistrationReq> writeRegistrations) {
        this.selector = selector;
        this.writeRegistrations = writeRegistrations;
    }

    /**
     * The body of the thread.  Reads data from all available sockets, and buffers the data for each
     * ConnThreadBase.  Then it calls
     */
    public void run() {
        boolean isWritable = false;
        int selected = 0;
        SelectionKey key = null;
        SocketChannel currSock = null;
        ConnThreadBase currConn = null;
        NioRegistrationReq req = null;

        // initial select call to get things started (the select call blocks until
        // there's something to select)
        try {
            selected = selector.select();
        }
        catch (IOException e) {
            GAlerter.lab("NioWriterThread: caught IOException on initial select ", e);
        }
        catch (CancelledKeyException cke) {
            //--
            // This is to work around an NIO bug.
            //--
            GAlerter.lab("Caught a cancelled key exception! ignoring: ", cke);
        }
        // Main running loop
        while (true) {
            if (selected > 0) {
                // Get the set of ready-to-write channels
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
                        isWritable = key.isWritable();
                    }
                    catch (CancelledKeyException e) {
                        GAlerter.lab("NioWriterThread: key=" + key + " has been cancelled.");
                        isWritable = false;
                        break;
                    }

                    // make sure we got the right kind of key
                    if (isWritable) {
                        // Get the channel and ConnThreadBase out of the key
                        currSock = (SocketChannel) key.channel();
                        currConn = (ConnThreadBase) key.attachment();

                        // flush our buffer
                        p_flushToChannel(currConn, currSock);
                    } else {
                        // we have the wrong kind of key for some reason
                        GAlerter.lab("NioWriterThread: error, key is not writiable");
                    }
                }
            }

            // Finished our writing; see if we have any socket connections to register on the queue
            synchronized (writeRegistrations) {
                Iterator<NioRegistrationReq> registrationIter = writeRegistrations.iterator();
                req = null;
                while (registrationIter.hasNext()) {
                    try {
                        // pop a request off the queue
                        req = registrationIter.next();
                        currSock = req.getSocketChannel();

                        // register the socket channel
                        // the selector will block on this if in use, so make sure no one
                        // else is using this selector
                        key = currSock.register(selector, SelectionKey.OP_WRITE);

                        // attach the conn (so we can get it back when this socket is selected)
                        key.attach(req.getConn());
                    }
                    catch (ClosedChannelException e) {
                        GAlerter.lab("NioWriterThread: could not register SocketChannel " + currSock
                                + " because it is closed");
                    }
                    catch (Exception e) {
                        GAlerter.lab("NioWriterThread: caught exception "
                                + ", skipping registration for channel ", currSock.toString(), e);
                    }
                    finally {
                        // no matter if we succeed or fail, don't attempt to re-register this guy
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
                GAlerter.lab("NioWriterThread: caught IOException during selection", e);
            }
            catch (CancelledKeyException cke) {
                //--
                // This is to hopefully work around an NIO bug.
                //--
                GAlerter.lab("Caught a cancelled key exception! ignoring: ", cke);
            }
        }
    }

    private void p_flushToChannel(ConnThreadBase conn, SocketChannel channel) {
        // the NioWriterThread expects that the conn's DataOutput is an DataOutputStreamWrapperNIOBuffer
        DataOutputStreamWrapperNIOBuffer outBuff = (DataOutputStreamWrapperNIOBuffer) conn.getDataOutputCustom();

        try {
            synchronized (outBuff) {
                outBuff.flushToChannel(channel);
            }
        }
        catch (IOException e) {
            // this is because our buffer has exceeded its maximum capacity.
            // we need to kill the conn
            GAlerter.lab("NioWriterThread.p_flush: buffer exceeded max capacity.  Killing conn to "
                    + conn.getIp());
            conn.die();
        }
    }
}
