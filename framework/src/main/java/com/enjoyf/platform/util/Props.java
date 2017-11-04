/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.enjoyf.platform.util.log.GAlerter;

/**
 * A singleton to provide access to the standard property containing the
 * name of the property file hierarchy for the currently running
 * logicProcess. The standard property is: "serv.props.file" and can be set
 * on the java cmd string with:
 * <p/>
 * -Dserv.props.file=<filename>
 * <p/>
 * where <filename> is a full pathname of some properties file.
 */
public class Props {
    private static final String SERV_PROPNAME = "serv.props.file";
    private static final String ENV_PROPNAME = "env.props.file";

    protected static Props instance = new Props();

    protected FiveProps servProps = null;
    protected FiveProps envProps = null;
    protected Map<String, FiveProps> clientPropsMap = new HashMap<String, FiveProps>();

    protected String propsFileName = null;
    protected String envPropsFileName = null;

    protected FiveProps unionProps = null;

    //the env key setting.
    private final static String KEY_HOTDEPLOY_RELOAD_INTERVAL_MSEC = "hotdeploy.reload.interval.msec";

    //the env attri.
    private long hotdeployReloadIntervalMsec = 5 * 60 * 1000;

    /**
     * Protected constructor
     */
    protected Props() {
    }

    /**
     * Retrieve the properties object.
     *
     * @return Returns a Properties object. It may be empty if the property
     *         was not set, or the file could not be found, but it will never
     *         be null.
     */
    public Properties get() {
        return servProps.getProps();
    }

    /**
     * Returns the jvm's server properties, ie, whatever got passed in
     * as -Dcom.platform.props.
     */
    public synchronized FiveProps getServProps() {
        if (servProps == null) {
            initServProps();
        }
        return servProps;
    }

    public long getHotdeployReloadIntervalMsec() {
        return hotdeployReloadIntervalMsec;
    }

    /**
     * Return the env FiveProps object.
     */
    public synchronized FiveProps getEnvProps() {
        if (envProps == null) {
            initEnvProps();
        }

        //do some other things
        hotdeployReloadIntervalMsec = envProps.getLong(KEY_HOTDEPLOY_RELOAD_INTERVAL_MSEC, hotdeployReloadIntervalMsec);

        return envProps;
    }

    /**
     * Return the service client specific FiveProps object. This first looks for a props file referenced
     * in the env.props using <serviceName>.client.props.file. If that is not found it tries to derive
     * a path based on the path to the env.props file.
     *
     * @param serviceName
     * @return FiveProps
     */
    public synchronized FiveProps getClientProps(String serviceName) {
        FiveProps clientProps = clientPropsMap.get(serviceName);
        if (clientProps == null) {
            String clientPropsFileProp = serviceName + ".client.props.file";
            String propFileName = getEnvProps().get(clientPropsFileProp, null);
            if (propFileName == null) {
                String envPropsFileName = getEnvPropsFileName();
                if (envPropsFileName.startsWith("/props/env/")) {
                    propFileName = "/props/service/" + serviceName + envPropsFileName.substring(10);
                } else {
                    propFileName = "/props/service/" + serviceName + "/def.properties";
                }
            }
            clientProps = new FiveProps(propFileName, false);
            clientPropsMap.put(serviceName, clientProps);
        }
        return clientProps;
    }

    /**
     * Return the name of the ten properties file.
     */
    public synchronized String getServPropsFileName() {
        if (servProps == null) {
            initServProps();
        }
        return propsFileName;
    }

    /**
     * Return the name of the ul properties file.
     */
    public synchronized String getEnvPropsFileName() {
        if (envProps == null) {
            initEnvProps();
        }
        return envPropsFileName;
    }

    /**
     * Returns the union of the com.enjoyf.platform.props properties + the env properties.
     * NOTE: The env properties populate the output first, then the
     * com.enjoyf.platform.props properties. So com.enjoyf.platform.props overrides what's in
     * env.
     */
    public synchronized FiveProps getUnion() {
        if (unionProps == null) {
            unionProps = new FiveProps(envProps.getProps());
            unionProps.addAll(servProps);
        }
        return unionProps;
    }

    public static Props instance() {
        return instance;
    }

    private void initServProps() {
        propsFileName = Environment.instance().getProperty(SERV_PROPNAME);
        servProps = createProps(SERV_PROPNAME, propsFileName, false);
    }

    private void initEnvProps() {
        envPropsFileName = Environment.instance().getProperty(ENV_PROPNAME);
        envProps = createProps(ENV_PROPNAME, envPropsFileName, true);
    }

    private static FiveProps createProps(String propName, String propsFileName, boolean loadSysProps) {
        FiveProps props = null;

        if (propsFileName == null || propsFileName.length() == 0) {
            GAlerter.lab("Props: Could not find property: " + propName);

            // Just create an empty properties object.
            props = new FiveProps("");
        } else {
            props = new FiveProps(propsFileName, loadSysProps);
        }

        return props;
    }

    public static void main(String[] args) {
        System.out.println(Props.instance().get());
    }
}
