package com.enjoyf.platform.util.http;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class HttpClientManager {

    private Logger logger = LoggerFactory.getLogger(HttpClientManager.class);

    public static final int OK = 200;                        // OK: Success!
    public static final int NOT_MODIFIED = 304;            // Not Modified: There was no new data to return.
    public static final int BAD_REQUEST = 400;                // Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    public static final int NOT_AUTHORIZED = 401;            // Not Authorized: Authentication credentials were missing or incorrect.
    public static final int FORBIDDEN = 403;                // Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    public static final int NOT_FOUND = 404;                // Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    public static final int NOT_ACCEPTABLE = 406;        // Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    public static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Weibo team can investigate.
    public static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or being upgraded.
    public static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are
    public static final int SERVICE_TIMEOUT = 504;// Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are


    // 一次性从数据流读取的最大byte数
    private int max_bytes = 4096;

    HttpClient client = null;
    private MultiThreadedHttpConnectionManager connectionManager;

    public HttpClientManager() {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setDefaultMaxConnectionsPerHost(15);
        params.setConnectionTimeout(300 * 1000);
        params.setSoTimeout(300 * 1000);

        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        client = new org.apache.commons.httpclient.HttpClient(clientParams, connectionManager);
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
    }


    public HttpResult post(String url, HttpParameter[] params, HttpParameter[] headerParams) {
        PostMethod postMethod = new PostMethod(url);
        if (params != null) {
            for (HttpParameter param : params) {
                if (param.getValue() != null) {
                    postMethod.addParameter(param.getName(), param.getValue());
                }
            }
        }
        HttpMethodParams param = postMethod.getParams();
        param.setContentCharset("UTF-8");

        return httpRequest(postMethod, headerParams);
    }

    public HttpResult get(String url, HttpParameter[] params) {

        if (null != params && params.length > 0) {
            String encodedParams = HttpClientManager.encodeParameters(params);
            if (-1 == url.indexOf("?")) {
                url += "?" + encodedParams;
            } else {
                url += "&" + encodedParams;
            }
        }
        GetMethod getmethod = new GetMethod(url);
        return httpRequest(getmethod, null);
    }

    public HttpResult get(String url, HttpParameter[] params, String coding) {

        if (null != params && params.length > 0) {
            String encodedParams = HttpClientManager.encodeParameters(params, coding);
            if (-1 == url.indexOf("?")) {
                url += "?" + encodedParams;
            } else {
                url += "&" + encodedParams;
            }
        }
        GetMethod getmethod = new GetMethod(url);
        return httpRequest(getmethod, null);
    }

    public HttpResult multPartURL(String url, HttpParameter[] params)
            throws Exception {
        PostMethod methodpost = new PostMethod(url);
        Part[] parts = new Part[params.length];
        int i = 0;
        for (HttpParameter param : params) {
            if (param.getValue() != null) {
                parts[i++] = new StringPart(param.getName(), param.getValue(), "UTF-8");
            } else if (param.getFile() != null) {
                FilePart filePart = new FilePart(param.getName(), param.getFile().getName(),
                        param.getFile(), param.getContentType(),
                        "UTF-8");
                filePart.setTransferEncoding("binary");
                parts[i++] = filePart;
            } else if (param.getByteData() != null) {
                ByteArrayPart byteArrayPart = new ByteArrayPart(param.getByteData().getData(), param.getName(), param.getByteData().getContentType());
                parts[i++] = byteArrayPart;
            }
        }
        methodpost.setRequestEntity(new MultipartRequestEntity(parts, methodpost.getParams()));
        return httpRequest(methodpost, null);
    }


    public HttpResult postMultipart(String url, HttpParameter[] params) throws FileNotFoundException {
        MultipartPostMethod postMethod = new MultipartPostMethod(url);
        for (int i = 0; i < params.length; i++) {
            if (params[i].getFile() != null) {
                FilePart cbFile = new FilePart(params[i].getName(), params[i].getFile());
                cbFile.setContentType(params[i].getContentType());
                postMethod.addPart(cbFile);
            } else {
                postMethod.addParameter(params[i].getName(), params[i].getValue());
            }
        }
        HttpMethodParams param = postMethod.getParams();
        param.setContentCharset("UTF-8");

        return httpRequest(postMethod, params);
    }

    public HttpURLConnection sendFormdata(String reqUrl, Map<String, String> parameters, String fileParamName, String filename,
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

    private HttpResult httpRequest(HttpMethod method, HttpParameter[] headerParams) {
        HttpResult result = new HttpResult();

        int responseCode = -1;
        try {
            if (headerParams != null && headerParams.length > 0) {
                List<Header> headers = new ArrayList<Header>();
                for (HttpParameter headerParam : headerParams) {
                    headers.add(new Header(headerParam.getName(), headerParam.getValue()));
                }
                client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
            }

            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            client.executeMethod(method);
            Header[] resHeader = method.getResponseHeaders();
            Map<String, String> headerMap = new HashMap<String, String>();
            for (Header header : resHeader) {
                headerMap.put(header.getName(), header.getValue());
            }

            responseCode = method.getStatusCode();
            String resultStr = readInputStream(method.getResponseBodyAsStream(), headerMap);

            result.setReponseCode(responseCode);
            result.setResult(resultStr);
            return result;
        } catch (IOException ioe) {
            logger.error(" http occured IOException.", ioe);
            if (method != null) {
                responseCode = method.getStatusCode();
            } else {
                responseCode = SERVICE_TIMEOUT;
            }
            result.setReponseCode(responseCode);
            result.setResult("occured ioexceotion");
            return result;
        } finally {
            method.releaseConnection();
        }
    }

    /*
     * 对parameters进行encode处理
	 */
    private static String encodeParameters(HttpParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(postParams[j].getValue(),
                                "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }

    private static String encodeParameters(HttpParameter[] postParams, String coding) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams[j].getName(), coding))
                        .append("=")
                        .append(URLEncoder.encode(postParams[j].getValue(), coding));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }

    public HttpByteData getByte(String picPath, boolean isLocal) throws IOException {
        return HttpURLUtils.getByte(picPath, isLocal);
    }

    /**
     * 将输入流按照特定的编码转换成字符串
     *
     * @param is 输入流
     * @return 字符串
     * @throws java.io.IOException
     */
    private String readInputStream(InputStream is, Map<String, String> headerMap) throws IOException {
        InputStream in = null;
        if (!CollectionUtil.isEmpty(headerMap) &&
                headerMap.containsKey("Content-Encoding")
                && headerMap.get("Content-Encoding").contains("gzip")) {
            in = new GZIPInputStream(is);
        } else {
            in = is;
        }

        byte[] b = new byte[max_bytes];
        StringBuilder builder = new StringBuilder();
        int bytesRead = 0;
        while (true) {
            bytesRead = in.read(b, 0, max_bytes);
            if (bytesRead == -1) {
                return builder.toString();
            }
            builder.append(new String(b, 0, bytesRead, "UTF-8"));
        }
    }
}
