/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

/**
 * @author Yin Pengyi
 */
public class DisplayableEmailAddress implements java.io.Serializable {
    private String emailAddress;
    private String displayAddress;


    public DisplayableEmailAddress(String address) {
        this(address, address);
    }

    public DisplayableEmailAddress(String address, String display) {
        if (address == null) {
            throw new IllegalArgumentException("Cannot create a null email address");
        }

        if (display == null) {
            display = "";
        }

        this.displayAddress = display;
        this.emailAddress = address;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('<').append(this.getDisplayAddress()).append('>');
        builder.append(this.getEmailAddress());
        return builder.toString();
    }
}