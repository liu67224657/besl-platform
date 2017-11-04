package com.enjoyf.platform.util.http;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class HttpParameter {
    private String name;
    private String value;
    private File file = null;
    private HttpByteData byteData;

    private static final long serialVersionUID = -8708108746980739212L;

    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public HttpParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public HttpParameter(String name, HttpByteData byteData) {
        this.name = name;
        this.byteData = byteData;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public File getFile() {
        return file;
    }

    public boolean isFile() {
        return null != file;
    }

    public HttpByteData getByteData() {
        return byteData;
    }

    public boolean isByteData() {
        return null != byteData;
    }

    private static final String JPEG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";
    private static final String OCTET = "application/octet-stream";

    /**
     * @return content-type
     */
    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String contentType;
        String extensions = file.getName();
        int index = extensions.lastIndexOf(".");
        if (-1 == index) {
            // no extension
            contentType = OCTET;
        } else {
            extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
            if (extensions.length() == 3) {
                if ("gif".equals(extensions)) {
                    contentType = GIF;
                } else if ("png".equals(extensions)) {
                    contentType = PNG;
                } else if ("jpg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else if (extensions.length() == 4) {
                if ("jpeg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else {
                contentType = OCTET;
            }
        }
        return contentType;
    }


    public static boolean containsFile(HttpParameter[] params) {
        boolean containsFile = false;
        if (null == params) {
            return false;
        }
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    /*package*/
    static boolean containsFile(List<HttpParameter> params) {
        boolean containsFile = false;
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    public static HttpParameter[] getParameterArray(String name, String value) {
        return new HttpParameter[]{new HttpParameter(name, value)};
    }

    public static HttpParameter[] getParameterArray(String name, int value) {
        return getParameterArray(name, String.valueOf(value));
    }

    public static HttpParameter[] getParameterArray(String name1, String value1
            , String name2, String value2) {
        return new HttpParameter[]{new HttpParameter(name1, value1)
                , new HttpParameter(name2, value2)};
    }

    public static HttpParameter[] getParameterArray(String name1, int value1
            , String name2, int value2) {
        return getParameterArray(name1, String.valueOf(value1), name2, String.valueOf(value2));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof HttpParameter) {
            HttpParameter that = (HttpParameter) obj;

            if (file != null ? !file.equals(that.file) : that.file != null)
                return false;

            return this.name.equals(that.name) &&
                    this.value.equals(that.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return "HttpParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", file=" + file +
                '}';
    }

    public int compareTo(Object o) {
        int compared;
        HttpParameter that = (HttpParameter) o;
        compared = name.compareTo(that.name);
        if (0 == compared) {
            compared = value.compareTo(that.value);
        }
        return compared;
    }

    public static String encodeParameters(HttpParameter[] httpParams) {
        if (null == httpParams) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < httpParams.length; j++) {
            if (httpParams[j].isFile()) {
                throw new IllegalArgumentException("parameter [" + httpParams[j].name + "]should be text");
            }
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(httpParams[j].name, "UTF-8"))
                        .append("=").append(URLEncoder.encode(httpParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();

    }
}
