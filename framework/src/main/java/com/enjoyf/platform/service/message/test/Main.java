/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.message.test;

import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
//        postMessage();
//        testPushMsg();
        System.out.println("===================");
    }

    public static void postMessage() {
        Message property = new Message();

        property.setBody("setBody");
        property.setMsgType(MessageType.PRIVATE);
        property.setOwnUno("setOwnUno");
        property.setSenderUno("setOwnUno");
        property.setRecieverUno("setRecieverUno");
        property.setSendIp("127.0.0.1");

        Pagination page = new Pagination();

        try {
            MessageServiceSngl.get().postMessage(property.getOwnUno(), property);

            MessageServiceSngl.get().removeMessage(property.getOwnUno(), property.getMsgId());

            Utility.sleep(2000);

            System.out.println(MessageServiceSngl.get().queryMessagesBySender("setOwnUno", "setOwnUno", MessageType.PRIVATE, page));

            a();
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Utility.sleep(10000);
    }

    public static void a() {
        Notice property = new Notice();

        property.setCount(123);
        property.setNoticeType(NoticeType.NEW_AT);
        property.setOwnUno("setOwnUno");
        property.setDescription("setDescription");

        try {
            property = MessageServiceSngl.get().postNotice(property.getOwnUno(), property);

            //MessageServiceSngl.get().readNoticeByType(property.getOwnUno(), property.getNoticeType());
            Utility.sleep(2000);

            System.out.println(MessageServiceSngl.get().getNotice(property.getOwnUno(), property.getNoticeType()));
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Utility.sleep(10000);
    }

//    public static void testPushMsg() {
//        PushMessage pushMessage = new PushMessage();
//        try {
//            pushMessage.setMsgBody(new String("ios测试消息ddddddddd".getBytes(), "utf8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        pushMessage.setPushMessageType(PushMessageType.DEVICE);
//        pushMessage.setPushMsgCode(PushMessageCode.content);
//        pushMessage.setPushMsgId(0);
//        pushMessage.setRemoveStatus(ValidStatus.VALID);
//        pushMessage.setSendDate(new Date());
//
//        PushMessageOptions pmo = new PushMessageOptions();
//        pmo.setCid("1");
//        pmo.setDomain("1");
//        pmo.setCuno("1");
//        pushMessage.setMsgOptions(pmo);
//
//        //  parsePushMessage(pushMessage,true);
//
//        try {
//
//            //
//            pushMessage = MessageServiceSngl.get().create(pushMessage);
//
//            List<String> iosTestClient = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getIosTestClient();
//            for (String uuid : iosTestClient) {
//                ProfileMobileDevice profileMobileDevice = new ProfileMobileDevice();
//                profileMobileDevice.setMdSerial(uuid);
//                profileMobileDevice.setMdClientType(ProfileMobileDeviceClientType.IOS);
//                profileMobileDevice.setMdHdType("ip");
//                profileMobileDevice.setMdOsVersion("5.1.1");
//                profileMobileDevice.setPushStatus(ActStatus.ACTED);
//
//
//                //
//                ProfileServiceSngl.get().increaseProfileMobileDevice(profileMobileDevice);
//            }
//            System.out.println("#####:" + MessageServiceSngl.get().sendPushMessage(pushMessage, true));
//
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        System.out.println("over......");
//    }


}
