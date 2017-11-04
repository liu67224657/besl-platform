/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * The Environment class encapsulates access to environment
 * properties.  This is done to provide a facility for reading
 * environment properties from somewhere other than System.properties.
 * In some cases, it is burdensome to define properties by passing
 * them on the Java command line; for example, when configuring a
 * servlet engine that is started by a third party script.
 */

public class Environment {

    private static Environment mInstance = new Environment();
    private Properties mProperties;
    private static final String ENVIRONMENT_DEFAULTS_FILE =
            "/props/system.properties";


    /**
     * The fundamental call is getProperty, given a name.
     */
    public String getProperty(String name) {
        String sysProp = System.getProperty(name);
        if (sysProp != null) {
            return sysProp;
        }

        // Check to see if the property is present in the defaults file.
        if (mProperties != null) {
            String defaultProp = mProperties.getProperty(name);
            if (defaultProp != null) {
                return defaultProp;
            }
        }

        // Neither specified or defaulted.
        return null;
    }


    /**
     * Singleton instance
     */
    public static Environment instance() {
        return mInstance;
    }


    private Environment() {
        try {
            InputStream is = Environment.class.getResourceAsStream(ENVIRONMENT_DEFAULTS_FILE);
            if (is != null) {
                mProperties = new Properties();
                mProperties.load(is);
            }
        }
        catch (IOException e) {
            // This is expected most of the time, so don't log.
            // Normally we don't override the system properties.
        }
    }


}
