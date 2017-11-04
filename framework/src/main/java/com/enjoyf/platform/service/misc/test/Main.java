/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc.test;

import com.enjoyf.platform.service.misc.JoymeOperate;
import com.enjoyf.platform.service.misc.JoymeOperateLog;
import com.enjoyf.platform.service.misc.JoymeOperateType;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Utility;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        JoymeOperate joymeOperate = new JoymeOperate();
        joymeOperate.setContent("11");
        joymeOperate.setCreateTime(new Date());
        joymeOperate.setCreateUserId("1112");
        joymeOperate.setOperateType(JoymeOperateType.REFRESH_INDEX);
        joymeOperate.setServerId("admin");
        try {
            MiscServiceSngl.get().createOperate(joymeOperate);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            JoymeOperateLog log = new JoymeOperateLog();
            log.setOperateId(MiscServiceSngl.get().doOperate("www", JoymeOperateType.REFRESH_INDEX).get(0).getOperateId());
            log.setOperateServerId("www");
            log.setOperateType(JoymeOperateType.REFRESH_INDEX);
            MiscServiceSngl.get().createOperateLog(log);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
