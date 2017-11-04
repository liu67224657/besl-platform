package com.enjoyf.platform.webapps.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class UrlUtil {
        private static final String ENCODE_UTF8 = "utf-8";

    /**
     * Retrieve the contents from the given URL as a String, assuming the URL's
     * server matches what we expect it to match.
     */
    public static String retrieveSecureURL(String secureUrl) throws IOException {
        BufferedReader r = null;
        try {
            URL u = new URL(secureUrl);
            if (!u.getProtocol().equals("https")) {
                throw new IOException("only 'https' URLs are valid for this method");
            }

            URLConnection uc = u.openConnection();
            uc.setRequestProperty("Connection", "close");
            r = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            String line;
            StringBuffer buf = new StringBuffer();
            while ((line = r.readLine()) != null) {
                buf.append(line + "\n");
            }

            return buf.toString();
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException ex) {
                // ignore
            }
        }
    }


    public static String retrieveURL(String url) throws IOException {
        BufferedReader r = null;
        try {
            URL u = new URL(url);

            URLConnection uc = u.openConnection();
            uc.setRequestProperty("Connection", "close");
            r = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            String line;
            StringBuffer buf = new StringBuffer();
            while ((line = r.readLine()) != null) {
                buf.append(line + "\n");
            }

            return buf.toString();
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    public static String encode(String url) {
        return encode(url, ENCODE_UTF8);
    }

    public static String encode(String url, String enc) {
        try {
            return URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    public static String decode(String url) {
        return decode(url, ENCODE_UTF8);
    }

    public static String decode(String url, String enc) {
        try {
            return URLDecoder.decode(url, enc);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

}
