package com.enjoyf.platform.service.log;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-9-26
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public interface LogService {
    /**
     * 上报PV日志
     * @param pvLog
     */
    public void reportLogInfo(PageViewLog pvLog);

    /**
     * 上报手机访问的日志
     * @param mobileAccessLog
     */
    public void reportMobileAccessInfo(MobileAccessLog mobileAccessLog);


    /**
     * 上报手机访问的日志
     *
     * @param pcAccessInfo
     */
    public void reportPCAccessInfo(PCAccessInfo pcAccessInfo);
}


