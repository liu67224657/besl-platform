/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.smime;

import javax.mail.internet.MimeMessage;

/**
 * Interface class for a S/MIME Message Generator.
 */
public interface SMIMEMessageGenerator {
    /**
     * Initialize a SMIMEMessageGenerator
     *
     * @param cert String, resource path to cert
     * @param key  String, resource path to key
     * @throws org.bouncycastle.mail.smime.SMIMEException
     *
     */
    public void init(String cert, String key)
            throws SMIMEException;

    /**
     * Generate a signed mime object based on S/MIME certificates and message body.
     *
     * @param msg MimeMessage
     * @return signedMsg    MimeMessage
     * @throws org.bouncycastle.mail.smime.SMIMEException
     *
     */
    public MimeMessage sign(MimeMessage msg)
            throws SMIMEException;
}
