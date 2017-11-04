package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class VoteConstants {
    public static final String SERVICE_SECTION = "voteservice";
    public static final String SERVICE_PREFIX = "voteserver";
    public static final String SERVICE_TYPE = "voteserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }

}
