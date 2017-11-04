/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content.test;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.ResourceFile;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */

public class Main {

    public static void main(String[] args) {
//        Main m = new Main();
//        System.out.println("list::::" + TextJsonItem.fromJson("[{\"type\":1,\"item\":\"11111\"},{\"type\":2,\"item\":\"tupian.jpg\"},{\"type\":1,\"item\":\"aa\"},{\"type\":2,\"item\":\"tupian.jpg\"},{\"type\":1,\"item\":\"aa\"},{\"type\":1,\"item\":\"ccc\"}]"));
        //m.test();
    }

//    public void test() {
//        AccountClient acRef = new AccountClient();
//        try {
//            acRef.setAccountUno("12321312321321321");
//            acRef.setAccountDomain(AccountDomain.CLIENT);
//            acRef.setClientId("123213213");
//            acRef.setClientToken("safasfdsafdsa");
//            acRef.setAcccoutPlatform(AccountPlatform.PC);
//            acRef.setAuditStatus(AccountAuditStatus.getByValue(0));
//            userWebLogic.clientRegister(acRef);
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

//    public static void a(String[] args) {
//        Content property = new Content();
//
//        property.setUno(UUID.randomUUID().toString());
//        property.setContentType(new ContentType());
//        property.setPrivacy(PrivilegeType.PUBLIC);
//        property.setContent("setContent");
//        property.setPublishIp("127.0.0.1");
//        property.setPublishType(ContentPublishType.ORIGINAL);
//        property.getContentTag().add("a").add("b");
//
//        try {
//            property = ContentServiceSngl.get().postContent(property);
//
//            Utility.sleep(2000);
//
//            System.out.println(ContentServiceSngl.get().getContentById(property.getContentId()));
//
//            b(property);
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        Utility.sleep(10000);
//    }

//    public static void b(Content content) {
//        ContentReply property = new ContentReply();

//        property.setContentUno(content.getUno());
//        property.setContentId(content.getContentId());
//        property.setReplyBody("setReplyBody");
//        property.setReplyUno("xdfsdfwerfefsdfsfsdfsdferewrqwetgwe");
//        property.setReplyIp("128.0.0.1");

//        try {
//            property = ContentServiceSngl.get().postReply(property);

    //MessageServiceSngl.get().readNoticeByType(property.getOwnUno(), property.getNoticeType());
//            Utility.sleep(2000);

//            System.out.println(ContentServiceSngl.get().getReplyByRidCidUno(property.getReplyId(), property.getContentId(), content.getUno()));
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//    }

    public static void c(String[] args) {
        ResourceFile file = new ResourceFile();

        file.setFileId("/r002/image/2012/01/73/26190F799840D3FAE86CBA59DBEB3883.jpg");
        file.setResourceFileType(ResourceFileType.IMAGE);
        file.setCreateIp("127.0.0.1");
        file.setOwnUno("1");

        try {
            ContentServiceSngl.get().postResourceFile(file);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
