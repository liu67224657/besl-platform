package com.enjoyf.platform.util.validate;

import java.util.regex.Pattern;

/**
 * @author Yin Pengyi
 */
public abstract class Validator {
    private Pattern pattern;

    protected Validator() {
        pattern = null;
    }

    protected void setPattern(String regexp) {
        pattern = Pattern.compile(regexp);
    }

    public boolean isValid(String input) {
        if (input != null) {
            if (pattern != null) {
                if (pattern.matcher(input).find()) {
                    return true;
                }
            }
        }

        return false;
    }
}
