/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

import java.io.Serializable;

import com.enjoyf.platform.util.validate.EmailValidator;

/**
 * @author Yin Pengyi
 */
public class EmailAddress implements Serializable {
    private String email;

    /**
     * Constructor.
     * Constructs an EmailAddress from a string.
     */
    public EmailAddress(String email) {
        if (email != null) {
            this.email = email.trim().toLowerCase();
        }
    }


    /**
     * Method getEmail
     *
     * @return a String
     */
    public String getEmail() {
        return email;
    }


    /**
     * Construction of this object will pass the passed in string through
     * a validator. If it is deemed invalid, this method will return false.
     * The idea is that invalid addresses should not be sent email.
     */
    public boolean isValid() {
        return EmailValidator.instance().isValid(email);
    }


    /**
     * Determines if we have an email address populated in the object or not
     *
     * @return
     */
    public boolean isEmpty() {
        if (email == null || email.length() == 0) {
            return true;
        }

        return false;
    }


    /**
     * Standard compareTo() method
     */
    public int compareTo(Object o) {
        EmailAddress email = (EmailAddress) o;

        if (o instanceof EmailAddress) {
            EmailAddress that = (EmailAddress) o;

            if (this.isEmpty() && that.isEmpty()) {
                return 0;
            }
            if (this.isEmpty() && !that.isEmpty()) {
                return -1;
            }
            if (!this.isEmpty() && that.isEmpty()) {
                return 1;
            }
            return getEmail().compareToIgnoreCase(that.getEmail());
        }
        throw new IllegalArgumentException("unable to compare");
    }

    /**
     * Standard toString() method
     */
    public String toString() {
        return email;
    }

    /**
     * Standard equals() method
     */
    public boolean equals(Object obj) {
        if (obj instanceof EmailAddress) {
            EmailAddress that = (EmailAddress) obj;

            if (this.isEmpty() && that.isEmpty()) {
                return true;
            }
            if (this.isEmpty() && !that.isEmpty()) {
                return false;
            }
            if (!this.isEmpty() && that.isEmpty()) {
                return false;
            }

            return getEmail().compareToIgnoreCase(that.getEmail()) == 0;
        }
        return false;
    }
}