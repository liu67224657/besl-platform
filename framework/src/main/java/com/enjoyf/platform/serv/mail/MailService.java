/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.io.mail.MailMessage;

/**
 * An interface for mailing services that encapsulate transport sytem for sending out messages.
 */
interface MailService {
    /**
     * Sends a message.
     *
     * @throws MailServiceException if there is a problem with sending the mail message.
     * @throws MailMessageException if there is a problem with the message.
     */
    void send(MailMessage msg) throws MailMessageException, MailServiceException;

    /**
     * Checks whether the underlying mail transport system is connected.
     *
     * @return <code>true</code> if it is connected; <code>false</code> otherwise.
     */
    boolean isConnected();

    /**
     * Opens a connection to the transport system. This is called by the constructor and should only be called again if the connection is
     * closed and needs to be reopened.
     */
    void open();

    /**
     * Closes the transport connection and releases all resources. This along with {@link #open()} allows resetting the underlying transport
     * system when connection errors occur.
     */
    void close();

    /**
     * Called when we have a fatal error.
     */
    void reportFatal();
}