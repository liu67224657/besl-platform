/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.resdomain;

import com.enjoyf.platform.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-12-3 下午8:00
 * Description:
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
//            System.out.println(ResourceServerMonitor.get().getDownResourceDomainJson());
//            System.out.println(ResourceServerMonitor.get().getRandomUploadDomainPrefix());

            Utility.sleep(1000);
        }
        System.out.println(">>>" + (System.currentTimeMillis() - t));
    }
}
