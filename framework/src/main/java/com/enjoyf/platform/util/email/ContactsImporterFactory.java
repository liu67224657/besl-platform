package com.enjoyf.platform.util.email;


import com.enjoyf.platform.service.social.InviteMailProvider;
import com.enjoyf.platform.util.email.email.OneSixThreeImporter;
import com.enjoyf.platform.util.email.email.*;
import com.enjoyf.platform.util.email.google.GoogleImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ContactsImporter工厂类
 *
 * @author
 */
public class ContactsImporterFactory {

    private static Logger logger = LoggerFactory.getLogger(ContactsImporterFactory.class);

    public static ContactsImporter getImporterByInviteMailProvider(InviteMailProvider mailProvider, String email, String pwd) {
        if (mailProvider.equals(InviteMailProvider.MAIL_GMAIL)) {
            return getGmailContacts(email, pwd);
        } else if (mailProvider.equals(InviteMailProvider.MAIL_126)) {
            return getOneTwoSixContacts(email, pwd);
        } else if (mailProvider.equals(InviteMailProvider.MAIL_SOHU)) {
            return getSohuContacts(email, pwd);
        } else if (mailProvider.equals(InviteMailProvider.MAIL_MSN)){
            return getMSNContacts(email,pwd);
        } else {
            logger.info("getImporterByInviteMailProvider not suppory provider:",mailProvider);
            return null;
        }
    }

    /**
     * 获取Gmail Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return Gmail Importer实例
     */
    public static ContactsImporter getGmailContacts(String email,
                                                    String password) {
        return new GoogleImporter(email, password);
    }

    /**
     * 获取Yahoo Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return Yahoo Importer实例
     */
    public static ContactsImporter getYahooContacts(String email,
                                                    String password) {
        return new YahooImporter(email, password);
    }

    /**
     * 获取163 Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return 163 Importer实例
     */
    public static ContactsImporter getOneSixThreeContacts(String email,
                                                          String password) {
        return new OneSixThreeImporter(email, password);
    }

    /**
     * 获取126 Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return 126 Importer实例
     */
    public static ContactsImporter getOneTwoSixContacts(String email,
                                                        String password) {
        return new OneTwoSixImporter(email, password);
    }

    /**
     * 获取sina Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return sina Importer实例
     */
    public static ContactsImporter getSinaContacts(String email, String password) {
        return new SinaImporter(email, password);
    }

    /**
     * 获取sohu Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return sohu Importer实例
     */
    public static ContactsImporter getSohuContacts(String email, String password) {
        return new SohuImporter(email, password);
    }

    /**
     * 获取tom Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return tom Importer实例
     */
    public static ContactsImporter getTomContacts(String email, String password) {
        return new TomImporter(email, password);
    }

    /**
     * 获取yeah Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return yeah Importer实例
     */
    public static ContactsImporter getYeahContacts(String email, String password) {
        return new YeahImporter(email, password);
    }

    /**
     * 获取189 Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return 189 Import实例
     */
    public static ContactsImporter getOneEightNineContacts(String email, String password) {
        return new OneEightNineImporter(email, password);
    }

    /**
     * 获取139 Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return 139 Import实例
     */
    public static ContactsImporter getOneThreeNineContacts(String email, String password) {
        return new OneThreeNineImporter(email, password);
    }

    /**
     * 获取Hotmail Importer实例
     *
     * @param email    email地址
     * @param password 密码
     * @return Hotmail Importer实例
     */
    public static ContactsImporter getHotmailContacts(String email,
                                                      String password) {
        return new HotmailImporter(email, password);
    }


    /**
     * 获取MSN Importer实例
     *
     * @param username 用户名
     * @param password 密码
     * @return MSN Importer实例
     */
    public static ContactsImporter getMSNContacts(String username,
                                                  String password) {
        return new HotmailImporter(username, password);
    }

}
