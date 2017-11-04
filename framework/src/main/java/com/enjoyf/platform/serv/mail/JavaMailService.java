package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.io.mail.DisplayableEmailAddress;
import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.io.mail.MailMessageHTML;
import com.enjoyf.platform.io.mail.MailMessageText;
import com.enjoyf.platform.io.mail.MailMessageVisitor;
import com.enjoyf.platform.io.smime.SMIMEFactory;
import com.enjoyf.platform.io.smime.SMIMEMessageGenerator;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This class represents mail connections.  An instance of this class will be
 * a connection to a mail host for sending mail.  Most applications sending
 * mail will be able to share one JavaMailService object.  This uses the java mail.
 */

class JavaMailService implements MailService, ConnectionListener {

    private static final Logger logger = LoggerFactory.getLogger(JavaMailService.class);

    protected boolean debug = false;

    /* Java mail connection to a mail session */
    protected Session session = null;

    /* Java mail transport for this mail session */
    protected Transport transport = null;

    /* state of the transport connection */
    protected boolean connected = false;

    /**
     * default constructor uses default mail session
     */
    public JavaMailService() {
        /* Get a Session object */
        session = Session.getDefaultInstance(System.getProperties(), null);

        open();
    }

    /**
     * constructor with an smtp mail host
     */
    public JavaMailService(String smtpHost) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHost);

        session = Session.getDefaultInstance(props, null);

        open();
    }

    public JavaMailService(String smtpHost, boolean needAuth, String authUser, String authPwd) {
        SmtpAuthenticator authenticator = null;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHost);

        if (needAuth) {
            props.put("mail.smtp.auth", "true");

            authenticator = new SmtpAuthenticator(authUser, authPwd);
            session = Session.getInstance(props, authenticator);
        } else {
            session = Session.getDefaultInstance(props, null);
        }

        open();
    }

    /**
     * open a transport connection . This is called by the constructor
     * and should only be called again if the connection is closed and
     * needs to be reopened.
     */
    public void open() {
        if (transport != null) {
            logger.debug("JavaMailService.open(): connection already open");
            return;
        }

        try {
            /*
                * XXXX test if it is best performance to keep this
                * transport connection open for the life of this object
                * For now hard code to SMTP protocol
                */
            transport = session.getTransport("smtp");
            transport.connect();

            /*
                * add a listener to the connection which will set the
                * state of the connected flag to false if the
                * connection is lost
                */
            transport.addConnectionListener(this);
            connected = true;
        } catch (MessagingException e) {
            logger.error("JavaMailService.open(): transport error: ", e);

            connected = false;
        }
    }

    /**
     * Close the object. Release all resources.
     */
    public void close() {
        try {
            transport.close();
            transport = null;
            connected = false;
        } catch (MessagingException e) {
            //--
            // Since we are closing it, we already smell problems.
            // Just log the error.
            //--
            logger.error("JavaMailService.close(): transport error: ", e);
        } catch (NullPointerException npe) {
            //todo
        }
    }

    /**
     * set debugging for session - prints out mail session information
     */
    public void setDebug(boolean value) {
        debug = value;
        session.setDebug(debug);
    }

    public boolean getDebug() {
        return debug;
    }

    /**
     * ConnectionListener event
     */
    public void opened(ConnectionEvent e) {
        connected = true;
    }

    /**
     * ConnectionListener event
     */
    public void disconnected(ConnectionEvent e) {
        connected = false;
    }

    /**
     * ConnectionListener event
     */
    public void closed(ConnectionEvent e) {
        connected = false;
    }


    /**
     * Send a message.
     *
     * @throws MailServiceException Thrown if there is a problem
     *                              with the transport connection.
     * @throws MailMessageException Thrown if
     *                              there are other problems with sending the message.
     */

    public void send(MailMessage msg) throws MailMessageException, MailServiceException {
        /*
           * check if we've been passed an actual messaage
           */
        if (msg == null) {
            throw new MailMessageException(MailMessageException.NULL_MAIL_MESSAGE, "empty mail instance");
        }

        /*
           * make sure the connection is still valid
           */
        if (!connected) {
            throw new MailServiceException(MailServiceException.TRANSPORT_NOT_CONNECTED);
        }

        try {
            /**
             * Retrieve an internal JavaMail API message object.
             */
            Message javaMsg = p_buildJavaMessage(msg);

            /**
             * Fill the java mail message object with our propietary
             * MailMessage object. The only thing funny about this
             * is that the visitor stores an Exception that might
             * get thrown by the visitor. Since the exception is
             * part of the JavaMail api, we don't want to expose
             * that to MailMessage.accept(). Not only that, the
             * Visitor class is abstract, so we don't want to tie
             * down any exception throwing since we don't know what
             * the semantics of other visitors might be.
             */
            JavaMailVisitor v = new JavaMailVisitor(javaMsg);
            msg.accept(v);
            if (v.getException() != null) {
                throw v.getException();
            }

            /* message is built...now send it */
            javaMsg.saveChanges();

            if (msg.isSigned()) {
                SMIMEMessageGenerator gen = SMIMEFactory.instance().getMessageGenerator(msg.getFrom().getEmailAddress());

                if (gen != null) {
                    javaMsg = gen.sign((MimeMessage) javaMsg);
                } else {
                    GAlerter.lab("MailService.send(): Failed to sign message, certificates could not be found for " + msg.getFrom());
                }
            }

            javaMsg.saveChanges();

            Address[] addresses = javaMsg.getAllRecipients();
            transport.sendMessage(javaMsg, addresses);
        } catch (MessagingException e) {
            //--
            // Just treat any exception we get as a bad
            // conn exception and have the caller deal with it.
            //--
            logger.error("JavaMailService send error", e);
            throw new MailServiceException(MailServiceException.JAVA_MAIL_ERROR);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    class JavaMailVisitor extends MailMessageVisitor {
        private MessagingException mException = null;
        private Message mJavaMsg = null;

        public JavaMailVisitor(Message javaMsg) {
            mJavaMsg = javaMsg;
        }

        public MessagingException getException() {
            return mException;
        }

        public void visitMessageHTML(MailMessageHTML msg) {
            try {
                MimeMultipart mainMultipart = new MimeMultipart();
                MimeBodyPart mainBodyPart = new MimeBodyPart();
                String encoding = msg.getPreferredEncoding().toString();
                mJavaMsg.setHeader("Encoding", encoding);

                // Create the main multipart from the message's
                // text and html portions.
                // This may have to be nested within the file attachment
                // multipart if attachments exist

                /* text part */
                String text = msg.getText();
                if (text != null) {
                    /*
                          * this will cause text to be displayed if
                          * the mail reader can't display the html
                          */
                    mainMultipart.setSubType("alternative");
                    MimeBodyPart textPart = new MimeBodyPart();

                    // set utf-8 encoding for the text body part
                    textPart.setHeader("Content-Type", "text/plain,charset=" + encoding);
                    textPart.setHeader("Content-Transfer-Encoding", "base64");

                    textPart.setText(text);
                    mainMultipart.addBodyPart(textPart);
                }

                /* html part */
                String html = msg.getBody();
                if (html != null) {

                    MimeBodyPart htmlPart = new MimeBodyPart();

                    // set utf-8 encoding for the text body part
                    htmlPart.setHeader("Content-Type", "text/html;charset=" + encoding);
                    htmlPart.setHeader("Content-Transfer-Encoding", "base64");

                    htmlPart.setContent(html, "text/html;charset=" + encoding);
                    mainMultipart.addBodyPart(htmlPart);
                }
                mainBodyPart.setContent(mainMultipart);

                MimeMultipart attachMultipart = p_getAttachments(msg,
                        mainBodyPart);
                if (attachMultipart != null) {
                    // file attachments exist, attachment is outer layer
                    mJavaMsg.setContent(attachMultipart);
                } else {
                    // attachments don't exist, main is outer layer
                    mJavaMsg.setContent(mainMultipart);
                }
            } catch (MessagingException e) {
                mException = e;
            }
        }

        public void visitMessageText(MailMessageText msg) {
            try {
                String encoding = msg.getPreferredEncoding().toString();
                if (logger.isDebugEnabled()) {
                    logger.debug("Encoding setto:" + "text/plain,charset=" + encoding);
                }
                String text = msg.getBody();
                MimeBodyPart textPart = new MimeBodyPart();

                if (text != null) {
                    textPart.setText(text);
                }
                MimeMultipart mp = p_getAttachments(msg, textPart);

                if (mp != null) {
                    // set the content as a multipart
                    mJavaMsg.setContent(mp);
                } else if (text != null) {
                    // no attachments, just set the text
                    mJavaMsg.setText(text);
                }

                // set utf-8 encoding for the text body part
                try {
                    ContentType ct = null;
                    if (mp != null) {
                        ct = new ContentType(mp.getContentType());
                    } else {
                        ct = new ContentType(textPart.getContentType());
                    }
                    ct.setParameter("charset", encoding);
                    mJavaMsg.setContent(mJavaMsg.getContent(), ct.toString());
                } catch (IOException ioe) {
                    logger.error("JavaMailVisitor.visitMessageText(): Error setting content " + ioe.getMessage(), ioe);
                    throw new MessagingException(ioe.getMessage());
                }
            } catch (MessagingException e) {
                mException = e;
            }
        }

        /**
         * Returns a multipart/mixed that includes the appropriate
         * file attachments.  Since this is an outer multipart layer
         * the inner part (i.e. main email content) must be passed
         * in for proper construction.
         * Returns null if no file attachments exist
         *
         * @param msg         The message to get attachments for
         * @param mainContent The main content of the mail (will be part of the return val)
         * @return A multipart with the file attachments and the main content
         *         null if no files attached.
         */
        private MimeMultipart p_getAttachments(MailMessage msg, MimeBodyPart mainContent)
                throws MessagingException {
            Collection attachments = msg.getAttachments();
            Collection urlAttachments = msg.getUrlAttachments();
            Iterator iter = null;
            Object currAttach = null;
            String currUrlAttach = null;
            String filename = null;

            // check to see if any file attachments exist at all
            if (attachments == null && urlAttachments == null) {
                return null;
            }

            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart attachPart = null;
            mp.setSubType("mixed");

            // set the main content first
            if (mainContent != null) {
                mp.addBodyPart(mainContent);
            }

            // attach each object component
            if (attachments != null) {
                iter = attachments.iterator();
                while (iter.hasNext()) {
                    currAttach = iter.next();
                    attachPart = new MimeBodyPart();

                    if (currAttach instanceof DataSource) {
                        DataSource ds = (DataSource) currAttach;
                        attachPart.setDataHandler(new DataHandler(ds));
                    } else {
                        attachPart.setDataHandler(new DataHandler(
                                currAttach, msg.getMimeType(currAttach)));
                    }

                    filename = msg.getAttachmentName(currAttach);
                    if (filename != null) {
                        attachPart.setDisposition("attachment");
                        attachPart.setFileName(filename);
                    }

                    mp.addBodyPart(attachPart);
                }
            }
            // attach each url component
            if (urlAttachments != null) {
                iter = urlAttachments.iterator();
                while (iter.hasNext()) {
                    try {
                        currUrlAttach = (String) iter.next();
                        attachPart = new MimeBodyPart();
                        attachPart.setDataHandler(new DataHandler(
                                new URL(currUrlAttach)));

                        filename = msg.getAttachmentName(currUrlAttach);
                        if (filename != null) {
                            attachPart.setDisposition("attachment");
                            attachPart.setFileName(filename);
                        }

                        mp.addBodyPart(attachPart);
                    } catch (MalformedURLException e) {
                        // Not fatal, just log and skip it.
                        logger.warn("JavaMailService.JavaMailVisitor: "
                                + "malformed URL " + currUrlAttach);
                        continue;
                    }
                }
            }

            return mp;
        }
    }

    private Message p_buildJavaMessage(MailMessage msg) throws MessagingException {
        MimeMessage javaMsg = new MimeMessage(session);

        DisplayableEmailAddress from = msg.getFrom();
        if (from != null) {
            InternetAddress addy = new InternetAddress(from.getEmailAddress());
            if (from.getDisplayAddress() != null) {
                try {
                    addy.setPersonal(from.getDisplayAddress());
                } catch (UnsupportedEncodingException e) {
                    logger.error("Unsupported encoding detected for string:" + from.getDisplayAddress(), e);
                }
            }

            javaMsg.setFrom(addy);
        } else {
            throw new MessagingException("no from address");
        }

        String subj = msg.getSubject();
        if (subj != null) {
            javaMsg.setSubject(subj, msg.getPreferredEncoding().toString());
        } else {
            throw new MessagingException("no subject");
        }

        /* if there is no to address the mail is undeliverable */
        List<DisplayableEmailAddress> to = msg.getToList();
        if (to == null || to.size() == 0) {
            throw new MessagingException("missing recipient");
        } else {
            this.p_addList(to, javaMsg, Message.RecipientType.TO);
            /**
             * set the cc list
             */
            this.p_addList(msg.getCCList(), javaMsg, Message.RecipientType.CC);
            /**
             * set the bcc list
             */
            this.p_addList(msg.getBCCList(), javaMsg, Message.RecipientType.BCC);

        }
        return javaMsg;
    }

    private void p_addList(List<DisplayableEmailAddress> emailsAddy, MimeMessage javaMessage, Message.RecipientType type) throws MessagingException {
        if (emailsAddy == null || javaMessage == null || type == null) {
            return;
        }
        Iterator<DisplayableEmailAddress> iter = emailsAddy.iterator();
        while (iter.hasNext()) {
            DisplayableEmailAddress addy = iter.next();
            InternetAddress address = new InternetAddress(addy.getEmailAddress());
            if (addy.getDisplayAddress() != null) {
                try {
                    address.setPersonal(addy.getDisplayAddress());
                } catch (UnsupportedEncodingException e) {
                    logger.error("Unsupported encoding detected for string:" + addy.getDisplayAddress(), e);
                }
            }
            javaMessage.addRecipient(type, address);
        }

    }

    @Override
    public void reportFatal() {

    }

}