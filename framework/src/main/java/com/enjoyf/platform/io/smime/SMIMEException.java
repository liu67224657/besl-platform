/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.smime;

/**
 * Error messages for S/MIME
 */
public class SMIMEException extends RuntimeException {
    public static SMIMEException ALGORITHM_MISMATCH = new SMIMEException(1, "Algorithm Mismatch", "Certifcate and PrivateKey do not use the same algorithms.");
    public static SMIMEException GENERATOR_INIT_ERROR = new SMIMEException(2, "Initialization Error", "Failed to load and validate certs.");
    public static SMIMEException ALGORITHM_NOT_FOUND = new SMIMEException(3, "Algorithm Not Found", "Algorithm for the given provider could not be found.");
    public static SMIMEException PROVIDER_NOT_FOUND = new SMIMEException(4, "Provider Not Found", "Provider could not be found.");
    public static SMIMEException SIGN_ERROR = new SMIMEException(5, "Failed to Sign a message", "Unable to sign a message due to an unknown error.");
    public static SMIMEException BUILD_MESSAGE_ERROR = new SMIMEException(6, "Unable to build signed MimeMessage.", "Unable to properly build a signed message.");
    public static SMIMEException CERT_NOT_FOUND = new SMIMEException(7, "Certificates for provider not found", "Unable to load or locate certificates.");

    private int code;
    private String name;
    private String detail;

    private SMIMEException(int code, String name, String detail) {
        super(name);
        this.code = code;
        this.name = name;
        this.detail = detail;
    }

    public SMIMEException(SMIMEException se) {
        this(se.code, se.name, se.detail);
    }

    public SMIMEException(SMIMEException se, String detail) {
        this(se.code, se.name, detail);
    }

    public int getErrorCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int hashCode() {
        return code;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SMIMEException) {
            SMIMEException se = (SMIMEException) obj;
            return se.code == code;
        }

        return false;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(code);
        buf.append(": ");
        buf.append(name);

        if (detail != null) {
            buf.append(": ");
            buf.append(detail);
        }

        return buf.toString();
    }
}