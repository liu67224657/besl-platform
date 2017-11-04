package com.enjoyf.platform.service.log;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-9-26
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class LogServiceImpl implements LogService {
    private Logger pvLogger = Logger.getLogger("pageview");

    private Logger mobileAccessLogger = Logger.getLogger("mobileaccess");

    private Logger pcLogger = Logger.getLogger("pclog");

    /**
     * 上报PV日志
     *
     * @param pvLog
     */
    public void reportLogInfo(PageViewLog pvLog) {
        pvLogger.info(pvLog);
    }

    @Override
    public void reportMobileAccessInfo(MobileAccessLog mobileAccessLog) {
        mobileAccessLogger.info(mobileAccessLog);
    }

    @Override
    public void reportPCAccessInfo(PCAccessInfo pcAccessInfo) {
        pcLogger.info(pcAccessInfo);
    }
}
