/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * This is used to configure a service. Pass one of these objects into
 * a client-side service class which is built on top of the
 * com.enjoyf.platform.service.service framework. The only thing the service class
 * will care about is the ReqProcessor object, which this object
 * should return.
 */
public abstract class ServiceConfig {
    //
    protected ReqProcessor reqProcessor = null;

    //
    private int timeoutTime = 30 * 1000;

    /**
     * Sez whether or not this object has been configured correctly.
     */
    public boolean isValid() {
        return reqProcessor != null;
    }

    /**
     * Retrieve the ReqProcessor object.
     */
    public ReqProcessor getReqProcessor() {
        return reqProcessor;
    }

    /**
     * Set any ReqProcessor object. This means the caller knows what
     * he's doing in putting the RepProcessor object together.
     */
    public void setReqProcessor(ReqProcessor processor) {
        reqProcessor = processor;
    }

    /**
     * Set the timeout in msecs for transactions.
     */
    public void setTimeout(int msecs) {
        timeoutTime = msecs;

        //--------------------------------------------------------
        // Make sure we propagate the timeout.
        //--------------------------------------------------------
        if (reqProcessor != null) {
            reqProcessor.setTimeout(timeoutTime);
        }
    }

    /**
     * Retrieve the transaction timeout.
     */
    public int getTimeout() {
        return timeoutTime;
    }

    /**
     * A method to return an identifier for this service. Should be
     * overriden by derived classes.
     */
    public abstract String getId();
}
