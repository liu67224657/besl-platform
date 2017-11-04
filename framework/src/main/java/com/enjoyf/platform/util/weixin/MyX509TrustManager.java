package com.enjoyf.platform.util.weixin;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 上午11:57
 * To change this template use File | Settings | File Templates.
 * 证书信任管理器（用户https请求）
 */
public class MyX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }
}
