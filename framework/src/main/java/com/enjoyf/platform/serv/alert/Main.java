/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.alert;

import com.enjoyf.platform.serv.thrserver.MainUtil;


/**
 * The main class for the server.
 */
public class Main {
    public static void main(String[] args) {
        MainUtil.doMain(new AlertInitializer());
    }
}
