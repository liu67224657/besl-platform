package com.enjoyf.platform.props;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

import com.google.common.base.Strings;

/**
 * @author Yin Pengyi
 */
public class I18nMessageProperties {
    private static String messagePropsFileRoot = "/hotdeploy/props/message/";
    private static String propsEnv = "";

    private static final String KEY_MESSAGE_CONFIG_ENV = "i18n.message.config.env";
    private static final String PROP_FILE_DEFAULT_NAME = "def";
    private static final String PROP_FILE_SUFFIX_NAME = ".properties";
    private static final String PROP_FILE_DIR_SEPARATOR = "/";

    private static Hashtable<String, I18nMessageProperties> propsCache = new Hashtable<String, I18nMessageProperties>();
    private FiveProps props;

    static {
        propsEnv = Props.instance().getEnvProps().get(KEY_MESSAGE_CONFIG_ENV, propsEnv);
    }

    /**
     * Obtain an appropriate instance for a given service and locale.
     * The service should be a descriptive string ("roomserver",
     * "gameserver", "ucount"). The leaf props file will be
     * /props/message/SERVICE/def.properties.
     *
     * @param service the service name
     * @param locale  the locale
     * @return an appropriate instance
     */
    public static I18nMessageProperties forService(I18nServiceType service, Locale locale) {
        return forServiceComponent(service, PROP_FILE_DEFAULT_NAME, locale);
    }

    /**
     * Obtain an appropriate instance for a given service, component,
     * and locale. The service should be a descriptive string
     * ("roomserver", "gameserver", "ucount"). The component
     * may be any useful string. The leaf props file will be
     * /props/message/SERVICE/COMPONENT.properties.
     *
     * @param service   the service name
     * @param component the component
     * @param locale    the locale
     * @return an appropriate instance
     */
    public static I18nMessageProperties forServiceComponent(I18nServiceType service, String component, Locale locale) {
        String key = getKey(service, component, locale);

        I18nMessageProperties props;
        synchronized (propsCache) {
            props = propsCache.get(key);
            if (props == null) {
                try {
                    FiveProps fiveProps = new FiveProps(messagePropsFileRoot + service.getCode() + PROP_FILE_DIR_SEPARATOR +
                            (Strings.isNullOrEmpty(propsEnv) ? "" : (propsEnv + PROP_FILE_DIR_SEPARATOR)) +
                            component + PROP_FILE_SUFFIX_NAME, locale);
                    
                    props = new I18nMessageProperties(fiveProps);
                } catch (Exception e) {
                    props = new I18nMessageProperties(null);
                }

                propsCache.put(key, props);
            }
        }

        return props;
    }

    private static String getKey(I18nServiceType service, String component, Locale locale) {
        return service.getCode() + PROP_FILE_DIR_SEPARATOR + component + PROP_FILE_DIR_SEPARATOR + locale;
    }

    private I18nMessageProperties(FiveProps props) {
        this.props = props;
    }

    /**
     * Retrieve a locale-specific message string by key.
     *
     * @param propname the properties key
     * @return the message, or null if not found
     */
    public String get(String propname) {
        return props.get(propname);
    }

    public List<String> getList(String propname, String delim) {
        return props.getList(propname, delim);
    }

    public List<String> getList(String propName) {
        return props.getList(propName);
    }

    public String toString() {
        return props.toString();
    }
}
