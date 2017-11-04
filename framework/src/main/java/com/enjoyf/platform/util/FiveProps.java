/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the reading in of property files laid out in a hierarchy.
 */

public class FiveProps {
	
	private static final Logger logger = LoggerFactory.getLogger(FiveProps.class);
	
    private static final String DEFNAME = "def.properties";
    private static final String PREFIX = "def";
    private static final String EXTENSION = ".properties";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Properties props = new Properties();
    private boolean fileReadError = false;

    /**
     * Ctor the object from a file name. The file name should
     * be an absolute path that is relative to a classpath. Eg,
     * if fname = "/a/b/c/my.properties", then the following
     * files will be looked for:
     * /a/def.properties
     * /a/b/def.properties
     * /a/b/c/def.properties
     * /a/b/c/my.properties
     * <p/>
     * When paths/files aren't found, warning msgs will be printed,
     * but the code won't fail. At worst you'll end up with an
     * empty Properties object.
     */
    public FiveProps(String fname) {
        this(fname, null, false);
    }

    public FiveProps(String fname, Locale locale) {
        this(fname, locale, false);
    }

    public FiveProps(String fname, boolean loadSysProps) {
        this(fname, null, loadSysProps);
    }

    @SuppressWarnings("rawtypes")
	public FiveProps(String fname, Locale locale, boolean loadSysProps) {
        if (fname == null || fname.length() == 0) {
            fileReadError = true;
            logger.info("FiveProps: WARNING! passed in filename null or empty! No properties loaded");
            return;
        }

        InputStream is = null;
        try {
            // First check to see if the file exists in our path.
            // If not found, complain.
            is = FiveProps.class.getResourceAsStream(fname);
            if (is == null) {
                fileReadError = true;
                logger.info("FiveProps: WARNING! property file: '" + fname + "' not found in classpath!");
            }

            boolean wasDefault = false;
            PathIterator pit = new PathIterator(fname);
            String path;
            String name;

            while ((path = pit.nextPath()) != null) {
                name = path + "/" + DEFNAME;
                if (name.equals(fname)) {
                    wasDefault = true;
                }
                p_load(name);

                if (locale != null) {
                    String lang = locale.getLanguage();
                    if (lang != null && lang.length() > 0) {
                        name = path + "/" + PREFIX + "_" + lang + EXTENSION;
                        if (name.equals(fname)) {
                            wasDefault = true;
                        }
                        p_load(name);

                        String country = locale.getCountry();
                        if (country != null && country.length() > 0) {
                            name = path + "/" + PREFIX + "_" + lang +
                                    "_" + country + EXTENSION;
                            if (name.equals(fname)) {
                                wasDefault = true;
                            }
                            p_load(name);
                        }
                    }
                }
            }

            if (!wasDefault && is != null) {
                logger.info("FiveProps: Loading property file: " + fname);
                p_loadTrimmed(props, is);
            }

            if (locale != null) {
                String lang = locale.getLanguage();
                if (lang != null && lang.length() > 0) {
                    String pre = fname;
                    int idx = fname.lastIndexOf(EXTENSION);
                    if (idx > 0) {
                        pre = fname.substring(0, fname.lastIndexOf(EXTENSION));
                    }
                    name = pre + "_" + lang + EXTENSION;
                    if (!name.equals(fname)) {
                        p_load(name);
                    }

                    String country = locale.getCountry();
                    if (country != null && country.length() > 0) {
                        name = pre + "_" + lang + "_" + country + EXTENSION;
                        if (!name.equals(fname)) {
                            p_load(name);
                        }
                    }
                }
            }


            if (loadSysProps) {
                // load system properties
                Properties systemProperties = System.getProperties();
                for (Enumeration e = systemProperties.propertyNames(); e.hasMoreElements();) {
                    String propertyName = (String) e.nextElement();
                    String propertyValue = systemProperties.getProperty(propertyName);
                    //logger.info( "FiveProps: adding system property: " + propertyName + "=" + propertyValue );
                    props.setProperty(propertyName, propertyValue);
                }
            }
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private void p_load(String name) {
        logger.debug("FiveProps loading resource " + name);

        InputStream rt = FiveProps.class.getResourceAsStream(name);
        if (rt != null) {
            logger.info("FiveProps: Loading property file: " + name);

            p_loadTrimmed(props, rt);

            try {
                rt.close();
            } catch (Exception e) {
                //
            }
        }
    }

    /**
     * Create from a file name. If the file name resolves to the
     * absolute path "/a/b/c/my.properties", then the following
     * files will be looked for:
     * /a/def.properties
     * /a/b/def.properties
     * /a/b/c/def.properties
     * /a/b/c/my.properties
     * <p/>
     * When paths/files aren't found, warning msgs will be printed,
     * but the code won't fail. At worst you'll end up with an
     * empty Properties object.
     */
    static public FiveProps createFromLeafFile(String fname) {
        FiveProps fiveProps = new FiveProps();

        if (fname == null || fname.length() == 0) {
            logger.info("FiveProps: WARNING! passed in filename null or empty! No properties loaded");

            return fiveProps;
        }

        /**
         * First check to see if the file exists in our path.
         * If not found, bail.
         */
        File file = new File(fname);

        if (!file.exists()) {
            logger.info("FiveProps: WARNING! " + fname + " does not exist! No properties loaded");
            return fiveProps;
        }

        InputStream is = null;

        try {
            is = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            logger.info("FiveProps: WARNING! property file: '" + fname + " : " + e.getMessage());
        }

        boolean wasDefault = false;
        InputStream rt;
        PathIterator pit;

        try {
            pit = new PathIterator(file.getCanonicalPath());
        }
        catch (IOException e) {
            logger.info("FiveProps: WARNING! property file: '" + fname + " : " + e.getMessage());

            return fiveProps;
        }

        String path;
        String name;
        Properties props = new Properties();

        while ((path = pit.nextPath()) != null) {
            name = path + "/" + DEFNAME;
            if (name.equals(fname)) {
                wasDefault = true;
            }

            try {
                rt = new FileInputStream(name);

                logger.debug("FiveProps: Loading property file: " + name);

                p_loadTrimmed(props, rt);

                try {
                    rt.close();
                } catch (Exception e) {
                    /* Ignored */
                }
            }
            catch (FileNotFoundException e) {
                logger.warn("FiveProps: " + e.getMessage() + " : " + name);
            }
        }

        if (!wasDefault && is != null) {
            logger.info("FiveProps: Loading property file: " + fname);
            p_loadTrimmed(props, is);
            try {
                is.close();
            } catch (Exception e) { /* Ignored */}
        }

        fiveProps.props = props;

        return fiveProps;
    }

    /**
     * Ctor from an already existing properties object.
     */
    public FiveProps(Properties props) {
        this.props = props;
    }

    /**
     * Ctor from a CfgFile object.
     */
    public FiveProps(CfgFile cfgFile) {
        //--
        // First, populate with the entries that do not have a section.
        //--
        Hashtable ht = cfgFile.getSection();
        Enumeration itr = ht.keys();
        while (itr.hasMoreElements()) {
            String key = (String) itr.nextElement();
            props.put(key, ht.get(key));
        }

        Hashtable sections = cfgFile.getSections();
        Enumeration keys = sections.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            //--
            // Skip over the "default" section since we already
            // handled it up above.
            //--
            if (key.equals("default")) {
                continue;
            }

            Hashtable vals = (Hashtable) sections.get(key);
            Enumeration ivals = vals.keys();
            while (ivals.hasMoreElements()) {
                String keyVal = (String) ivals.nextElement();
                String value = (String) vals.get(keyVal);

                props.put(key + "." + keyVal, value);
            }
        }
    }

    /**
     * Create a FiveProps object from an absolute or relative
     * path name. Only the specified file is read in; there is
     * no def.properties loading.
     *
     * @throws IOException Thrown if the file could not be read.
     */
    public static FiveProps createFromLocalFile(String fname) throws IOException {
        FileInputStream fs = new FileInputStream(fname);
        InputStream in = new PropertiesEncodingInputStream(fs, "UTF8");
        FiveProps fiveProps = new FiveProps();
        fiveProps.props = new Properties();
        fiveProps.props.load(in);
        try {
            in.close();
            fs.close();
        }
        catch (Exception e) {
        }
        return fiveProps;
    }

    private FiveProps() {
    }

    /**
     * Add all the properties from the passed in object to this object.
     * It will override any existing properties.
     */
    public void addAll(FiveProps props) {
        Properties p = props.getProps();
        Enumeration itr = p.keys();
        while (itr.hasMoreElements()) {
            String key = (String) itr.nextElement();

            this.props.put(key, p.get(key));
        }
    }

    /**
     * Retrieve the properties object.
     */
    public Properties getProps() {
        return props;
    }

    /**
     * Return the property value.
     *
     * @param propname The name of the property whose value we want.
     */
    public String get(String propname) {
        return props.getProperty(propname);
    }

    /**
     * Retrieve a property value. Note that if the entry is
     * not found, or the property value is null, the default
     * value is returned.
     *
     * @param propname The name of the property whose value we want.
     * @param defValue A default value to use if the property is not
     *                 found.
     */
    public String get(String propname, String defValue) {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return defValue;
        }

        return sval;
    }

    /**
     * A utility method to retrieve a boolean property.
     */
    public boolean getBoolean(String propname, boolean defaultVal) {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return defaultVal;
        }

        return Boolean.valueOf(sval).booleanValue();
    }

    /**
     * Retrieve an integer property value. Note that if the entry is
     * not found, or the property value is not numeric, a 0 is returned.
     */
    public int getInt(String propname) {
        return getInt(propname, 0);
    }

    /**
     * Retrieve an integer property value. Note that if the entry is
     * not found, or the property value is not numeric, the default
     * value is returned.
     *
     * @param propname The name of the property whose value we want.
     * @param defValue A default value to use if the property is not
     *                 found.
     */
    public int getInt(String propname, int defValue) {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return defValue;
        }

        int val = defValue;
        try {
            val = Integer.parseInt(sval);
        }
        catch (NumberFormatException e) {
        }

        return val;
    }

    /**
     * Retrieve a double property value. Note that if the entry is
     * not found, or the property value does not parse as a double,
     * the default value is returned.
     *
     * @param propname The name of the property whose value we want.
     * @param defValue A default value to use if the property is not
     *                 found.
     */
    public double getDouble(String propname, double defValue) {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return defValue;
        }

        double val = defValue;
        try {
            val = Double.parseDouble(sval);
        }
        catch (NumberFormatException e) {
        }

        return val;
    }

    /**
     * Retrieve a long property value.If a value is not found the default
     * value is returned.
     *
     * @param propname name of the property whose value we want.
     * @param defValue A default value to use if the property is not found.
     */
    public long getLong(String propname, long defValue) {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return defValue;
        }

        long val = defValue;
        try {
            val = Long.parseLong(sval);
        }
        catch (NumberFormatException e) {
        }

        return val;
    }

    /**
     * Returns a list of strings. If a property is missing or the value of a property
     * is an empty string, return an empty Collection. If you really want to know
     * whether a property by a given name exists, use FiveProps.get(propertyName)
     */
    public List<String> getList(String propname, String delim) {
        List<String> result = new LinkedList<String>();
        String sval = props.getProperty(propname);
        if (sval != null) {
            StringTokenizer st;
            if (delim == null) {
                st = new StringTokenizer(sval);
            } else {
                st = new StringTokenizer(sval, delim);
            }
            while (st.hasMoreTokens()) {
                result.add(st.nextToken());
            }
        }

        return result;
    }

    /**
     * <p>Get props list delimited by common delims: the space character,
     * the tab character, the newline character, the carriage-return character,
     * and the form-feed character.</p>
     * <p>If a property is missing or the value of a property
     * is an empty string, return an empty Collection. If you really want to know
     * whether a property by a given name exists, use FiveProps.get(propertyName)</p>
     *
     * @param propName
     * @return List
     */
    public List<String> getList(String propName) {
        return getList(propName, null);
    }

    /**
     * returns a date stored as a short date eg 01/01/2001
     */
    public Date getShortDate(String propname) {
        return getShortDate(propname, null);
    }

    public Date getShortDate(String propname, Date defDate) {
        Date date = defDate;

        String sval = props.getProperty(propname);
        if (sval != null) {
            try {
                date = DateFormat.getDateInstance(DateFormat.SHORT).parse(sval);
            } catch (Exception e) {
            }
        }

        return date;
    }

    /**
     * returns a date stored as a short date (eg 01/01/2001) or a short
     * date and time (eg 01/01/2001 1:30 pm)
     * @throws ParseException 
     */
    public Date getShortDateTime(String propname) throws ParseException {
        String sval = props.getProperty(propname);
        if (sval == null) {
            return null;
        }

        return new java.text.SimpleDateFormat("YYYY-MM-DD HH:mm:ss").parse(sval);
    }

    /**
     * Returns 'true' if the properties object is empty.
     */
    public boolean isEmpty() {
        return props.size() == 0;
    }

    /**
     * Returns true if there was an error reading the primary
     * properties file.
     */
    public boolean getFileReadError() {
        return fileReadError;
    }

    /**
     * Return a string of sorted properties, one to a line.
     */
    public String toString() {
        TreeMap<?, ?> tm = new TreeMap(props);
        Set<?> s = tm.entrySet();
        Iterator<?> itr = s.iterator();
        StringBuffer sb = new StringBuffer();
        boolean first = true;

        while (itr.hasNext()) {
            if (!first) {
                sb.append(LINE_SEPARATOR);
            }

            first = false;
            sb.append(itr.next().toString());
        }

        return new String(sb);
    }

    public static void main(String[] args) {
        String file = args[0];
        FiveProps tp = new FiveProps(file, null);

        logger.info("props = \n" + tp);
    }

    /**
     * Reads a property list (key and element pairs) from the input stream.
     * Unlike Properties.load(), this method trims all property values.
     */
    static private void p_loadTrimmed(Properties props, InputStream input) {
        Properties temp = new Properties();

        try {
            // .properties files are always encoded in UTF8.
            // Wrap the InputStream in an object which can convert from UTF8
            // to something that Properties.load() can understand.
            input = new PropertiesEncodingInputStream(input, "UTF8");
            temp.load(input);

            for (Iterator<Map.Entry<Object, Object>> i = temp.entrySet().iterator(); i.hasNext();) {
                Map.Entry<Object, Object> entry = i.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();

                if (value != null) {
                    value = value.trim();
                    String oldVal = props.getProperty(name);
                    if (value.equals(oldVal)) {
                        logger.debug("FiveProps: DUPLICATE ENTRY for " + name + "=" + oldVal);
                    }
                    props.setProperty(name, value);
                }
            }
        } catch (IOException x) {
            logger.error("FiveProps: ERROR loading props, exc = " + x);
        }
    }
}
