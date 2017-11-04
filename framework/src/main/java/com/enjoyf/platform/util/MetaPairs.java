package com.enjoyf.platform.util;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * This class is immutable and thread safe
 * @author Yin Pengyi
 */
@SuppressWarnings("serial")
public class MetaPairs implements Serializable {

    private Map<String, String> pairsTable;
    private String cache;

    public MetaPairs() {
        pairsTable = new HashMap<String, String>();
    }

    /**
     * Ctor to build the object from a hashtable.
     *
     * @param ht A hashtable whose keys/values are assumed to be strings.
     */
    public MetaPairs(Map<String, String> ht) {
        if (ht == null) {
            pairsTable = new HashMap<String, String>();
        } else {
            pairsTable = ht;
            for (Map.Entry<String, String> entry : ht.entrySet()) {
                if (entry.getKey() == null) {
                    throw new IllegalArgumentException("Key cannot be null:");
                }
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("Value cannot be null:");
                }
            }
        }
    }

    /**
     * Construct a container to hold the settings, reconstituting
     * from the passed in string.
     *
     * @param s the string to reconstitue
     */
    public MetaPairs(String s) {
        pairsTable = new HashMap<String, String>();
        reconstitute(s);
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
        return pairsTable.get(name);
    }

    /**
     * Returns the number of entries in the container.
     *
     * @return The number of entries in the container.
     */
    public final int size() {
        return (pairsTable.size());
    }

    /**
     * Restore the object from a string.
     *
     * @param s The string.
     * @return true if successful.
     */
    public boolean reconstitute(String s) {
        pairsTable.clear();
        constitute(pairsTable, s);
        return true;
    }


    public static Map<String, String> constitute(String s) throws ParseException {
        Hashtable<String, String> table = new Hashtable<String, String>();

        if (!constitute(table, s)) {
            throw new ParseException("cannot parser string-->" + s + "<--", 0);
        }

        return table;
    }

    private static boolean constitute(Map<String, String> table, String s) {
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
        return deconstitute();
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
        if (cache == null) {
            cache = deconstitute(pairsTable);
        }
        return cache;
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
     * @param table the hashtable
     * @return The string.
     */
    public static String deconstitute(Map<String, String> table) {
        StringBuilder buffer = new StringBuilder();

        boolean notFirst = false;

        for (String key : table.keySet()) {
            if (notFirst) {
                buffer.append(" ");
            } else {
                notFirst = true;
            }

            buffer.append(key);
            buffer.append(" '");
            p_escape(table.get(key), buffer);
            buffer.append("'");
        }

        return buffer.toString();
    }

    /**
     * Parses a line into tokens. Assumes the separator is
     * whitespace, and that anything enclosed in quoteChar
     * is one token. Use a backslash to escape the quoteChar
     * character.  Use \r to represent a carriage return,
     * \n to represent a newline character, and \anything
     * to represent anything.
     *
     * @param line      The line to parse.
     * @param quoteChar The quote char.
     * @return Returns an array of strings, one per token.
     */
    public static String[] parseLine(String line, char quoteChar) {
        boolean inQuote = false;
        Vector<String> v = new Vector<String>();
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
        for (String key : pairsTable.keySet()) {
            String value = pairsTable.get(key);
            props.put(key, value);
        }
    }

    private static void p_escape(String s, StringBuilder out) {
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

}
