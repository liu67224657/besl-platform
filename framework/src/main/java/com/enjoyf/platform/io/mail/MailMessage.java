package com.enjoyf.platform.io.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.validate.EmailValidator;

/**
 * This class is for creating mail messages.An instance of this class will
 * be one message. This class sucks, the key should not be Object (String/DisplayableEmailAddress)
 */
@SuppressWarnings("serial")
public abstract class MailMessage implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(MailMessage.class);
	
    protected List<DisplayableEmailAddress> toAddressList = null; // mail recipients
    protected List<DisplayableEmailAddress> ccAddressList = null; // mail cc recipients
    protected List<DisplayableEmailAddress> bccAddressList = null;// mail bcc recipients
    protected DisplayableEmailAddress fromAddress = null;     // mail sender
    protected DisplayableEmailAddress replyToAddress = null; //mail reply to
    protected String subject = null;         // mail subject
    protected Map<Object, String> attachments = null;    // the file attachments, if there are any
    protected Map<Object, String> attachmentNames = null;    // the filenames for the attachments
    protected LinkedList<String> urlAttachments = null;    // the file attachments from a URL
    /**
     * Preferred encoding for subject and body. Defaults to UTF-8.
     * bug # 84816: China Email: for prize system, the enconding of all our post email should be set to GBK not UTF-8.
     * We change the default encoding reading from env property. If missing, default is still to utf-8
     */
    protected String charSetEncoding = "utf-8";
    protected boolean signed = false;

    /**
     * default constructor
     */
    public MailMessage() {
        toAddressList = new Vector<DisplayableEmailAddress>();
        ccAddressList = new Vector<DisplayableEmailAddress>();
        bccAddressList = new Vector<DisplayableEmailAddress>();
    }

    public boolean setFrom(String fromDisplayName, String fromAddress) {
        this.fromAddress = new DisplayableEmailAddress(fromAddress, fromDisplayName);
        return true;
    }

    public void setReplyTo(String replyToDisplay, String replyToAddress) {
        this.replyToAddress = new DisplayableEmailAddress(replyToAddress, replyToDisplay);
    }

    /**
     * Set the subject.
     *
     * @param subj A String containing the subject of the message.
     */
    public void setSubject(String subj) {
        subject = subj;
    }

    public boolean setTo(String toDisplay, String toEmailAddress) {
        if (!EmailValidator.instance().isValid(toEmailAddress)) {
        	logger.warn("MailMessage.setTo: Attempt to set bogus address: " + toEmailAddress);
            return false;
        }

        toAddressList.add(new DisplayableEmailAddress(toEmailAddress, toDisplay));
        return true;
    }

     public boolean resetTo(String toDisplayName, String toEmailAddress) {
        toAddressList.clear();
        return setTo(toDisplayName, toEmailAddress);
    }

     /**
     * Set a list of message recepients. Replaces the address list, a batch reset if you will.
     * This method makes a copy of the given list. These objects will be validated to be valid email addresses.
     *
     * @param toList a List of DisplayableEmailAddress Objects.
     * @return true if set, false if there was an invalid address.
     */
    public boolean setToList(List<DisplayableEmailAddress> toList) {
        boolean validList = p_validateList(toList);
        if (validList) {
            this.toAddressList = Collections.synchronizedList(toList);
        }
        return validList;
    }


    /**
     * Set the CC recepient of the message.  Adds address to the list
     *
     * @param cc A String containing a single cc recipients email address.
     * @return true if the address is valid or false if it is invalid (according
     *         to the sed Validator)
     */
    public boolean setCC(String cc) {
        return setCC(cc, cc);
    }

    public boolean setCC(String displayName, String emailAddress) {
        if (!EmailValidator.instance().isValid(emailAddress)) {
        	logger.warn("MailMessage.setCC: Attempt to set " +
                    "bogus address: " + emailAddress);
            return false;
        }

        ccAddressList.add(new DisplayableEmailAddress(emailAddress, displayName));
        return true;
    }

    /**
     * Set a list of cc message recepients.  Replaces the address list
     *
     * @param ccList A Vector of Strings containing all the cc recipients email
     *               addresses.
     * @return true if the address is valid or false if it is invalid (according
     *         to the sed Validator)
     * @depricated use setCCList(List<DisplayableEmailAddress>)
     */
    public boolean setCC(Vector ccList) {

        List<DisplayableEmailAddress> realList = this.p_constructList(ccList);
        return this.setCCList(realList);
    }

    public boolean setCCList(List<DisplayableEmailAddress> ccList) {
        boolean valid = this.p_validateList(ccList);
        if (valid) {
            this.ccAddressList = Collections.synchronizedList(ccList);
        }
        return valid;
    }

    public boolean setBCC(String displayName, String emailAddress) {
        if (!EmailValidator.instance().isValid(emailAddress)) {
            logger.warn("MailMessage.setBCC: Attempt to set " +
                    "bogus address: " + emailAddress);
            return false;
        }

        bccAddressList.add(new DisplayableEmailAddress(emailAddress, displayName));
        return true;
    }

    public boolean setBCCList(List<DisplayableEmailAddress> bccList) {
        boolean valid = this.p_validateList(bccList);
        if (valid) {
            this.bccAddressList = bccList;
        }
        return valid;
    }

    /**
     * Set the body.
     *
     * @param body A String representing the body of the message.
     */
    public abstract void setBody(String body);

    /**
     * Adds the file attachment as an object with the corresponding MIME type
     *
     * @param attachment The object to attach
     * @param mimeType   The MIME type for this object
     * @param filename   The file name to give this attachment.  Null to generate a random name.
     */
    public void addAttachment(Object attachment, String mimeType, String filename) {
        // the object is most likely unique so use it as the key, and
        // the mime type as the value
        if (attachment != null && mimeType != null) {
            if (attachments == null) {
                attachments = new HashMap<Object, String>();    // only create if attaching file
            }
            attachments.put(attachment, mimeType);

            // store the filename if one is specified
            if (filename != null) {
                if (attachmentNames == null) {
                    attachmentNames = new HashMap<Object, String>();
                }
                attachmentNames.put(attachment, filename);
            }
        }
    }

    /**
     * convenience call
     */
    public void addAttachment(Object attachment, String mimeType) {
        addAttachment(attachment, mimeType, null);
    }

    /**
     * Adds the file attachment from a URL.  Java mail will automatically
     * specify the MIME type.  Useful for images.
     * This is separated from the Object attachments because there is a different
     * call to create a DataHandler specifically from a URL, as opposed to a generic
     * Object.
     *
     * @param url      The url of the file to attach
     * @param filename The filename to give this attachment.  Null to generate a random name.
     */
    public void addUrlAttachment(String url, String filename) {
        if (url != null) {
            if (urlAttachments == null) {
                urlAttachments = new LinkedList<String>();
            }
            urlAttachments.add(url);

            // store the filename if one is specified
            if (filename != null) {
                if (attachmentNames == null) {
                    attachmentNames = new HashMap<Object, String>();
                }
                attachmentNames.put(url, filename);
            }
        }
    }

    /**
     * convenience call
     */
    public void addUrlAttachment(String url) {
        addUrlAttachment(url, null);
    }

    /**
     * Dump object information
     *
     * @return a String containing this object
     */
    public String toString() {
        String info = " To: " + getTo() +
                " From: " + getFrom() +
                " Subject: " + getSubject();
        return info;
    }

    /**
     * Get the body.
     *
     * @return the body of the message
     */
    public abstract String getBody();

    /**
     * Get the subject.
     *
     * @return a String containing the subject of the message
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the from address of the message.
     *
     * @return a String containing the senders email address
     */
    public DisplayableEmailAddress getFrom() {
        return this.fromAddress;
    }

    /**
     * Get the reply-to address of the message.
     *
     * @return replyTo    String email address.
     */
    public DisplayableEmailAddress getReplyTo() {
        return replyToAddress;
    }

    /**
     * Get a single recepient of the message.
     *
     * @return a String containing the first recipient
     *         email addresses.  If empty returns null
     * @deprecated use getToList() to get the complete list
     */
    public DisplayableEmailAddress getTo() {
        if (toAddressList.size() == 0) {
            return null;
        } else {
            return toAddressList.get(0);
        }
    }

    /**
     * Get the recepients of the message.
     *
     * @return a Vector of String objects containing all the recipients
     *         email addresses.
     */
    public List<DisplayableEmailAddress> getToList() {
        return toAddressList;
    }

    /**
     * Get the cc recepients of the message.
     *
     * @return a Vector of String objects containing all the cc recipients
     *         email addresses.
     */
    public List<DisplayableEmailAddress> getCCList() {
        return ccAddressList;
    }

    /**
     * Get the bcc recepients of the message.
     *
     * @return a Vector of String objects containing all the bcc recipients
     *         email addresses.
     */
    public List<DisplayableEmailAddress> getBCCList() {
        return bccAddressList;
    }

    /**
     * Returns the collection of Object file attachments
     */
    public Collection<Object> getAttachments() {
        if (attachments == null) {
            return null;
        }
        return attachments.keySet();
    }

    /**
     * Returns the filename for the attached object (both Objects and url's)
     */
    public String getAttachmentName(Object o) {
        if (attachmentNames == null || o == null) {
            return null;
        }
        return (String) attachmentNames.get(o);
    }

    /**
     * Returns the mime type for the file attachment passed in
     */
    public String getMimeType(Object o) {
        if (o == null || attachments == null) {
            return null;
        }
        return attachments.get(o);
    }

    /**
     * Returns the collection of url file attachments (as strings)
     */
    public Collection<String> getUrlAttachments() {
        return urlAttachments;
    }

    /**
     * Sets the preferred encoding for the subject and body text of email message.
     */
    public void setPreferredEncoding(String enc) {
        if (enc != null) {
            charSetEncoding = enc;
        }
    }

    /**
     * Gets the preferred encoding for the email message
     */
    public String getPreferredEncoding() {
        return charSetEncoding;
    }

    /**
     * is the message signed?
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * set the message to be signed;
     */
    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    /**
     * Virtual method to be implemented by derived classes so that
     * the MailService class knows what to do with the message.
     */
    public abstract void accept(MailMessageVisitor v);


    private List<DisplayableEmailAddress> p_constructList(Vector toList) {
        // Our completed list. Refactor to save interface
        List<DisplayableEmailAddress> realList = new ArrayList<DisplayableEmailAddress>();
        // start with the assumption that the list is not null
        boolean rtVal = toList != null;

        /*
           *form the list in terms of email addresses.
           */

        for (Iterator i = toList.iterator(); i.hasNext() && rtVal;) {
            Object o = i.next();
            if (o instanceof String) {
                realList.add(new DisplayableEmailAddress((String) o, (String) o));
            } else {
                if (o instanceof DisplayableEmailAddress) {
                    realList.add((DisplayableEmailAddress) o);
                } else {
                    rtVal = false;
                }
            }
        }
        //if false then return null
        if (!rtVal) {
            realList = null;
        }
        return realList;
    }

    private boolean p_validateList(List<DisplayableEmailAddress> toList) {
        boolean valid = toList != null;
        if (valid) {
            for (Iterator<DisplayableEmailAddress> iter = toList.iterator(); iter.hasNext() && valid;) {
                if (!EmailValidator.instance().isValid(iter.next().getEmailAddress())) {
                    logger.warn("Cannot send message to an Invalid email address");
                    valid = false;
                }
            }
        }
        return valid;
    }
}
