/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;

/**
 * Request class for submitting requests to java servers.
 */
public class Request {
    //the protocal data
    private byte requestType = 0;
    private byte metricsType = 0;
    private WPacket outputPacket = null;

    //the server info
    private ServiceAddress serviceAddress = null;

    //the request attributes.
    private boolean blockingFlag = true;

    /**
     * the partition is set in client side encoding class.
     */
    private int partitionValue = -1;
    private int timeoutTime = -1;

    /**
     * Build a request object from the passed in args.
     *
     * @param type The type of the transaction.
     * @param wp   The contents of the transaction.
     */
    public Request(byte type, WPacket wp) {
        requestType = type;
        outputPacket = wp;
    }

    /**
     * Ctor an empty request object. One of the set methods below
     * better be used before submitting this request.
     */
    public Request() {
    }

    /**
     * Specify whether or not you want a non-blocking request.
     * By default, requests do block and wait for a reply.
     */
    public void setBlocking(boolean val) {
        blockingFlag = val;
    }

    /**
     * Returns true if this a non-blocking request.
     */
    public boolean isBlocking() {
        return blockingFlag;
    }

    /**
     * Set the type of the request.
     */
    public void setType(byte type) {
        requestType = type;
    }

    /**
     * Set the packet of the request.
     */
    public void setPacket(WPacket wp) {
        outputPacket = wp;
    }

    /**
     * Set both the type and packet of the request.
     */
    public void set(byte type, WPacket wp) {
        requestType = type;
        outputPacket = wp;
    }

    public byte getType() {
        return requestType;
    }

    public WPacket getPacket() {
        return outputPacket;
    }

    /**
     * Set the service address of the service you wish to
     * logicProcess the request on. If unset, a random service
     * will be chosen (hopefully in a load-balanced way). In
     * fact, you shouldn't ever set this, unless you need this
     * kind of behavior (eg, talking to the objserver).
     */
    public void setServiceAddress(ServiceAddress saddr) {
        serviceAddress = saddr;
    }

    /**
     * Return the service address used to logicProcess this request.
     * It will be set AFTER the request gets processed, and only
     * if it got processed succesfully (so you may get a null back).
     */
    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    /**
     * Returns true if the request is to be partitioned.
     */
    public boolean isPartitionable() {
        return partitionValue != -1;
    }

    /**
     * Returns the partitionValue number. This is an integer from 0-n.
     */
    public int getPartition() {
        return partitionValue;
    }

    /**
     * Set the partitionValue value.
     */
    public void setPartition(int p) {
        partitionValue = p;
    }

    /**
     * Set a timeout specifically for this transaction, in msecs.
     */
    public void setTimeout(int timeout) {
        timeoutTime = timeout;
    }

    /**
     * Retrieve the set timeout. If -1, it means no timeout was set.
     */
    public int getTimeout() {
        return timeoutTime;
    }

    /**
     * Returns true if a specific timeout was set for this request.
     */
    public boolean isTimeoutSet() {
        return timeoutTime != -1;
    }

    /**
     * Set a different code for the metrics type. Normally, the type used
     * in metrics collection comes from getType(), but when you use a
     * Command pattern to logicProcess requests, all requests get lumped under
     * the same type. You can bypass this by setting this field to
     * something else, and the metrics collector will then use this value.
     * <p/>
     * NOTE: The type used should be defined in the XConstants file with
     * the attendant TransProfileContainer. Otherwise the stats will show up
     * with a name of "UNKNOWN", although they will be differentiated by
     * number.
     * <p/>
     * One idea for client use is if the request objects are all in
     * a hierarchy, add a getType() method on the Request base class to
     * return the type, and do something like:
     * <p/>
     * Request req = new Request(...);
     * req.setMetricsType( beslRequest.getType() );
     * <p/>
     * Each BeslRequest class can then implement a virtual getType() method
     * or the ctor could set a base member requestType var with the right code.
     */
    public void setMetricsType(byte type) {
        metricsType = type;
    }

    /**
     * Returns the metrics type as set by a caller, which is 0 when unset.
     */
    byte getMetricsType() {
        return metricsType;
    }

    /**
     * This returns a usable metrics type. Which means it uses metricsType
     * if set, otherwise it uses just the type.
     */
    byte getUsableMetricsType() {
        return metricsType == 0 ? requestType : metricsType;
    }
}
