package com.enjoyf.platform.util;

import java.util.StringTokenizer;
import java.util.Vector;

@Deprecated
public class PathIterator {
    private int cur = 0;
    private String[] elements;

    /**
     * Ctor the object from a filename. Note that no checking is done
     * to see if the dir/file actually exists.
     *
     * @param fileName The absolute pathname of a file.
     */
    public PathIterator(String fileName) {
        StringTokenizer t = new StringTokenizer(fileName, "/");
        Vector v = new Vector();

        while (t.hasMoreTokens()) {
            String s = t.nextToken();
            v.addElement(s);
        }
        elements = new String[v.size()];
        v.copyInto(elements);
    }

    /**
     * Returns the next path, starting from the root. Eg, if the
     * input is "/a/b/c/d". The return values of this routine will be:
     * /a
     * /a/b
     * /a/b/c
     * It is assumed that the leaf is a filename and not returned.
     * <p/>
     * Another example, "a/b/c/d/.", returns:
     * /a
     * /a/b
     * /a/b/c
     * /a/b/c/d
     * <p/>
     * Will return null when done.
     * <p/>
     * The routine IS NOT thread-safe.
     */
    public String nextPath() {
        if (cur >= elements.length - 1) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= cur; i++) {
            sb.append("/" + elements[i]);
        }

        cur++;
        return new String(sb);
    }

    public static void main(String[] args) {
        PathIterator pit = new PathIterator(args[0]);
        String s = null;
        while ((s = pit.nextPath()) != null) {
            System.out.println(s);
        }
    }
}
