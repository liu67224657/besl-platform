package com.enjoyf.platform.util.http;

import org.apache.http.*;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-30
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtilGetURL {
	public static String httpGet(String url) throws IOException {
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient mClient = new DefaultHttpClient();
		mClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {

				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					for (HeaderElement element : ceheader.getElements()) {
						if (element.getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GzipDecompressingEntity(response.getEntity()));
							return;
						}
					}
				}
			}
		});
		String result = "";
		HttpResponse response = mClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

}
