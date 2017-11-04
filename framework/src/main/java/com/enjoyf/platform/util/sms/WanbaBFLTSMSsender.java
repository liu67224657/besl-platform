package com.enjoyf.platform.util.sms;

import com.enjoyf.platform.util.log.GAlerter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-30 下午3:33
 * Description:
 */
public class WanbaBFLTSMSsender extends AbstractSender {
    private static final String API_URL = "http://cf.lmobile.cn/submitdata/service.asmx/g_Submit";
    private static final String SNAME = "dllxfd01";
    private static final String SPWD = "HQLcT0aSs";

    private String smsName = SNAME;
    private String smsPwd = SPWD;

    public WanbaBFLTSMSsender() {
    }

    public WanbaBFLTSMSsender(String smsName, String smsPwd) {
        this.smsName = smsName;
        this.smsPwd = smsPwd;
    }

    /**
     * string sname,    提交账户
     * string spwd,     提交账户的密码
     * string scorpid,  企业代码（扩展号，不确定请赋值空）
     * string sprdid,   产品编号
     * string sdst,   接收号码间用英文半角逗号“,”隔开，触发产品一次只能提交一个,,其他产品一次不能超过10万个号码
     * string smsg   信息内容, UTF-8编码，通常为70汉字以内，具体由平台内部决定
     *
     * @return
     */
    @Override
    public SendResult sendAction(String phone, String content) {
        SendResult returnObj = new SendResult();
        GAlerter.lan("wanba sendcode:" + phone + ",content=" + content);
        try {
            String postData = "sname=" + smsName + "&spwd=" + smsPwd + "&scorpid=&sprdid=1012818&sdst=" + phone + "&smsg=" + java.net.URLEncoder.encode(content, "utf-8");
            //发送POST请求
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                returnObj.setCode(SendResult.URL_ERROR);
                return returnObj;
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return getSendResult(result);
        } catch (IOException e) {
            GAlerter.lan(this.getClass().getName() + " occured Exception.e:", e);

            returnObj.setCode(SendResult.URL_ERROR);

        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured Exception.e:", e);
            returnObj.setCode(SendResult.SEND_ERROR);
        }
        return returnObj;
    }

    private SendResult getSendResult(String xmlResult) throws Exception {
        SendResult sendResult = new SendResult();

        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = new ByteArrayInputStream(xmlResult.getBytes());
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        if (map == null) {
            sendResult.setCode(SendResult.SEND_ERROR);
            return sendResult;
        }
        int code = Integer.parseInt(map.get("State"));
        String msg = map.get("MsgState");
        sendResult.setCode(code);
        sendResult.setMsg(msg);


        return sendResult;
    }
}
