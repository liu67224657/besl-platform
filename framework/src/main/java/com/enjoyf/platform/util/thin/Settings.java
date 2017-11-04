/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thin;

import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 * Container class for settings. The class contains any number of
 * name value pairs, each of which is a string.
 * Names are required to be alphanumeric.
 * <p/>
 * NOTE: THIS CLASS IS BUILT INTO MANY GAME APPLETS. If it is changed on /main/LATEST,
 * it will trigger a re-download of these applets. It should be changed only on the applet
 * development branch, which is released to production every few months or so. If you have
 * questions, please ask Jim Greer or Mike Riccio.
 */
public class Settings {
    private Hashtable table;
    private String cache;
    private boolean dirty;

    /**
     * Constructs a container to hold the settings.
     */
    public Settings() {
        table = new Hashtable();
        dirty = true;
    }

    /**
     * Ctor to build the object from a hashtable.
     *
     * @param ht A hashtable whose keys/values are assumed to be strings.
     */
    public Settings(Hashtable ht) {
        if (ht == null) {
            table = new Hashtable();
        } else {
            table = ht;
        }
        dirty = true;
    }

    /**
     * Construct a container to hold the settings, reconstituting
     * from the passed in string.
     */
    public Settings(String s) {
        table = new Hashtable();
        reconstitute(s);
    }

    /**
     * Adds an entry into the container.
     * If an entry already exists with that name, its value
     * is replaced.
     *
     * @param name  The name of the entry.  Must be alphanumeric.
     * @param value The value of the entry. Note that a null
     *              value arg is stored as an empty string.
     */
    public void add(String name, String value) {
        synchronized (table) {
            table.put(name, (value != null) ? value : "");
            dirty = true;
        }
    }

    /**
     * Add all of the settings from the passed in arg to the current
     * settings object. If a setting already exists in the current
     * object, it is replaced.
     *
     * @param s The settings object to add.
     */
    public void add(Settings s) {
        Hashtable t = s.table;
        synchronized (table) {
            synchronized (t) {
                Enumeration itr = t.keys();
                while (itr.hasMoreElements()) {
                    String key = (String) itr.nextElement();
                    table.put(key, t.get(key));
                }
            }

            dirty = true;
        }
    }

    /**
     * Removes an entry from the container.
     *
     * @param name This is the key of the entry.
     * @return Returns 'false' if we could not find the entry.
     */
    public final boolean remove(String name) {
        boolean result = false;

        synchronized (table) {
            result = (table.remove(name) != null);
            if (result) {
                dirty = true;
            }
        }

        return result;
    }

    /**
     * Returns the value for a particular entry.
     *
     * @param name This identifies which entry we are interested in.
     * @return Returns null if 'name' is not found.
     *         If an empty string is returned, this
     *         means the name was found, but its value was
     *         empty.
     */
    public final String getValue(String name) {
        return ((String) table.get(name));
    }

    /**
     * Returns the number of entries in the container.
     *
     * @return The number of entries in the container.
     */
    public final int size() {
        return (table.size());
    }

    /**
     * Returns an enumerator for this container that
     * enumerates over the names in the container.
     */
    public final Enumeration elements() {
        return (table.keys());
    }


    /**
     * Restore the object from a string.
     *
     * @param s The string.
     * @return true if successful.
     */
    public boolean reconstitute(String s) {
        synchronized (table) {
            dirty = true;
            table.clear();
            constitute(table, s);
        }

        return true;
    }


    public static Hashtable constitute(String s)
            throws ParseException {
        Hashtable table = new Hashtable();

        if (!constitute(table, s)) {
            throw new ParseException("cannot parser string-->" + s + "<--", 0);
        }

        return table;
    }

    // recreating Hashtable(table) using string (s)
    private static boolean constitute(Hashtable table, String s) {
        table.clear();

        if (s != null && s.length() > 0) {

            String tokens[] = parseLine(s, '\'');
            if (tokens.length < 2 || tokens.length % 2 != 0) {
                return (false);
            }

            for (int i = 0; (i < tokens.length); i += 2) {
                String name = tokens[i];
                String value = tokens[i + 1];

                table.put(name, (value != null) ? value : "");
            }
        }

        return true;
    }


    /**
     * Turn the object into a string.
     * <p/>
     * The format is "key1 'escapedvalue1' key2 'escapedvalue2'..."
     * Where escapedvalue represents the value with all single
     * quotes turned into \' and all backslashes turned into \\.
     * <p/>
     * NOTE: The string value of the object is always cached
     * and updated every time the object is modified.
     *
     * @return The string.
     */
    public final String toString() {
        return (deconstitute());
    }

    /**
     * Turn the object into a string.
     * <p/>
     * The format is "key1 'escapedvalue1' key2 'escapedvalue2'..."
     * Where escapedvalue represents the value with all single
     * quotes turned into \' and all backslashes turned into \\.
     * <p/>
     * NOTE: The string value of the object is always cached
     * and updated every time the object is modified.
     *
     * @return The string.
     */
    public String deconstitute() {

        synchronized (table) {
            if (dirty) {
                dirty = false;
                cache = deconstitute(table);
            }

            return (cache);
        }
    }

    /**
     * Turn the hashtable into a string.
     * <p/>
     * The format is "key1 'escapedvalue1' key2 'escapedvalue2'..."
     * Where escapedvalue represents the value with all single
     * quotes turned into \' and all backslashes turned into \\.
     * Newlines are turned into \n and carriage returns are
     * turned into \r.
     *
     * @return The string.
     */
    public static String deconstitute(Hashtable table) {
        StringBuffer buffer = new StringBuffer();

        synchronized (table) {
            boolean notFirst = false;

            Enumeration e = table.keys();
            while (e.hasMoreElements()) {
                if (notFirst) {
                    buffer.append(" ");
                } else {
                    notFirst = true;
                }

                String key = (String) e.nextElement();
                buffer.append(key);
                buffer.append(" '");
                p_escape((String) table.get(key), buffer);
                buffer.append("'");
            }
        }

        return (new String(buffer));
    }

    /**
     * Parses a line into tokens. Assumes the separator is
     * whitespace, and that anything enclosed in quoteChar
     * is one token. Use a backslash to escape the quoteChar
     * character.  Use \r to represent a carriage return,
     * \n to represent a newline character, and \anything
     * to represent anything.
     *
     * @param line      line to parse.
     * @param quoteChar The quote char.
     * @return Returns an array of strings, one per token.
     */
    public static String[] parseLine(String line, char quoteChar) {
        boolean inQuote = false;
        Vector v = new Vector();
        StringBuffer sb = new StringBuffer();
        int len = line.length();

        int i = 0;
        while (i < len) {
            char c = line.charAt(i++);

            if (c == '\\') {
                // If at EOL, eat the backslash.
                if (i >= len) {
                    break;
                }

                c = line.charAt(i++);
                switch (c) {
                    case'r':
                        sb.append('\r');
                        break;
                    case'n':
                        sb.append('\n');
                        break;
                    default:
                        // anything else becomes whatever follows it
                        sb.append(c);
                }
            } else if (c == quoteChar) {
                if (inQuote) {
                    v.addElement(new String(sb));
                    sb.setLength(0);
                    inQuote = false;
                } else {
                    inQuote = true;
                }
            } else {
                if (inQuote) {
                    sb.append(c);
                } else if (Character.isWhitespace(c)) {
                    if (sb.length() > 0) {
                        v.addElement(new String(sb));
                        sb.setLength(0);
                    }
                } else {
                    sb.append(c);
                }
            }
        }

        if (sb.length() > 0) {
            v.addElement(new String(sb));
        }

        String[] retargs = new String[v.size()];
        v.copyInto(retargs);
        return (retargs);
    }


    /**
     * Copies the name value pairs from the settings
     * object into a props object
     *
     * @param props The properties object to copy the settings into
     */
    public void copy(Properties props) {
        synchronized (table) {
            Enumeration e = table.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = (String) table.get(key);
                props.put(key, value);
            }
        }
    }

    private static void p_escape(String s, StringBuffer out) {
        int len = s.length();
        for (int i = 0; (i < len); i++) {
            char c = s.charAt(i);
            switch (c) {
                case'\'':
                    out.append("\\'");
                    break;
                case'\r':
                    out.append("\\r");
                    break;
                case'\n':
                    out.append("\\n");
                    break;
                case'\\':
                    out.append("\\\\");
                    break;
                default:
                    out.append(c);
            }
        }
    }

    public Hashtable getInternalHashtable() {
        return table;
    }
}
