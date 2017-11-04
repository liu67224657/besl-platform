package com.enjoyf.platform.tools.usercenter;

import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.misc.MiscConstants;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2016/11/11 0011.
 */
public class UpdateQQbyUnionid {

    private static UserCenterHandler userCenterHandler = null;
    private static RedisManager userCenterRedis = null;

    private static final String KEY_OAUTH2ME_ = MiscConstants.SERVICE_SECTION + "_oauth2me_";

    public static void main(String[] args) {

        System.out.println("==============start=================");
        FiveProps fiveProps = Props.instance().getServProps();
        try {
            userCenterHandler = new UserCenterHandler("usercenter", fiveProps);
            userCenterRedis = new RedisManager(fiveProps);
        } catch (Exception e) {
            e.printStackTrace();
            Utility.sleep(5000);
            System.exit(0);
        }
        loadUserLogin();
    }


    private static void loadUserLogin() {
        int toltle = 0;
        try {
            int cp = 0;
            Pagination page = null;
            do {
                cp += 1;
                page = new Pagination(1000 * cp, cp, 1000);
                PageRows<UserLogin> pageRows = userCenterHandler.queryUserLoginPage(
                        new QueryExpress().add(QueryCriterions.eq(UserLoginField.LOGIN_DOMAIN, LoginDomain.QQ.getCode())), page);
                if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                    return;
                }
                page = pageRows.getPage();
                for (UserLogin userLogin : pageRows.getRows()) {
                    if (userLogin != null) {
                        System.out.println("====================" + toltle);
                        userCenterRedis.set(KEY_OAUTH2ME_ + userLogin.getLoginKey(), new Gson().toJson(userLogin));
                        toltle++;
                    }
                }
            } while (!page.isLastPage());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("========totle num:" + toltle);
    }


    private static List<String> pidList(String filePath) {
        BufferedReader br = null;
        InputStreamReader isr = null;
        List<String> list = new ArrayList<String>();
        try {
            isr = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
            br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


}
