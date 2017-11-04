package com.enjoyf.platform.util.htmlparse.test;

import com.enjoyf.platform.util.htmlparse.Spider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-7
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class MKHXMain {
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println("start=====mkhx=========" + now);
        Spider spider = new Spider("http://mkhx.m.joyme.com", "e:/mobile/mkhx");
        spider.downLoadAllLink();
        System.out.println("end=======mkhx=======" + (System.currentTimeMillis() - now));

        now = System.currentTimeMillis();
        System.out.println("start=====mxm=========" + now);
        spider = new Spider("http://mxm.m.joyme.com", "e:/mobile/mxm");
        spider.downLoadAllLink();
        System.out.println("end=======mxm=======" + (System.currentTimeMillis() - now));

        now = System.currentTimeMillis();
        System.out.println("start=====wzzj=========" + now);
        spider = new Spider("http://wzzj.m.joyme.com", "e:/mobile/wzzj");
        spider.downLoadAllLink();
        System.out.println("end=======wzzj=======" + (System.currentTimeMillis() - now));

        now = System.currentTimeMillis();
        System.out.println("start=====dzm=========" + now);
        spider = new Spider("http://dzm.m.joyme.com", "e:/mobile/dzm");
        spider.downLoadAllLink();
        System.out.println("end=======dzm=======" + (System.currentTimeMillis() - now));
    }
}
