package com.enjoyf.platform.util.htmlparse.test;

import com.enjoyf.platform.util.htmlparse.Spider;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-7
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class DzmMain {
     public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println("start=====dzm=========" + now);
        Spider spider = new Spider("http://dzm.m.joyme.com", "e:/mobile/dzm");
        spider.downLoadAllLink();
        System.out.println("end=======dzm=======" + (System.currentTimeMillis() - now));

    }
}
