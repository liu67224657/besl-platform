package com.enjoyf.webapps.tools.webpage.tags;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-6
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class BitwiseTag {
    /**
     * 按位与计算
     * @param x
     * @param y
     * @return
     */
    public static int and(int x, int y){
        return x&y;
    }

    /**
     * 按位或计算
     * @param x
     * @param y
     * @return
     */
    public static int or(int x, int y){
        return  x|y;
    }
}
