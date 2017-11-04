package com.enjoyf.platform.util.http;

import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-2-11 下午6:04
 * Description:
 */
public class URLUtils {

    private static final String DEFAULT_GAME_ICON = "/static/theme/default/images/gamecollection/default-game-icon.jpg";

    public static String getHtmlStringByURl(String indexUrl) throws IOException {
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
            return StreamUtil.readByInputStream(in, "utf-8");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            StreamUtil.closeInputStream(in);
        }
    }


//    public static String HTTP_QINIU = "http://joymepic.qiniudn.com";
//    public static String HTTPS_QINIU = "https://joymepic.qiniudn.com";
//
//    public static String HTTP_JOYMEPIC = "http://joymepic.joyme.com";
//    public static String HTTPS_JOYMEPIC = "https://joymepic.joyme.com";


//    public static String getQiniuUrl(String path) {
//        if (StringUtil.isEmpty(path)) {
//            return path;
//        }
//        if (path.startsWith("http://") || path.startsWith("https://")) {
//            if (path.startsWith(HTTP_JOYMEPIC)) {
//                path = path.replaceAll(HTTP_JOYMEPIC, HTTP_QINIU);
//            } else if (path.startsWith(HTTPS_JOYMEPIC)) {
//                path = path.replaceAll(HTTPS_JOYMEPIC, HTTPS_QINIU);
//            }
//            return path;
//        }
//
//        return "http://joymepic.qiniudn.com" + path;
//    }

    public static String getJoymeDnUrl(String path) {
        return FileHandlerFactory.getUrlByPath(path);
    }

    public static String getDefaultGameIcon() {
        return WebappConfig.get().getUrlLib() + DEFAULT_GAME_ICON;
    }
}
