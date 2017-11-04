package com.enjoyf.platform.util.htmlparse;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-8
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class Spider {
    private static final String SPECL_PATTERN = "[%a-zA-Z0-9\\.]+:[%a-zA-Z0-9\\.]+";
    private static final String SHORTURL_PATTERN = "/wiki/([%a-zA-Z0-9\\._\\(\\)\\-!]+)";
    private static final String COMMON_PATTERN = "/index.php\\?title=([%a-zA-Z0-9\\._\\(\\)\\-!]+)";

    //    private static final String FILE_PATH = "e:/mobile/";
    public static String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");

    private Pattern speclPagePattern;
    private Pattern shortUrlPattern;
    private Pattern commonPattern;

    private String host;
    private String filePath;

    /**
     * genterator to  filePath all web link and css
     *
     * @param host     ex:http://my.m.joyme.com
     * @param filePath ex: FILE_PATH/wikiCode
     */
    public Spider(String host, String filePath) {
        this.host = host;
        speclPagePattern = Pattern.compile(host + SPECL_PATTERN);
        shortUrlPattern = Pattern.compile(host + SHORTURL_PATTERN);
        commonPattern = Pattern.compile(host + COMMON_PATTERN);
        this.filePath = filePath;
    }

    public Set<String> downLoadAllLink() {
        Set<String> hrefSet = new HashSet<String>();
        try {
            Document doc = this.getHostDocument();
            hrefSet = this.downLoadByLinkHref(doc, new HashSet<String>());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return hrefSet;
    }

    public String downLoadByLink(String link) {
        if (StringUtil.isEmpty(link)) {
            return null;
        } else if (link.startsWith("#")) {
            return null;
        }

        String afterFilterHref = filterLink(link, host);

        if (StringUtil.isEmpty(afterFilterHref)) {
            return null;
        }

        Document hrefDoc = null;
        try {
            hrefDoc = getDocumentByUrl(afterFilterHref);

            String pageName = URLDecoder.decode(getPageName(afterFilterHref), "UTF-8");
            downloadPage(pageName, hrefDoc);
            return afterFilterHref;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document getHostDocument() throws IOException {
        return Jsoup.connect(host).timeout(60000).post();
    }

    private Document getDocumentByUrl(String url) throws IOException {
        return Jsoup.connect(url).timeout(60000).post();
    }

    //递归调用并且下载页面
    private Set<String> downLoadByLinkHref(Document document, Set<String> hrefSet) {

        Elements elementList = document.getElementsByTag("a");

        for (Element element : elementList) {
            String hrefValue = element.attr("href");

            if (StringUtil.isEmpty(hrefValue)) {
                continue;
            } else if (hrefValue.startsWith("#")) {
                continue;
            }

            String afterFilterHref = filterLink(hrefValue, host);

            if (StringUtil.isEmpty(afterFilterHref)) {
                continue;
            } else if (hrefSet.contains(afterFilterHref)) {
                continue;
            }

            hrefSet.add(afterFilterHref);
            Document hrefDoc = null;
            try {
                hrefDoc = getDocumentByUrl(afterFilterHref);

                String pageName = URLDecoder.decode(getPageName(afterFilterHref), "UTF-8");
                downloadPage(pageName, hrefDoc);
                hrefSet = downLoadByLinkHref(hrefDoc, hrefSet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hrefSet;
    }


    public static void main(String[] args) {

        try {
            System.out.println(URLEncoder.encode(")", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long now = System.currentTimeMillis();
        System.out.println("start==============" + now);
        Spider spider = new Spider("http://mt.m.joyme.com", "e:/mobile/mt2");
//        spider.downLoadAllLink();
        spider.downLoadByLink("http://mt.m.joyme.com/wiki/%E6%89%8B%E6%9C%BA%E7%89%88%E5%AF%BC%E8%88%AA");
        System.out.println("end==============" + (System.currentTimeMillis() - now));
    }

    /////////下载///////////
    private void downloadPage(String htmlName, Document document) throws IOException {

        String htmlfilePath = filePath;
        if (!FileUtil.isFileOrDirExist(htmlfilePath)) {
            FileUtil.createDirectory(htmlfilePath);
        }

        if (FileUtil.isFileOrDirExist(htmlfilePath + SYSTEM_FILE_SEPARATOR + htmlName + ".html")) {
            return;
        }


        String jsFilePath = filePath + SYSTEM_FILE_SEPARATOR + "js";
        if (!FileUtil.isFileOrDirExist(jsFilePath)) {
            FileUtil.createDirectory(jsFilePath);
        }

        String styleFilePath = filePath + SYSTEM_FILE_SEPARATOR + "style";
        if (!FileUtil.isFileOrDirExist(styleFilePath)) {
            FileUtil.createDirectory(styleFilePath);
        }


        Elements imgElements = document.getElementsByTag("img");
        for (Element element : imgElements) {
            String src = downloadImage(element, filePath);
            if (StringUtil.isEmpty(src)) {
                continue;
            }
            element.attr("src", "." + src);
        }

        Elements linkElements = document.getElementsByTag("link");
        for (Element link : linkElements) {
            String rel = link.attr("rel");
            if (StringUtil.isEmpty(rel) || !rel.equals("stylesheet")) {
                link.remove();
                continue;
            }
            downloadLinkTag(link, styleFilePath);
            link.attr("href", "./style/style.css");
        }

        Elements scriptsElements = document.getElementsByTag("script");
        for (Element script : scriptsElements) {

            String src = downloadScript(script, filePath);
            if (StringUtil.isEmpty(src)) {
                script.remove();
            }
            script.attr("src", "./" + src);
        }

        Elements iframeElements = document.getElementsByTag("iframe");
        iframeElements.remove();

        FileOutputStream fileOutputStream = new FileOutputStream(new File(htmlfilePath + SYSTEM_FILE_SEPARATOR + htmlName + ".html"));
        fileOutputStream.write(document.html().getBytes());
    }

    private String downloadImage(Element imageElement, String filePath) throws MalformedURLException {
        String src = imageElement.attr("src");

        URL url = !src.startsWith("http://") ? new URL(host + src) : new URL(src);
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(url.openStream());

            src = URLDecoder.decode(src, "UTF-8");
            String imgDir = filePath + SYSTEM_FILE_SEPARATOR + src.substring(0, src.lastIndexOf("/"));


            String fileName = src.substring(src.lastIndexOf("/"), src.length());
            if (FileUtil.isFileOrDirExist(imgDir + SYSTEM_FILE_SEPARATOR + fileName)) {
                return src;
            }

            if (!FileUtil.isFileOrDirExist(imgDir)) {
                FileUtil.createDirectory(imgDir);
            }

            fileOutputStream = new FileOutputStream(new File(imgDir + SYSTEM_FILE_SEPARATOR + fileName));
            int t;
            while ((t = bufferedInputStream.read()) != -1) {
                fileOutputStream.write(t);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } finally {
            StreamUtil.closeInputStream(bufferedInputStream);
            StreamUtil.closeOutputStream(fileOutputStream);
        }
        return src;
    }

    private String downloadLinkTag(Element link, String filePath) throws MalformedURLException, FileNotFoundException {
        String rel = link.attr("rel");
        if (StringUtil.isEmpty(rel) || !rel.equals("stylesheet")) {
            link.remove();
        }

        String cssFilePath = filePath + SYSTEM_FILE_SEPARATOR + "style" + ".css";
        if (FileUtil.isFileOrDirExist(cssFilePath)) {
            return cssFilePath;
        }

        if (FileUtil.isFileOrDirExist(filePath + SYSTEM_FILE_SEPARATOR)) {
            FileUtil.createDirectory(filePath);
        }

        String src = link.attr("href");
        URL url = !src.startsWith("http://") ? new URL(host + src) : new URL(src);
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(url.openStream());
            fileOutputStream = new FileOutputStream(new File(cssFilePath));
            int t;
            while ((t = bufferedInputStream.read()) != -1) {
                fileOutputStream.write(t);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } finally {
            StreamUtil.closeInputStream(bufferedInputStream);
            StreamUtil.closeOutputStream(fileOutputStream);
        }

        return cssFilePath;
    }

    private String downloadScript(Element script, String filePath) throws MalformedURLException {
        String src = script.attr("src");

        if (StringUtil.isEmpty(src) || src.contains("load.php") || src.contains("http://")) {
            return null;
        }

        String jsDir = filePath + SYSTEM_FILE_SEPARATOR + src.substring(0, src.lastIndexOf("/"));
        String jsName = src.substring(src.lastIndexOf("/"), src.length());

        if (!FileUtil.isFileOrDirExist(jsDir)) {
            URL url = new URL(host + src);

            FileOutputStream fileOutputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(url.openStream());
                fileOutputStream = new FileOutputStream(new File(jsDir + jsName));
                int t;
                while ((t = bufferedInputStream.read()) != -1) {
                    fileOutputStream.write(t);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            } finally {
                StreamUtil.closeInputStream(bufferedInputStream);
                StreamUtil.closeOutputStream(fileOutputStream);
            }
        }
        return src;
    }

    ////
    private String filterLink(String url, String host) {
        if (speclPagePattern.matcher(url).find()) {
            return null;
        }

        if (url.startsWith("/")) {
            url = host + url;
        }

        Matcher matcher = shortUrlPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        }

        Matcher matcher2 = commonPattern.matcher(url);
        if (matcher2.find()) {
            return matcher2.group(0);
        }
        return null;
    }

    private String getPageName(String url) {
        Matcher matcher = shortUrlPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        Matcher matcher2 = commonPattern.matcher(url);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        return null;
    }
}
