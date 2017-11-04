package com.enjoyf.platform.util.validate;

import com.enjoyf.platform.io.mail.EmailAddress;

/**
 * A utility class which validates e-mail addresses.
 * The implementation for this regular expression was adapted from an adaptation of the
 * validator in the back of "Mastering Regular Expressions".
 *
 * @author Yin Pengyi
 */
public class EmailValidator extends Validator {
    public static final String UNKNOWN = "unknown@nowhere.com";

    // Let's avoid backslash mania...
    private static String PERIOD = "\\.";
    private static String ALPHANUM = "\\w";

    // An ATOMCHAR defines all legal characters for name/address parts.
    private static String ATOMCHAR = "[" + ALPHANUM + "+&-]";

    // An ATOM is some number of ATOMCHARS not follow by something that could be part of an ATOM.
    private static String ATOM = ATOMCHAR + "+(?!" + ATOMCHAR + ")";

    // A DOMAIN_PART is just an ATOM...
    private static String DOMAIN_PART = ATOM;

    // A DOMAIN is a DOMAIN_PART followed by any number of DOMAIN_PART seperated by periods.
    private static String DOMAIN = DOMAIN_PART + "(?:" + PERIOD + DOMAIN_PART + ")+";

    // The user's NAME is just an ATOM followed by any number of ATOMS seperated by periods.
    private static String NAME = ATOM + "(?:" + PERIOD + ATOM + ")*";

    // The full spec.
    private static String ADDR_SPEC = NAME + "@" + DOMAIN;

    protected static Validator instance = new EmailValidator();

    protected EmailValidator() {
        super();

        String pattern = ADDR_SPEC;
        setPattern(pattern);
    }


    public boolean isValid(EmailAddress address) {
        return isValid(address.getEmail());
    }


    public static Validator instance() {
        return instance;
    }


    public static boolean isSet(String email) {
        if (email == null || email.length() == 0) {
            return false;
        }

        if (!EmailValidator.instance().isValid(email)) {
            return false;
        }

        if (email.equalsIgnoreCase(UNKNOWN)) {
            return false;
        }

        return true;
    }
}