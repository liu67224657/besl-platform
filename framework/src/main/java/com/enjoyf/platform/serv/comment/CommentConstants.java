/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.comment;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:ericliu@fivewh.com>Eric Liu</a>
 */
public class CommentConstants {
    public static final String SERVICE_SECTION = "commentservice";
    public static final String SERVICE_PREFIX = "commentserver";
    public static final String SERVICE_TYPE = "commentserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    static {
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
