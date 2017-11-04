package com.enjoyf.platform.util.apk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author wuqiang
 */
public class AndroidXMLParser {

    private static final String TAG_USES_PERMISSION_MANIFEST = "/uses-permission/manifest";
    private static final String TAG_USES_SDK_MANIFEST = "/uses-sdk/manifest";
    private static final String TAG_MANIFEST = "/manifest";
//	private static final String TAG_SUPPORTS_SCREENS_MANIFEST = "/supports-screens/manifest";
    private static final float RADIX_MULTS[] = { 0.00390625F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F };
    private static final String DIMENSION_UNITS[] = { "px", "dip", "sp", "pt", "in", "mm", "", "" };
    private static final String FRACTION_UNITS[] = { "%", "%p", "", "", "", "", "", "" };

    private File apkFile;
    private File tmpDir;
    private boolean forceDownload;
    private URL url;
    private Deque<String> tagStack = new ArrayDeque<String>();
    private APKInfo result = new APKInfo();

    public AndroidXMLParser(File apkFile) throws IOException {
        this.apkFile = apkFile;

        if(this.apkFile == null || !this.apkFile.isFile()){
            throw new IOException("You passed a unknown file to the AndroidXMLParser.");
        }
    }

    public AndroidXMLParser(URL url, File tmpDir, boolean forceDownload) throws IOException {
        this.url = url;
        this.tmpDir = tmpDir;
        this.forceDownload = forceDownload;
    }

    public APKInfo getResult() {
        if (tagStack.isEmpty()) {
            return result;
        } else {
            throw new RuntimeException("XML tag stack is not empty");
        }
    }

    public String parse() throws XmlPullParserException, IOException, NoSuchAlgorithmException {
        AXmlResourceParser parser = new AXmlResourceParser();
        if (this.apkFile == null) {
            this.apkFile = new File(tmpDir, url.toString().substring( url.toString().lastIndexOf("/") + 1 ));
        }

        if (!apkFile.exists() || forceDownload) {
            URLConnection conn = this.url.openConnection();
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.apkFile));
            byte[] buf = new byte[2048];
            while (true) {
                int len = in.read(buf);
                if (len <= 0) break;
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

        setFileSize();
        JarFile jarFile = new JarFile(apkFile);
        JarEntry entry = jarFile.getJarEntry("AndroidManifest.xml");
        InputStream in = new BufferedInputStream(jarFile.getInputStream(entry));

        StringBuilder sb = new StringBuilder();
        try {
            parser.open(in);
            while (true) {
                int type = parser.next();
                if (type == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                switch (type) {
                    case XmlPullParser.START_DOCUMENT: {
                        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        break;
                    }
                    case XmlPullParser.START_TAG: {
                        processStartTag(parser, sb);
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        processEndTag(parser, sb);
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        sb.append(parser.getText());
                        break;
                    }
                }
            }
        } finally {
            if (parser != null) parser.close();
            if (in != null) in.close();
        }
        return sb.toString();
    }

    private void setFileSize() throws IOException, NoSuchAlgorithmException {
        result.setSize(this.apkFile.length());
        FileInputStream fis = new FileInputStream( apkFile );
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex( fis );
        result.setMd5hash(md5);
    }

    private void processEndTag(AXmlResourceParser parser, StringBuilder sb) {
        tagStack.pop();
        sb.append("</");
        sb.append(getNamespacePrefix(parser.getPrefix()));
        sb.append(parser.getName());
        sb.append(">");
    }

    private void processStartTag(AXmlResourceParser parser, StringBuilder sb)
            throws XmlPullParserException
    {
        sb.append("<");
        String tag = getNamespacePrefix(parser.getPrefix()) + parser.getName();
        tagStack.push(tag);
        sb.append(tag);

        int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
        int namespaceCount = parser.getNamespaceCount(parser.getDepth());
        for (int i = namespaceCountBefore; i != namespaceCount; ++i) {
            sb.append(" xmlns:");
            sb.append(parser.getNamespacePrefix(i));
            sb.append("=\"");
            sb.append(parser.getNamespaceUri(i));
            sb.append("\"");
        }

        for (int i = 0; i != parser.getAttributeCount(); ++i) {
            String attr = getNamespacePrefix(parser.getAttributePrefix(i)) + parser.getAttributeName(i);
            String value = getAttributeValue(parser, i);
            sb.append(" ");
            sb.append(attr);
            sb.append("=\"");
            sb.append(value);
            sb.append("\"");

            if (tagMatch(TAG_MANIFEST) && attr.equals("android:versionName")) {
                result.setVersionName(value);
            }
            if (tagMatch(TAG_MANIFEST) && attr.equals("android:versionCode")) {
                result.setVersionCode(Integer.valueOf(value));
            }
            if (tagMatch(TAG_MANIFEST) && attr.equals("package")) {
                result.setSlug(value);
            }
            if (tagMatch(TAG_USES_SDK_MANIFEST) && attr.equals("android:minSdkVersion")) {
                result.setMinSDKVersion(Integer.valueOf(value));
            }
            if (tagMatch(TAG_USES_PERMISSION_MANIFEST) && attr.equals("android:name")) {
                result.getPermissions().add(value);
            }
            //TODO: process permissionGroup and level
        }
        sb.append(">");
    }

    private boolean tagMatch(String path) {
        StringBuilder sb = new StringBuilder();
        for (String s : tagStack) {
            sb.append("/");
            sb.append(s);
        }
        return sb.toString().equals(path);
    }

    private String getNamespacePrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return "";
        }
        return prefix + ":";
    }

    private String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == TypedValue.TYPE_STRING) {
            return parser.getAttributeValue(index);
        }
        if (type == TypedValue.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == TypedValue.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (type == TypedValue.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (type == TypedValue.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data))
                    + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type == TypedValue.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data))
                    + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type >= TypedValue.TYPE_FIRST_COLOR_INT
                && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (type >= TypedValue.TYPE_FIRST_INT
                && type <= TypedValue.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

    private String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    private static float complexToFloat(int complex) {
        return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

}