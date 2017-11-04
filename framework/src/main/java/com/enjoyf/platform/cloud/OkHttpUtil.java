package com.enjoyf.platform.cloud;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.google.gson.JsonObject;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhimingli on 2017/6/16.
 */
public class OkHttpUtil {

    private static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");


    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static Response doGet(String url) {
        return doGet(url,null,null);
    }

    public static Response doGet(String url, String Authorization, Map<String, String> headMap) {

        Request.Builder builder = new Request.Builder();
        if (!StringUtil.isEmpty(Authorization)) {
            builder.addHeader("Authorization", buildAuthorization(Authorization));
        }
        if (!CollectionUtil.isEmpty(headMap)) {
            builder.head().headers(Headers.of(headMap));
        }

        builder.url(url);
        final Request request = builder.build();
        return execute(request);
    }

    public static Response doPost(String url, JsonObject json) {
        return doPost(url, json, null, null);
    }

    /**
     * POST提交Json数据
     * JsonObject json = new JsonObject();
     * json.addProperty("k1", "v1");
     *
     * @param url
     * @param json
     * @return
     */
    public static Response doPost(String url, JsonObject json, String Authorization, Map<String, String> headMap) {
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json.toString());

        Request.Builder builder = new Request.Builder();
        if (!StringUtil.isEmpty(Authorization)) {
            builder.addHeader("Authorization",  buildAuthorization(Authorization));
        }
        if (!CollectionUtil.isEmpty(headMap)) {
            builder.head().headers(Headers.of(headMap));
        }

        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        return execute(request);
    }

    public static Response doPost(String url, Map<String, String> params) {
        return doPost(url, params, null, null);
    }

    public static Response doPost(String url, Map<String, String> params, String Authorization, Map<String, String> headMap) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (!CollectionUtil.isEmpty(headMap)) {
            requestBuilder.head().headers(Headers.of(headMap));
        }
        if (!StringUtil.isEmpty(Authorization)) {
            requestBuilder.addHeader("Authorization", Authorization);
        }

        Request request = requestBuilder
                .url(url)
                .post(builder.build())
                .build();
        return execute(request);
    }

    public static Response doPut(String url, JsonObject json) {
        return doPut(url, json, null, null);
    }


    public static Response doPut(String url, JsonObject json, String Authorization, Map<String, String> headMap) {
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json.toString());

        Request.Builder builder = new Request.Builder();
        if (!StringUtil.isEmpty(Authorization)) {
            builder.addHeader("Authorization",  buildAuthorization(Authorization));
        }
        if (!CollectionUtil.isEmpty(headMap)) {
            builder.head().headers(Headers.of(headMap));
        }

        Request request = builder
                .url(url)
                .put(requestBody)
                .build();
        return execute(request);
    }

    public static Response doPut(String url, Map<String, String> params, String Authorization, Map<String, String> headMap) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (!CollectionUtil.isEmpty(headMap)) {
            requestBuilder.head().headers(Headers.of(headMap));
        }
        if (!StringUtil.isEmpty(Authorization)) {
            requestBuilder.addHeader("Authorization", Authorization);
        }

        Request request = requestBuilder
                .url(url)
                .put(builder.build())
                .build();
        return execute(request);
    }

    public static Response doDelete(String url) {
        return doDelete(url,null,null);
    }


    public static Response doDelete(String url, String Authorization, Map<String, String> headMap) {
        Request.Builder builder = new Request.Builder();
        if (!StringUtil.isEmpty(Authorization)) {
            builder.addHeader("Authorization",  buildAuthorization(Authorization));
        }
        if (!CollectionUtil.isEmpty(headMap)) {
            builder.head().headers(Headers.of(headMap));
        }

        Request request = builder
                .url(url)
                .delete()
                .build();
        return execute(request);
    }


    public static Response execute(Request request) {
        Response response = null;
        try {
            OkHttpClient client = new OkHttpClient();
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String buildAuthorization(String Authorization){
        return "Bearer "+Authorization;
    }

}
