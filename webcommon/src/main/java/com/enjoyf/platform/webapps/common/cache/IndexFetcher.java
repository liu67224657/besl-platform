package com.enjoyf.platform.webapps.common.cache;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexFetcher {
    private static IndexFetcher instance = null;

    public static IndexFetcher get() {
        if (instance == null) {
            synchronized (IndexFetcher.class) {
                if (instance == null) {
                    instance = new IndexFetcher();
                }
            }
        }
        return instance;
    }

    private IndexFetcher() {
    }


    public String fetcherIndex(String indexUrl) {
        String host = getHost(indexUrl);
        indexUrl = indexUrl + "/?date=" + new Date().getTime();
        return fetcherIndex(indexUrl, host);
    }


    private String fetcherIndex(String indexUrl, String host) {

        try {
//            if (!FileUtil.isFileOrDirExist(IndexConstant.getIndexBaseDir())) {
//                FileUtil.createDirectory(IndexConstant.getIndexBaseDir());
//            }
            String indexString = getHtmlStringByUrl(indexUrl);

            if (indexString.length() < 1024 * 10) {
                return null;
            }

            Document doc = Jsoup.parse(indexString);

            //remove sy.joyme.com robots tag
            removeRobots(doc);
            repleaceHref(doc, indexUrl, host);
            repleaceImg(doc, indexUrl, host);
            repleaceCss(doc, indexUrl, host);
            repleaceScript(doc, indexUrl, host);

            return doc.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void removeRobots(Document doc) {
        Elements elements = doc.select("meta[name=robots]");
        if (elements != null && elements.size() > 0) {
            elements.remove();
        }
    }

    private void repleaceScript(Document doc, String indexUrl, String host) {
        Elements elements = doc.getElementsByTag("script");

        for (Element javaScript : elements) {
            String src = javaScript.attr("src");
            if (StringUtil.isEmpty(src) || !src.contains(".js")) {
                continue;
            }

            javaScript.attr("src", getUrl(indexUrl, src, host));
        }
    }

    private void repleaceCss(Document doc, String indexUrl, String host) {
        Elements elements = doc.getElementsByTag("link");

        for (Element link : elements) {
            String rel = link.attr("rel");
            if (StringUtil.isEmpty(rel) || !rel.equalsIgnoreCase("stylesheet")) {
                link.remove();
                continue;
            }

            String src = link.attr("href");
            if (StringUtil.isEmpty(src)) {
                continue;
            }
            link.attr("href", getUrl(indexUrl, src, host));
        }
    }

    private void repleaceImg(Document doc, String indexUrl, String host) {
        Elements elements = doc.getElementsByTag("img");
        for (Element img : elements) {
            String src = img.attr("src").trim();
            if (StringUtil.isEmpty(src)) {
                continue;
            }
            img.attr("src", getUrl(indexUrl, src, host));

            String dataUrl = img.attr("data-url").trim();
            if (StringUtil.isEmpty(dataUrl)) {
                continue;
            }
            img.attr("data-url", getUrl(indexUrl, dataUrl, host));

        }
    }

    private void repleaceHref(Document doc, String pageUrl, String host) {
        Elements elements = doc.getElementsByTag("a");
        for (Element link : elements) {
            String href = link.attr("href").trim();
            if (StringUtil.isEmpty(href) || href.startsWith("#") || href.startsWith("javascript")) {
                continue;
            }
            link.attr("href", getUrl(pageUrl, href, host));
        }
    }

    private String getHtmlStringByUrl(String indexUrl) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            url = new URL(indexUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            String encoding = connection.getContentEncoding();
            if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
                in = new GZIPInputStream(connection.getInputStream());
            } else {
                in = new BufferedInputStream(connection.getInputStream());
            }
            return readByInputStream(in, "utf-8");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            StreamUtil.closeInputStream(in);
        }
    }

    private String readByInputStream(InputStream in, String encoding) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, encoding);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } finally {
            if (isr != null) {
                isr.close();
                isr = null;
            }
            if (br != null) {
                br.close();
                br = null;
            }
        }
    }

    private File writeInFile(InputStream in, File file) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            int t;
            while ((t = in.read()) != -1) {
                fileOutputStream.write(t);
            }
        } finally {
            StreamUtil.closeOutputStream(fileOutputStream);
        }
        return file;
    }

    private static String getUrl(String pageUrl, String path, String host) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }

        String pagePath = pageUrl.replace(host, "");

        String[] pagePathArray = pagePath.split("/");
        String[] pathArray = path.split("/");


        if (pathArray[0].equals(".")) {
            String returnPath = host;
            for (int i = 1; i < pagePathArray.length - 1; i++) {
                returnPath += "/" + pagePathArray[i];
            }

            for (int i = 1; i < pathArray.length; i++) {
                returnPath += "/" + pathArray[i];
            }
            return returnPath;
        }

        if (pathArray[0].equals("..")) {
            String returnPath = host;
            for (int i = 1; i < pagePathArray.length - 2; i++) {
                returnPath += "/" + pagePathArray[i];
            }

            for (int i = 1; i < pathArray.length; i++) {
                returnPath += "/" + pathArray[i];
            }
            return returnPath;
        }

        return host + path;
    }

    private static String getHost(String url) {
        Pattern hostPattern = Pattern.compile("(http://[^/]+)/?");

        List<Map<String, String>> hostMap = RegexUtil.fetch(url, hostPattern, -1);
        if (CollectionUtil.isEmpty(hostMap)) {
            return null;
        }

        return hostMap.get(0).get("1");
    }

}
