package com.enjoyf.platform.util.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public final class HttpURLUtils {
    private static final Logger logger = Logger.getLogger("Platform");
    private static final String CONTENT_TYPE_BMP = "image/bmp";
    private static final String CONTENT_TYPE_PNG = "image/png";
    private static final String CONTENT_TYPE_GIF = "image/gif";
    private static final String CONTENT_TYPE_JPEG = "image/jpeg";
    private static final String CONTENT_TYPE_JPG = "image/jpg";

    public static String doPost(String reqUrl, Map<String, String> parameters) throws IOException {
        HttpURLConnection urlConn = null;
        try {
            urlConn = sendPost(reqUrl, parameters);
            String responseContent = getContent(urlConn);
            return responseContent.trim();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }
    }

    public static String doUploadFile(String reqUrl, Map<String, String> parameters, String fileParamName, String filename, String contentType,
                                      byte[] data) {
        HttpURLConnection urlConn = null;
        try {
            urlConn = sendFormdata(reqUrl, parameters, fileParamName, filename, contentType, data);
            HttpByteData httpByteData = getBytes(urlConn);
            String responseContent = null;
            if (httpByteData != null) {
                responseContent = new String(httpByteData.getData());
            }
            return responseContent.trim();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }

    public static HttpURLConnection sendFormdata(String reqUrl, Map<String, String> parameters, String fileParamName, String filename,
                                                 String contentType, byte[] data) throws IOException {
        HttpURLConnection urlConn = null;
        OutputStream os = null;
        try {
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
            urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
            urlConn.setDoOutput(true);

            urlConn.setRequestProperty("connection", "keep-alive");

            String boundary = "-----------------------------114975832116442893661388290519"; // 分隔符
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            boundary = "--" + boundary;
            StringBuffer params = new StringBuffer();
            if (parameters != null) {
                for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext(); ) {
                    String name = iter.next();
                    String value = parameters.get(name);
                    params.append(boundary + "\r\n");
                    params.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
                    // params.append(URLEncoder.encode(value, "UTF-8"));
                    if (name.equals("content")) {
                        value = URLDecoder.decode(value, "utf-8");
                    }
                    params.append(value);
                    params.append("\r\n");
                }
            }

            StringBuilder sb = new StringBuilder();
            // sb.append("\r\n");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"" + filename + "\"\r\n");
            sb.append("Content-Type: " + contentType + "\r\n\r\n");
            byte[] fileDiv = sb.toString().getBytes();
            byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
            byte[] ps = params.toString().getBytes();

            os = urlConn.getOutputStream();
            os.write(ps);
            os.write(fileDiv);
            os.write(data);
            os.write(endData);

            os.flush();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return urlConn;
    }

    public static String getContent(HttpURLConnection urlConn) throws IOException {
        InputStream in = null;
        BufferedReader rd = null;
        try {
            in = urlConn.getInputStream();
            rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null) {
                tempStr.append(tempLine).append("\r\n");
                tempLine = rd.readLine();
            }
            return tempStr.toString();
        } finally {
            try {
                if (rd != null)
                    rd.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }
    }

    public static HttpByteData getBytes(String reqUrl) throws IOException {
        HttpURLConnection conn = sendGet(reqUrl);

        return getBytes(conn);
    }

    private static HttpByteData getBytes(HttpURLConnection urlConn) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream os = null;
        try {
            in = urlConn.getInputStream();
            os = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int i = 0; (i = in.read(buf)) > 0; )
                os.write(buf, 0, i);

            byte[] bytes = os.toByteArray();
            HttpByteData byteData = new HttpByteData(bytes, urlConn.getHeaderField("Content-Type"));
            return byteData;
        } finally {
            try {
                if (os != null)
                    os.close();
                if (in != null)
                    in.close();
                if (urlConn != null) {
                    urlConn.disconnect();
                    urlConn = null;
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }
    }

    private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters) {
        HttpURLConnection urlConn = null;
        try {
            String params = generatorParamString(parameters);
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            // urlConn
            // .setRequestProperty(
            // "User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
            urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
            urlConn.setDoOutput(true);
            byte[] b = params.getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return urlConn;
    }

    private static HttpURLConnection sendGet(String reqUrl) {
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            // urlConn
            // .setRequestProperty(
            // "User-Agent",
            // "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
            urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
        } catch (IOException e) {
            logger.error("oauth is error,Message:" + e.getMessage(), e);
        }
        return urlConn;
    }

    /**
     * 将parameters中数据转换成用"&"链接的http请求参数形式
     *
     * @param parameters
     * @return
     */
    public static String generatorParamString(Map<String, String> parameters) {
        StringBuffer params = new StringBuffer();
        if (parameters != null) {
            for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String value = parameters.get(name);
                params.append(name + "=");
                try {
                    params.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (Exception e) {
                    String message = String.format("'%s'='%s'", name, value);
                    throw new RuntimeException(message, e);
                }
                if (iter.hasNext())
                    params.append("&");
            }
        }
        return params.toString();
    }

    /**
     * @param link
     * @param charset
     * @return
     */
    public static String doGet(String link, String charset) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int i = 0; (i = in.read(buf)) > 0; ) {
                out.write(buf, 0, i);
            }
            out.flush();
            String s = new String(out.toByteArray(), charset);
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }

    /**
     * UTF-8编码
     *
     * @param link
     * @return
     */
    public static String doGet(String link) {
        return doGet(link, "UTF-8");
    }

    public static int getIntResponse(String link) {
        String str = doGet(link);
        return Integer.parseInt(str.trim());
    }

    public static String parseContentType(String fileName) {
        String contentType = "image/jpg";
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".jpg"))
            contentType = CONTENT_TYPE_JPG;
        else if (fileName.endsWith(".png"))
            contentType = CONTENT_TYPE_PNG;
        else if (fileName.endsWith(".jpeg"))
            contentType = CONTENT_TYPE_JPEG;
        else if (fileName.endsWith(".gif"))
            contentType = CONTENT_TYPE_GIF;
        else if (fileName.endsWith(".bmp"))
            contentType = CONTENT_TYPE_BMP;
        else
            throw new RuntimeException("不支持的文件类型'" + fileName + "'(或没有文件扩展名)");
        return contentType;
    }

    public static HttpByteData getByte(String picPath, boolean isLocal) throws IOException {
        byte[] picData = null;
        if (isLocal) {
            File file = new File(picPath);
            picData = new byte[1024 * 20];
            InputStream in = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                in = new FileInputStream(file);
                for (int len = 0; (len = in.read(picData)) > 0; ) {
                    os.write(picData, 0, len);
                }
                return new HttpByteData(os.toByteArray(), "image/jpeg");
            } finally {
                try {
                    if (os != null)
                        os.close();
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } else {
            return HttpURLUtils.getBytes(picPath);
        }
    }


    public static void main(String[] args) {
        try {
            HttpURLUtils.getByte("http://joymetest.qiniudn.com/qiniu/wiki/images/home/2016/07/20/IMG_0436.JPG", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
