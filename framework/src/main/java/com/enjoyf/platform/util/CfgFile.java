/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Hashtable;

import com.enjoyf.platform.util.log.GAlerter;


/**
 * A class to support access to a cfg file. The files are your typical
 * .ini files. Each file can have any number of sections, and each section
 * any number of entries. Sections start with some string in square brackets:
 * [section1] and end with the start of another section (or EOF). Any
 * entries at the beginning are assumed to be in the "default" section.
 * Entries are of the form: <x> = <y>, where x and y can be anything. No
 * special escaping or parsing is done, <x> is anything the left of the
 * first equal sign, and <y> is anything to the right. Both <x> and <y> are
 * trimmed for whitespace.
 */
public class CfgFile {

    private static String DEFAULT_SECTION = "default";
    private Hashtable<String, Hashtable<String, String>> sections = 
	new Hashtable<String, Hashtable<String, String>>(10);

    /**
     * Builds up the object from the specified file.
     *
     * @param r A Reader that will be read from to get the configuration.
     */
    public CfgFile(Reader r) throws FileNotFoundException, IOException {
        String lineRead = null;
        String currentSection = DEFAULT_SECTION;
        BufferedReader rdr = new BufferedReader(r);
        p_allocateSection(currentSection);

        while ((lineRead = rdr.readLine()) != null) {
            String s = lineRead.trim();

            if (s.equals("")) {
                continue;
            }

            if (s.startsWith("#")) {
                continue;
            }

            if (p_isSection(s)) {
                String s1 = (s.substring(1, s.length() - 1)).trim();
                p_allocateSection(s1);
                currentSection = s1;
            }

            if (p_isEntry(s)) {
                int idx = s.indexOf('=');
                String key = s.substring(0, idx);
                String value = s.substring(idx + 1, s.length());
                p_addEntry(currentSection, key.trim(), value.trim());
            }
        }
    }

    /**
     * Builds up the object from the specified file.
     *
     * @param pathname This is the pathname of the file to
     *                 read. Must be the full name (including any suffix).
     */
    public CfgFile(String pathname) throws FileNotFoundException, IOException {
        this(new LineNumberReader(new FileReader(pathname)));
    }


    /**
     * An empty constructor.  Used internally only to support
     * package relative resource location in a static method.
     */
    private CfgFile() {
    }

    /**
     * Returns a CfgFile made from the specified file, which is
     * named relative to the classroot (ie, "/ini/x.ini").
     * Returns null if an error occurs. <p>
     *
     * @param pathname This is the pathname of the file to
     *                 read. This name is classroot relative.
     */
    public static CfgFile create(String pathname) {
        CfgFile nullCfg = new CfgFile();        // to get Class only
        CfgFile cfg = null;

        try {
            InputStream is = nullCfg.getClass().getResourceAsStream(pathname);
            if (is == null) {
                throw new IOException("couldn't load config resource");
            }

            cfg = new CfgFile(new InputStreamReader(is));
        } catch (Exception e) {
            GAlerter.lab("Could not read ini file: ", pathname, e);
        }

        return cfg;
    }

    /**
     * Return a hashtable of entries for the default section.
     *
     * @return Returns a hashtable containing all
     *         entries in the default section. Returns null if the section
     *         was not found.
     */
    public Hashtable<String, String> getSection() {
        return sections.get(DEFAULT_SECTION);
    }

    /**
     * Return a hashtable of entries for the specified
     * section.
     *
     * @return Returns a hashtable containing all the
     *         entries in the section. May return null if the section
     *         was not found.
     * @secName The section name we are interested in.
     */
    public Hashtable<String, String> getSection(String secName) {
        return sections.get(secName);
    }

    /**
     * Return the value for a key in the default section.
     *
     * @param key The key whose value we want.
     * @return Returns the value if found. Otherwise null is returned.
     */
    public String getEntry(String key) {
        return getEntry(DEFAULT_SECTION, key);
    }

    /**
     * Return the value (as an integer) for a key in the default
     * section.
     *
     * @param key The key whose value we want.
     * @return See the other getIntEntry method.
     */
    public int getIntEntry(String key) {
        return getIntEntry(DEFAULT_SECTION, key);
    }

    /**
     * Return the value for a key in the specified section.
     *
     * @param section The section to look in.
     * @param key     The key whose value we want.
     * @return Returns the value of the key. If the section
     *         was not found, or the key was not found, a null is returned.
     */
    public String getEntry(String section, String key) {
        Hashtable<String, String> ht = getSection(section);
        if (ht == null) {
            return null;
        }

        return ht.get(key);
    }

    /**
     * Return an entry as an integer.
     *
     * @param section The section to look in.
     * @param key     The key whose value we want.
     * @return Returns the value as an integer. If the section or
     *         entry was not found, a 0 is returned. If the value could not
     *         be interpreted as an integer, a 0 is returned. This does overload
     *         the return value somewhat. If you need tighter control, just use
     *         getEntry() (this is a convenience method).
     */
    public int getIntEntry(String section, String key) {
        String sval = getEntry(section, key);
        if (sval == null) {
            return 0;
        }

        int val = 0;
        try {
            val = Integer.parseInt(sval);
        }
        catch (NumberFormatException e) {
        }

        return val;
    }

    /**
     * Return an entry as an boolean.
     *
     * @param section    The section to look in.
     * @param key        The key whose value we want.
     * @param defaultVal Value to return if the entry does not exist.
     * @return Returns the value as a boolean.  If the value does
     *         not exist, returns the default argument.  See the JDK doc
     *         for the precise definition of boolean strings (in class java.lang.Boolean).
     */
    public boolean getBooleanEntry(String section, String key, boolean defaultVal) {
        String sval = getEntry(section, key);
        if (sval == null) {
            return defaultVal;
        }

        return Boolean.valueOf(sval).booleanValue();
    }

    /**
     * Return a hashtable of hashtables. The key of the first hashtable
     * is the section name, the value another hashtable. The secondary
     * hashtable has key/value pairs (both strings).
     */
    public Hashtable<String, Hashtable<String, String>> getSections() {
        return sections;
    }

    private void p_allocateSection(String secName) {
        sections.put(secName, new Hashtable<String, String>(15));
    }

    private boolean p_isSection(String sec) {
        if (sec.startsWith("[") && sec.endsWith("]")) {
            return true;
        }

        return false;
    }

    private boolean p_isEntry(String s) {
        if (s.indexOf('=') != -1) {
            return true;
        }

        return false;
    }

    private void p_addEntry(String section, String key, String value) {
        Hashtable<String, String> ht = getSection(section);

        if (ht == null) {
            throw new RuntimeException("CfgFile: section not found! " + section);
        }

        ht.put(key, value);
    }

}
