package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.serv.thrserver.MainUtil;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-10
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        MainUtil.doMain(new BillingInitializer());
    }
}
