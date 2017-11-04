package com.enjoyf.platform.util.htmlparse.test;

import com.enjoyf.platform.util.htmlparse.Spider;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-7
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class WzzjMain {
     public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println("start=====wzzj=========" + now);
        Spider spider = new Spider("http://wzzj.m.joyme.com", "e:/mobile/wzzj");
        spider.downLoadAllLink();
        System.out.println("end=======wzzj=======" + (System.currentTimeMillis() - now));

    }
}
