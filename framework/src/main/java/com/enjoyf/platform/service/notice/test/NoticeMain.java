package com.enjoyf.platform.service.notice.test;

import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.SystemNotice;
import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/22
 */
public class NoticeMain {
    static String ASK_PROFILEID = "02d40db86df60181779d4d9fd9852ebf";

    public static void main(String[] args) {
        SystemNotice systemNotice = new SystemNotice();
        systemNotice.setAppkey("17yfn24TFexGybOF0PqjdY");
        systemNotice.setCreateTime(new Date());
        systemNotice.setText("这是我的系统消息");
        systemNotice.setJi("2");
        systemNotice.setJt("1");
        try {
            NoticeServiceSngl.get().createSystemNotice(systemNotice);

            NoticeServiceSngl.get().deleteSystemNotice("17yfn24TFexGybOF0PqjdY", "2.3.0", "0", 10014l);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        while (true) {
        }
    }
}
