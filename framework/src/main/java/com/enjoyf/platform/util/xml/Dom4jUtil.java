package com.enjoyf.platform.util.xml;

import com.enjoyf.util.IgnoreDTDEntityResolver;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public final class Dom4jUtil {
    public static Document getDocument(String xml, boolean isIgnoreDTD) throws Exception {
        SAXReader saxReader = new SAXReader();
        if (isIgnoreDTD) {
            saxReader.setEntityResolver(new IgnoreDTDEntityResolver());
        }
        Document doc = saxReader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        return doc;
    }

    public static Document getDocument(InputStream is) throws Exception {
        SAXReader saxReader = new SAXReader();
        return saxReader.read(is);
    }

    public static Document getDocument(String xml) throws Exception {
        return com.enjoyf.util.Dom4jUtil.getDocument(xml, false);
    }

    public static String formatXML(Document document, String charset) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw, format);
        try {
            xw.write(document);
            xw.flush();
            xw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public static Document getDocumentByFilePath(String path) throws Exception {
        SAXReader saxReader = new SAXReader();
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception(path + " is not exist");
        }
        Document doc = saxReader.read(file);
        return doc;
    }

    public static Document createNewDocument() {
        return DocumentHelper.createDocument();
    }

    public String getFirstValueOfXPath(Document doc, String xpath) {
        List list = doc.selectNodes(xpath);
        if (list.size() > 0) {
            Element element = (Element) list.get(0);
            return element.getText();
        }
        return null;
    }

    public static Document getHeaderDocument(Document doc, String userName, String password, String[] headerGroupNames) {
        if (userName != null) {
            Element e = (Element) doc.selectNodes("//sotestapenv:Header").get(0);
            e = e.addElement("AuthenticationToken");
            Element e1 = e.addElement("Username");
            e1.setText(userName);
            if (password != null) {
                e1 = e.addElement("Password");
                e1.setText(password);
            }
            if (headerGroupNames != null && headerGroupNames.length > 0) {
                for (int i = 0; i < headerGroupNames.length; i++) {
                    String groupName = headerGroupNames[i];
                    e1 = e.addElement("Group");
                    e1.setText(groupName);
                }
            }
        }
        return doc;
    }

    public static Document getHeaderDocument(Document doc, String userName, String password) {
        return getHeaderDocument(doc, userName, password, null);
    }

}
