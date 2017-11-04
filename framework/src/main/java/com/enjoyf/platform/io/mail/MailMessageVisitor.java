/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

/**
 * An abstraction for visiting MailMessage objects.
 */
public abstract class MailMessageVisitor {
    public abstract void visitMessageHTML(MailMessageHTML msg);

    public abstract void visitMessageText(MailMessageText msg);
}
