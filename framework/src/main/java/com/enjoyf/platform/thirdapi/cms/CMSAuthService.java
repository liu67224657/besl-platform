package com.enjoyf.platform.thirdapi.cms;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import org.apache.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-12 下午1:48
 * Description:
 */
public class CMSAuthService {
    private static CMSAuthService instance;


    public static CMSAuthService get() {
        if (instance == null) {
            synchronized (CMSAuthService.class) {
                if (instance == null) {
                    instance = new CMSAuthService();
                }
            }
        }
        return instance;
    }

    private CMSAuthService() {
    }

    public String login(long userid){
        HttpClientManager manager = new HttpClientManager();

        HttpParameter[] params = new HttpParameter[]{
                new HttpParameter("userid", userid),
                new HttpParameter("type", "login"),
        };

        HttpResult httpResult = manager.post(WebappConfig.get().getCmsApiURL(), params, null);

        return httpResult.getResult();
    }

    public String logout(long userid) {
        HttpClientManager manager = new HttpClientManager();

        HttpParameter[] params = new HttpParameter[]{
                new HttpParameter("userid", userid),
                new HttpParameter("type", "logout"),
        };

        HttpResult httpResult = manager.post(WebappConfig.get().getCmsApiURL(), params, null);

        return httpResult.getResult();
    }

}
