package com.enjoyf.platform.util.sms;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.xml.Dom4jUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-30 下午3:33
 * Description:
 */
@Deprecated
public class ZXTSMSsender  extends AbstractSender {
    private static final String API_URL = "http://221.179.180.158:9000/QxtSms/QxtFirewall";
    private static final String OPER_ID = "yzfdkj";
    private static final String OPER_PWD = "yzfdkj1";

    /**
     * Op erID	1	业务标识	已开通的帐号名称
     * OperPass	1	操作密码	与帐号名称对应的密码
     * SendTime	0	发送时间	YYYYMMDDHHMMSS格式,为空表示立即发送
     * ValidTime	0	消息存活有效期	YYYYMMDDHHMMSS格式
     * AppendID	0	附加号码	见注1
     * DesMobile	1	接收手机号集合	1.	发送单条消息时，此字段填写11位的手机号码。
     * 2.	群发消息时，此字段为使用逗号分隔的手机号码串。
     * 3.	每批发送的手机号数量不得超过200个
     * Content	1	发送消息内容	最长不要超过500个字
     * ContentType	1	消息类型	取值有15和8。15：以普通短信形式下发,8：以长短信形式下发。
     */
    @Override
    public SendResult sendAction(String phone, String content) {
        SendResult returnObj = new SendResult();

        HttpClientManager clientManager = new HttpClientManager();
        HttpParameter[] params = new HttpParameter[]{
                new HttpParameter("OperID", OPER_ID),
                new HttpParameter("OperPass", OPER_PWD),
                new HttpParameter("DesMobile", phone),
                new HttpParameter("Content", content),
                new HttpParameter("ContentType", 8)
        };
        HttpResult result = clientManager.get(API_URL, params, "GBK");

//        发送失败
        if (result.getReponseCode() != HttpClientManager.OK) {
            GAlerter.lab(this.getClass().getName() + "send sms url occured error. " + result.getReponseCode());
            returnObj.setCode(SendResult.URL_ERROR);
            return returnObj;
        }
        try {
            returnObj = getSendResult(result.getResult());
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured Exception.e:", e);
            returnObj.setCode(SendResult.URL_ERROR);
            return returnObj;
        }

        return returnObj;
    }

    private SendResult getSendResult(String xmlResult) throws Exception {
        SendResult sendResult = new SendResult();

        Document document = Dom4jUtil.getDocument(xmlResult);
        List list = document.selectNodes("//code");
        if (!CollectionUtil.isEmpty(list)) {
            String code = (String) ((Element) list.get(0)).getData();
            sendResult.setThirdCode(code);
            sendResult.setCode(code.equals("03") ? SendResult.SEND_SUCESS : SendResult.SEND_ERROR);
        }


        List msgids = document.selectNodes("//msgid");
        if (!CollectionUtil.isEmpty(msgids)) {
            String msgid = (String) ((Element) msgids.get(0)).getData();
            sendResult.setMsg(msgid);
        }


        return sendResult;
    }
}
