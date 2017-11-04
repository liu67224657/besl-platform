package com.enjoyf.platform.webapps.common.util;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class HttpUpload {

    private static final int OK = 200;                        // OK: Success!
    private static final int NOT_MODIFIED = 304;            // Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;                // Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;            // Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;                // Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;                // Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;        // Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Weibo team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are

    HttpClient client = null;
    private MultiThreadedHttpConnectionManager connectionManager;

    public HttpUpload() {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setDefaultMaxConnectionsPerHost(15);
        params.setConnectionTimeout(300 * 1000);
        params.setSoTimeout(300 * 1000);

        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        client = new org.apache.commons.httpclient.HttpClient(clientParams, connectionManager);
//        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
//        Protocol.registerProtocol("https", myhttps);
    }


    public String post(String url, PostParameter[] params, String token) {
        PostMethod postMethod = new PostMethod(url);
        for (int i = 0; i < params.length; i++) {
            postMethod.addParameter(params[i].getName(), params[i].getValue());
        }
        HttpMethodParams param = postMethod.getParams();
        param.setContentCharset("UTF-8");

        return httpRequest(postMethod, token);
    }

    public String postMultipart(String url, PostParameter[] params, String token) throws FileNotFoundException {
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

        return httpRequest(postMethod, token);
    }


    private String httpRequest(HttpMethod method, String token) {
        String result = null;

        InetAddress ipaddr;
        int responseCode = -1;
        try {
            ipaddr = InetAddress.getLocalHost();
            List<Header> headers = new ArrayList<Header>();

            headers.add(new Header(CookieUtil.ACCESS_TOKEN, token));
            headers.add(new Header("API-RemoteIP", ipaddr.getHostAddress()));
            client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            client.executeMethod(method);
            Header[] resHeader = method.getResponseHeaders();
            responseCode = method.getStatusCode();
            result = method.getResponseBodyAsString();
            if (responseCode != OK) {
                GAlerter.lab("upload image responseCode :" + responseCode);
                List<String> errorList = new ArrayList<String>();
                errorList.add(result);
                result = JsonBinder.buildNormalBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, String.valueOf(responseCode), errorList));
            }
            return result;
        } catch (IOException ioe) {
            GAlerter.lab("upload image link occured IOException. e:", ioe);
            List<String> errorList = new ArrayList<String>();
            errorList.add(ioe.getMessage());
            result = JsonBinder.buildNormalBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E, String.valueOf(responseCode), errorList));
            return result;
        } finally {
            method.releaseConnection();
        }
    }

}
