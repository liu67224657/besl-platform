package com.enjoyf.platform.tools.usercenter;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.usercenter.UserCenterCache;
import com.enjoyf.platform.serv.usercenter.UserCenterConfig;
import com.enjoyf.platform.serv.usercenter.UsercenterRedis;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.*;

/**
 * Created by zhimingli on 2017-1-10 0010.
 * 有2个导入功能
 * 1、导入用户简介 homewiki.user_addition  ---->`usercenter`.`profile`
 * 2、导入用户隐私设置 homewiki.user_addition  ---->`usercenter`.`user_privacy`
 */
public class UpdateProfileByHomeWiki {

    private static UserCenterHandler userCenterHandler = null;
    private static UserCenterCache userCenterCache = null;
    private static UsercenterRedis usercenterRedis = null;
    private static UserCenterConfig config;


    private static String url = "172.16.75.143";
    private static String username = "caoshuguang";
    private static String password = "123456";

    static {
        if (WebappConfig.get().getDomain().equals("joyme.alpha")) {

        } else if (WebappConfig.get().getDomain().equals("joyme.beta")) {
            //beta有619条记录
            url = "10.171.101.30";
            username = "td_java";
            password = "tgT68Hgc7y";
        } else if (WebappConfig.get().getDomain().equals("joyme.com")) {
            //com有18644条记录
            url = "rm-2zem35fsj37eqzu3p.mysql.rds.aliyuncs.com";
            username = "td_java";
            password = "tgT68Hgc7y";
        }
    }

    public static void main(String[] args) {


        System.out.println("==============start=================");
        FiveProps fiveProps = Props.instance().getServProps();
        try {
            userCenterHandler = new UserCenterHandler("usercenter", fiveProps);
            config = new UserCenterConfig(fiveProps);
            userCenterCache = new UserCenterCache(config.getMemCachedConfig());
            usercenterRedis = new UsercenterRedis(fiveProps);
        } catch (Exception e) {
            e.printStackTrace();
            Utility.sleep(5000);
            System.exit(0);
        }

        //得到总条数
        int count = getCount();

        //更新
        if (count > 0) {
            update(count);
        }

        System.out.println("==============end=================");
    }

    private static void update(int count) {
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int start = 0;
        int limit = 100;

        //遍历次数
        int forsize = count / limit;
        if (count % limit != 0) {
            forsize += 1;
        }

        System.out.println("forsize---forsize>" + forsize);
        for (int i = 0; i < forsize; i++) {
            try {
                // String sql = "SELECT * FROM `user_addition` WHERE profileid='02d1ef6a587cc1399eb9d84007290f1d' ORDER BY user_id ASC LIMIT " + start + "," + end;
                String sql = "SELECT * FROM `user_addition`  ORDER BY user_id ASC LIMIT " + start + "," + limit;
                System.out.println("sql---sql>" + sql);
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String profileid = rs.getString("profileid");
                    if (StringUtil.isEmpty(profileid)) {
                        continue;
                    }
                    try {
                        Profile profile = userCenterHandler.getProfile(profileid);
                        if (profile == null) {
                            continue;
                        }
                        UpdateExpress updateExpress = new UpdateExpress();
                        String brief = rs.getString("brief");//简介
                        String interest = rs.getString("interest");//兴趣
                        String birthday = rs.getString("birthday");//生日 2017-01-01

                        if (!StringUtil.isEmpty(brief) || !StringUtil.isEmpty(interest) || !StringUtil.isEmpty(birthday)) {
                            if (!StringUtil.isEmpty(brief)) {
                                updateExpress.set(ProfileField.DESCRIPTION, brief);
                            }
                            if (!StringUtil.isEmpty(interest)) {
                                updateExpress.set(ProfileField.HOBBY, interest);
                            }

                            //更新用户简介数据
                            if (!StringUtil.isEmpty(birthday)) {
                                //DB格式 2017/01/01
                                updateExpress.set(ProfileField.BIRTHDAY, birthday.replaceAll("-", "/"));
                            }
                            boolean bval = userCenterHandler.modifyProfile(updateExpress, profileid);
                            if (bval) {
                                userCenterCache.deleteProfile(profile);
                            }
                        }

                        //更新用户隐私数据
                        int is_attention = rs.getInt("is_attention");//是否允许他人关注(1：允许，0：不允许)
                        int is_secretchat = rs.getInt("is_secretchat");//是否允许他人私信(1：允许，0：不允许)


                        QueryExpress qu = new QueryExpress();
                        qu.add(QueryCriterions.eq(UserPrivacyField.PROFILEID, profileid));
                        UserPrivacy userPrivacy = userCenterHandler.getUserPrivacy(qu);
                        if (userPrivacy == null) {
                            UserPrivacyPrivacyAlarm privacyAlarm = new UserPrivacyPrivacyAlarm();
                            privacyAlarm.setUserat("1");
                            privacyAlarm.setSysteminfo("1");
                            privacyAlarm.setFollow("1");
                            privacyAlarm.setComment("1");
                            privacyAlarm.setAgreement("1");

                            UserPrivacyFunction privacyFunction = new UserPrivacyFunction();
                            privacyFunction.setAcceptFollow(String.valueOf(is_attention));
                            privacyFunction.setChat(String.valueOf(is_secretchat));

                            userPrivacy = new UserPrivacy();
                            userPrivacy.setFunctionSetting(privacyFunction);
                            userPrivacy.setAlarmSetting(privacyAlarm);
                            userPrivacy.setCreateip("127.0.0.1");
                            userPrivacy.setCreatetime(new java.util.Date());
                            userPrivacy.setProfileId(profileid);
                            userPrivacy.setUpdateip("127.0.0.1");
                            userPrivacy.setUpdatetime(new java.util.Date());
                            userCenterHandler.addUserPrivacy(userPrivacy);

                        } else {
                            UserPrivacyFunction privacyFunction = new UserPrivacyFunction();
                            privacyFunction.setAcceptFollow(String.valueOf(is_attention));
                            privacyFunction.setChat(String.valueOf(is_secretchat));

                            UpdateExpress up = new UpdateExpress();
                            up.set(UserPrivacyField.FUNCTIONSETTING, privacyFunction.toJson());

                            boolean userBval = userCenterHandler.modifyUserPrivacy(qu, up);
                            if (userBval) {
                                usercenterRedis.delWikiUserPrivacy(profileid);
                            }
                        }
                        System.out.println("success---profileid>" + profileid);
                    } catch (Exception e) {
                        System.out.println("errorerrorerrorerror---profileid>" + profileid);
                        e.printStackTrace();
                    }
                }
                start = start + limit;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        DataBaseUtil.closeResultSet(rs);
        DataBaseUtil.closeStatment(pstmt);
        DataBaseUtil.closeConnection(conn);

    }


    private static int getCount() {
        Connection conn = getConn();
        //String getCountsql = "SELECT COUNT(*) FROM `user_addition` WHERE profileid='02d1ef6a587cc1399eb9d84007290f1d'";
        String getCountsql = "SELECT COUNT(*) FROM `user_addition`";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(getCountsql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeConnection(conn);
        }
        return count;
    }


    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String jdbcurl = "jdbc:mysql://" + url + ":3306/homewiki?useUnicode=true&characterEncoding=UTF-8";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(jdbcurl, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
