/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

/**
 * Abstract interface to represent functionality to be accessed by
 * package classes, NOT by clients. Rather than putting this all into
 * one interface, i split it up into two to make the distinction.
 * In the end, all the methods will be publicly accessible by
 * implementing classes, but at least we have some way of
 * differentiating them.
 */
public interface RegistrantPriv {
    /**
     * Set the naming service address.
     */
    public void setNamingServiceAddress(NamingServiceAddress namingService);

    /**
     * Start the service up.
     */
    public void start();

    /**
     * Shut the service down.
     */
    public void stop();
}
