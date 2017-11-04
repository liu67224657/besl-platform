package com.enjoyf.platform.util.sms;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午1:13
 * Description:
 */
public interface SMSsender {

    public SendResult sendMessage(String phone, String content, String type);
}
