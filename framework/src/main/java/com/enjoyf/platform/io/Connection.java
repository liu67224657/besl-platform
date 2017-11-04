package com.enjoyf.platform.io;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.KeepAlive;

/**
 * A generic connection class for managing a socket.
 * Used by Transactor class, but really can be used by anybody.
 */
public class Connection {
	
	private static final Logger logger = LoggerFactory.getLogger(Connection.class);
	
    //the next id seed.
    private static int nextId = 1;

    //
    private int connId;
    private Socket socket = null;

    //
    private DataOutputStream outputStrean = null;
    private DataInputStream inputStream = null;

    private InputStream baseInputStream = null;
    private OutputStream baseOutputStream = null;

    private ServiceAddress serviceAddress = null;

    //
    private boolean keepAliveFlag = false;
    private boolean connectedFlag = false;

    /////////////////////////////////////////////////////////////////
    public Connection(ServiceAddress saddr) {
        this(saddr, false);
    }

    /**
     * Constructor
     *
     * @param saddr     the address to connect to
     * @param keepAlive specifies if we want to use keepAlive
     * @param useSSL    specifies if we want secure sockets (SSL)
     */
    public Connection(ServiceAddress saddr, boolean keepAlive) {
        connId = getNextId();
        serviceAddress = saddr;
        keepAliveFlag = keepAlive;
    }

    /////////////////////////////////////////////////////////////////
    public String getIp() {
        return serviceAddress != null ? serviceAddress.getAddr() : "unset";
    }

    public boolean isConnected() {
        return socket != null && connectedFlag;
    }

    public int getId() {
        return connId;
    }

    /**
     * Create and establish a connection.
     *
     * @throws TtlException Thrown if we had a problem connecting.
     */
    public synchronized void connect() throws TtlException {
    	
        if (logger.isDebugEnabled()) {
        	logger.debug("Connection.connect: Establishing conn to: " + this);
        }

        try {
            socket = new Socket(serviceAddress.getInetAddress(), serviceAddress.getPortInt());

            if (keepAliveFlag) {
                KeepAlive.setKeepAlive(socket, true);
            }

            //
            baseOutputStream = socket.getOutputStream();
            outputStrean = new DataOutputStream(new BufferedOutputStream(baseOutputStream));

            //
            baseInputStream = socket.getInputStream();
            inputStream = new DataInputStream(baseInputStream);

            //
            connectedFlag = true;
        }
        catch (IOException e) {
            close();
            logger.error("Connection.ConnThread: Could not connect: " + e);
            throw new TtlException(TtlException.CONNECT, "initial connect", e);
        }
        catch (Exception e) {
            close();
            logger.error("Connection.ConnThread: Could not connect: " + e);
            throw new TtlException(TtlException.CONNECT, "initial connect");
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Connection.ctor: Successful connection to: " + this);
        }
    }

    /**
     * Return the output stream associated with this connection.
     */
    public DataOutputStream getOutputStream() {
        return outputStrean;
    }

    public OutputStream getBaseOutputStream() {
        return baseOutputStream;
    }

    /**
     * Return the input stream associated with this connection.
     */
    public DataInputStream getInputStream() {
        return inputStream;
    }

    public InputStream getBaseInputStream() {
        return baseInputStream;
    }

    /**
     * Close the connection.
     */
    public synchronized void close() {
        connectedFlag = false;

        if (socket == null) {
            return;
        }

        //
        try {
            socket.close();
        }
        catch (IOException e) {
        }
        socket = null;
    }

    /**
     * Clean up the socket (close it).
     *
     * @throws Throwable Throwing it here because when we call
     *                   super.finalize() it wants us to throw it.
     */
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    public String toString() {
        //--
        // NOTE: This code used to call Socket.toString(), but turns
        // out that there were bizzarre delays when doing this. Stack
        // traces showed threads stuck in InetAddress doing a dns
        // lookup. So we want to avoid dns lookups.
        //--
        StringBuffer sb = new StringBuffer();
        sb.append("saddr=" + serviceAddress + ":");
        sb.append("id=" + connId + ":");
        sb.append("socket=" + (socket == null ? "NULL" : Integer.toString(socket.getLocalPort())));

        return new String(sb);
    }

    private synchronized static int getNextId() {
        return nextId++;
    }
}
