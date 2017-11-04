package com.enjoyf.platform.util.htmlparse.test;

import com.enjoyf.platform.util.htmlparse.Spider;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-7
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class MxmMain {
     public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println("start=====mxm=========" + now);
        Spider spider = new Spider("http://mxm.m.joyme.com", "e:/mobile/mxm");
        spider.downLoadAllLink();
        System.out.println("end=======mxm=======" + (System.currentTimeMillis() - now));
    }
}
