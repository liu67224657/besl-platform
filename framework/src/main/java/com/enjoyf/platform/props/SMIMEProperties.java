/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.props;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

/**
 * Property file locator for S/MIME certificates
 */
public class SMIMEProperties {
    private static SMIMEProperties instance = new SMIMEProperties();

    private FiveProps properties = null;

    /**
     * Return TenProps for SMIME certificates.
     *
     * @return props    TenProps
     */
    public FiveProps getProperties() {
        if (properties == null) {
            synchronized (this) {
                if (properties == null) {
                    properties = new FiveProps(Props.instance().getEnvProps().get("smime.props.file", "/props/cert/smime/smime.properties"));
                }
            }
        }

        return properties;
    }

    public static SMIMEProperties instance() {
        return instance;
    }

    private SMIMEProperties() {

    }
}
