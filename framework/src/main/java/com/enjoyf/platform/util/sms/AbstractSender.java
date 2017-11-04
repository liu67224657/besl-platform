package com.enjoyf.platform.util.sms;

import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-30 下午6:06
 * Description:
 */
public abstract class AbstractSender implements SMSsender {

    @Override
    public SendResult sendMessage(String phone, String content, String type) {
        SendResult sendResult = null;
        try {
            sendResult = sendAction(phone, content);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e:", e);
        }

        if (sendResult == null) {
            GAlerter.lab(this.getClass().getName() + "send result is null.phone: " + phone);
            sendResult = new SendResult();
            sendResult.setCode(SendResult.SEND_ERROR);
            sendResult.setMsg("ssend.result.null");
        }

        return sendResult;
    }

    protected abstract SendResult sendAction(String phone, String content);
}
