/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Class to represent a compound key, such as ones used
 * for property files.
 * A property style key delimits each key with a '.'.
 * Each subsequent key after a '.' delimiter represents a child key
 * of the previous key.  For example:
 * madden-2004.game_complete.wins would represent
 * madden-2004
 * |
 * +--game_complete
 * |
 * +--wins
 */
public class CompoundKey implements Serializable {
    public transient static final String KEY_DELIMITER = ".";

    private String[] tokens;
    private String key;

    /**
     * Create an instance of CompoundKey with a property string name.
     * String parameter will be treated as a property string, in that any '.'
     * will be considered a delimiter of a key and child keys.
     *
     * @param key String
     */
    public CompoundKey(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("CompoundKey.ctor(): Key cannot be null or empty.");
        }

        this.key = key;
        tokenize();
    }

    public CompoundKey appendKey(String newKey) {
        if (newKey == null || newKey.length() == 0) {
            throw new IllegalArgumentException("CompoundKey.ctor(): Key cannot be null or empty.");
        }

        return new CompoundKey(key + KEY_DELIMITER + newKey);
    }

    /**
     * Get key in a property key string format.
     *
     * @return key    String
     */
    public String getKeyValue() {
        return key;
    }

    /**
     * Get the key at a particular level.
     * Useful to determine the parent keys and child keys
     *
     * @param i int, 1 represents top-most level
     * @return code    String.
     */
    public String getKeyAtLevel(int i) {
        if (i - 1 >= tokens.length) {
            return null;
        } else {
            return tokens[i - 1];
        }
    }

    /**
     * Get a subset of the keys
     *
     * @param start int (inclusive)
     * @param end   int (exclusive)
     * @return subKey    String
     */
    public String getSubKey(int start, int end) {
        StringBuffer subKey = new StringBuffer();

        for (int i = start; i < end; i++) {
            String key = getKeyAtLevel(i);

            if (key != null) {
                if (subKey.length() > 0) {
                    subKey.append(KEY_DELIMITER);
                }

                subKey.append(key);
            }
        }

        return subKey.toString();
    }

    /**
     * Get a tokenized version of the string.
     *
     * @return tokens    String[]
     */
    public String[] getTokens() {
        return tokens;
    }

    /**
     * Get the number of key tokens down this key contains
     *
     * @return count    int
     */
    public int getNumOfTokens() {
        return tokens.length;
    }

    /**
     * Iterator to allow you to walk through the keys
     *
     * @return iterator    Iterator
     */
    public Iterator iterator() {
        return Arrays.asList(tokens).iterator();
    }

    /**
     * Tokenize the keys by seperating each key/child key into a token.
     */
    private void tokenize() {
        StringTokenizer st = new StringTokenizer(key, KEY_DELIMITER);
        tokens = new String[st.countTokens()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = st.nextToken();
        }
    }

    /**
     * String description of CompoundKey
     *
     * @return desc    String
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CompoundKey[");
        sb.append("key=");
        sb.append(key);
        sb.append("]");

        return sb.toString();
    }

    /**
     * Generate the hash code for this object
     *
     * @return int    hash
     */
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Check to see if the keys are equal
     *
     * @param obj Object
     * @return true        If they are equal.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof CompoundKey) {
            CompoundKey oKey = (CompoundKey) obj;

            if (key.equalsIgnoreCase(oKey.getKeyValue())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Read an object from a object stream.
     *
     * @param in ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tokenize();
    }
}
