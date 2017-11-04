package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.util.http.AppUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/4/23.
 */
public class UpdateAppConfigController {

    public static void main(String[] args) {
        List<AppConfig> configList = queryAppConfig();
        for (AppConfig config : configList) {
            if (!config.getConfigId().equals(AppConfigUtil.getAppConfigId(config.getAppKey(), String.valueOf(config.getPlatform().getCode()), config.getVersion(), config.getChannel(), String.valueOf(config.getEnterpriseType().getCode())))) {
                System.out.println("--------old:" + config.getConfigId());
                System.out.println("--------new:" + AppConfigUtil.getAppConfigId(config.getAppKey(), String.valueOf(config.getPlatform().getCode()), config.getVersion(), config.getChannel(), String.valueOf(config.getEnterpriseType().getCode())));
                System.out.println(config.getAppKey() + "," + config.getPlatform() + "," + config.getVersion() + "," + config.getChannel() + "," + config.getEnterpriseType().getCode());
                deleteAppConfig(config.getConfigId());
            }
        }


    }

    private static void deleteAppConfig(String configId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://db001.prod:3311/joymeapp?useUnicode=true&characterEncoding=UTF-8", "joymeappser", "2QWdf#Z9fc0o*$zE");
            pstmt = conn.prepareStatement("DELETE FROM app_config WHERE configid='" + configId + "'");
            int i = pstmt.executeUpdate();
            System.out.println("----------" + i);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static List<AppConfig> queryAppConfig() {
        List<AppConfig> list = new ArrayList<AppConfig>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://db001.prod:3311/joymeapp?useUnicode=true&characterEncoding=UTF-8", "joymeappser", "2QWdf#Z9fc0o*$zE");
            pstmt = conn.prepareStatement("SELECT * FROM app_config");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static AppConfig rsToObject(ResultSet rs) throws SQLException {
        AppConfig appConfig = new AppConfig();
        appConfig.setConfigId(rs.getString("configid"));
        appConfig.setAppKey(rs.getString("appkey"));
        appConfig.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        appConfig.setVersion(rs.getString("version"));
        appConfig.setChannel(rs.getString("channel"));
        appConfig.setEnterpriseType(AppEnterpriserType.getByCode(rs.getInt("enterprise")));
        appConfig.setInfo(AppConfigInfo.parse(rs.getString("appinfo")));
        appConfig.setCreateDate(rs.getTimestamp("createdate"));
        appConfig.setCreateUserId(rs.getString("createuserid"));
        appConfig.setModifyDate(rs.getTimestamp("modifydate"));
        appConfig.setModifyUserId(rs.getString("modifyuserid"));
        appConfig.setAppSecret(rs.getString("appsecret"));
        return appConfig;
    }
}
