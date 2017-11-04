package com.enjoyf.platform.webapps.common.html.tag;

import com.enjoyf.platform.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class IpTag {
    private static Logger logger = LoggerFactory.getLogger(IpTag.class);

    public static String convertLongToString(long address) {
        if(logger.isDebugEnabled()){
            logger.debug("converLongToString param address : " + address);
        }
        return IpUtil.cvtToString((int)address);
    }
}
