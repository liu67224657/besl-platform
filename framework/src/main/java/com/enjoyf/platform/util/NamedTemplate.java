package com.enjoyf.platform.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Yin Pengyi
 */

/**
 * Like java.text.TextFormat, NamedTemplate class is a text template with dynamic string substituion.
 * However, it does not have the hard-to-read and hard-to-use numeric argument problem.
 * <p/>
 * Instead of using {0}, {1}, etc to identify the dynamic string position, it uses more readable
 * and also reusable string key in the format of ${key.name}
 * <p/>
 * Note: preceding backslash can escape the especial char sequeue, "${". e.g. "\\${" will be translated as "${"
 * <p/>
 * For example,
 * <p/>
 * NamedTemplate template =  new NamedTemplate("${user.name} wins double jackpot in ${game.name}.  ${user.name} is club platform member.");
 * Map map = new HashMap();
 * map.put("user.name", "Oscar Chan");
 * map.put("game.name", "Popfu");
 * <p/>
 */
public class NamedTemplate {
    
    private static final Logger logger = LoggerFactory.getLogger(NamedTemplate.class);
    
    private static final String QUOTE_START_CHAR = "${";
    private static final String QUOTE_END_CHAR = "}";
    private static final char ESCAPE_CHAR = '\\';

    private ArrayList<String> segmentList = new ArrayList<String>();

    // key = integer of position, value = parameter name
    private Map<Integer, String> argsMap = new TreeMap<Integer, String>();

    /**
     * @param template
     * @throws IllegalArgumentException if template is missing or the content of template is not invalid
     */
    private NamedTemplate(String template) throws IllegalArgumentException {
        if (template == null) {
            throw new IllegalArgumentException("missing template");
        }

        p_parse(template);
    }

    /**
     * @param text is the template text
     * @return a NamedTemplate object representing the text.  null if the text argument is invalid
     */
    public static NamedTemplate parse(String text) {
        NamedTemplate template = null;

        try {
            template = new NamedTemplate(text);
        } catch (IllegalArgumentException e) {
            GAlerter.lab("Unable to create template", e);
        }

        return template;
    }

    public static NamedTemplate read(String classPath) {
        String text = p_readFromResource(classPath);

        if (text == null) {
            return null;
        }

        NamedTemplate template = NamedTemplate.parse(text);

        return template;
    }

    /**
     * read the context of a file from resource
     *
     * @param path
     * @return the context of the file. null if no such file
     */
    private static String p_readFromResource(String path) {
        StringBuffer content = new StringBuffer();

        InputStream istream = NamedTemplate.class.getResourceAsStream(path);

        if (istream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

            try {
                String line = reader.readLine();
                while (line != null) {
                    content.append(line).append("\n");

                    line = reader.readLine();
                }
            } catch (IOException e) {
                logger.error("NamedTemplate: unable to read template from " + path, e);
            }
        } else {
            return null;
        }

        return content.toString();
    }

    /**
     * @param template
     * @throws IllegalArgumentException if template format is not valid
     */
    private void p_parse(String template) throws IllegalArgumentException {
        ParsePosition currentIndex = new ParsePosition(0);

        int paramIndex = 0;
        while (currentIndex.getIndex() <= template.length()) {
            String segment = p_extractStaticSegment(currentIndex, template);
            if (segment != null) {
                segmentList.add(segment);
            }

            if (currentIndex.getIndex() >= template.length()) {
                break;
            }

            String key = p_extractKey(currentIndex, template);
            if (key != null) {
                argsMap.put(new Integer(paramIndex++), key);
            }
        }
    }

    /**
     * extract the static text of the template from the current position until dynamic key is reached
     *
     * @param currentIndex
     * @param template
     * @return
     */
    public String p_extractStaticSegment(ParsePosition currentIndex, String template) {
        String segment;

        if (currentIndex.getIndex() >= template.length()) {
            return "";
        }

        int quoteStartIndex = template.indexOf(QUOTE_START_CHAR, currentIndex.getIndex());

        if (quoteStartIndex == -1) {
            // if QUOTE_START is not found
            segment = template.substring(currentIndex.getIndex());
            currentIndex.setIndex(template.length());
        } else if (quoteStartIndex != 0 && template.charAt(quoteStartIndex - 1) == ESCAPE_CHAR) {
            // if the QUOTE_START char is ESCAPED
            // look for the next QUOTE_START
            // extract everything before the ESCAPE_CHAR
            String part = template.substring(currentIndex.getIndex(), quoteStartIndex - 1);

            // increment index to skip the current QUOTE_START
            currentIndex.setIndex(quoteStartIndex + QUOTE_START_CHAR.length());
            String remaining = p_extractStaticSegment(currentIndex, template);
            segment = part + QUOTE_START_CHAR + remaining;

            //current index should be set by the recursive calls
        } else {
            // if QUOTE_START is found
            segment = template.substring(currentIndex.getIndex(), quoteStartIndex);
            currentIndex.setIndex(quoteStartIndex);
        }

        return segment;
    }

    /**
     * The format of the string should be in format "${key}..."
     * where key is an indString identifier that subject to java identifier rule string.
     * the pointer will be forward to the end of closing brace if succeed.
     * Otherwise, null will be returned.
     *
     * @param currentIndex current index
     * @param template     template
     * @return
     */
    private String p_extractKey(ParsePosition currentIndex, String template) {
        String key = null;

        if (!template.startsWith(QUOTE_START_CHAR, currentIndex.getIndex())) {
            throw new IllegalArgumentException("unable to locate QUOTE_START at " + currentIndex.getIndex());
        }

        int quoteEndIndex = template.indexOf(QUOTE_END_CHAR, currentIndex.getIndex());

        if (quoteEndIndex == -1) {
            // QUOTE_END is not found
            throw new IllegalArgumentException("unable to locale QUOTE_END at" + currentIndex.getIndex());
        } else {
            // QUOTE_END is found
            key = template.substring(currentIndex.getIndex() + QUOTE_START_CHAR.length(), quoteEndIndex);
            currentIndex.setIndex(quoteEndIndex + 1);

            if (!isIdentifier(key)) {
                throw new IllegalArgumentException("unaccepted identifier: " + key + " at " + currentIndex.getIndex());
            }
        }

        return key;
    }

    /**
     * true if it is valid identifier
     * valid identifer is defined as below:
     * <br>		first char is true for Character.isJavaIdentifierStart()
     * <br>		remaining char is true for Character.isJavaIdentiferPart() or '.'
     *
     * @param identifier
     * @return
     */
    private boolean isIdentifier(String identifier) {
        boolean isFirst = true;

        for (int i = 0; i < identifier.length(); i++) {
            char ch = identifier.charAt(i);

            if (isFirst && Character.isJavaIdentifierStart(ch)) {
                // first char of key
            } else if (Character.isJavaIdentifierPart(ch) || ch == '.') {
                // remaining chars of the key
            } else {
                // error in format
                return false;
            }

            isFirst = false;
        }

        return true;
    }

    public Collection<String> getParameterNames() {
        return Collections.unmodifiableCollection(argsMap.values());
    }

    public int countParameters() {
        return argsMap.size();
    }

    public String toString() {
        StringBuffer text = new StringBuffer();

        int i = 0;
        for (Iterator<String> segItr = segmentList.iterator(); segItr.hasNext();) {
            String segment = segItr.next();
            text.append(segment);

            if (segItr.hasNext()) {
                text.append("<").append(argsMap.get(new Integer(i++))).append(">");
            }
        }

        return text.toString();
    }


    public String format(Map<String, String> dataMap) {
        StringBuffer text = new StringBuffer();
        int i = 0;
        for (Iterator<String> segItr = segmentList.iterator(); segItr.hasNext();) {
            String segment = segItr.next();
            text.append(segment);
            if (segItr.hasNext()) {
                String keyName = argsMap.get(new Integer(i++));

                if (!dataMap.containsKey(keyName)) {
                    //todo
                    //maybe we can set blank for this arg
                    throw new IllegalArgumentException("unable to format text because of missing parameter: " + keyName);
                }

                text.append(dataMap.get(keyName));
            }
        }

        return text.toString();
    }

}