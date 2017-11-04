package com.enjoyf.test;

import com.enjoyf.platform.util.StringUtil;

import java.text.DecimalFormat;

/**
 * Created by zhimingli on 2015/12/13 0013.
 */
public class CreateTable2016 {
    private static String year = "2017";

    public static void main(String[] args) {
        //advertise();
        //point();
        //sync();
        // toolslog();


        //===========================PC=============================
        // STAT();
        //STAT2();
         STAT3();
        //updateStat();
    }

    private static void updateStat() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "ALTER TABLE `gamevent_log_" + number + "` ADD COLUMN uniquekey VARCHAR(32) DEFAULT NULL;\n" +
                    "UPDATE gamevent_log_" + number + " SET uniquekey=eventid;\n" +
                    "ALTER TABLE `gamevent_log_" + number + "` ADD UNIQUE `unique_" + number + "` (uniquekey);\n";
            System.out.println(str);
        }
    }

    private static void STAT3() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `online_info_" + number + "` (\n" +
                    "  `online_id` bigint(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `appkey` varchar(36) NOT NULL,\n" +
                    "  `zonekey` varchar(32) NOT NULL,\n" +
                    "  `value` int(11) NOT NULL,\n" +
                    "  `report_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `stat_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                    "  `platform` int(11) DEFAULT '0',\n" +
                    "  `source` int(11) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`online_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            System.out.println(str);
        }
    }

    private static void STAT2() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `gamevent_log_" + number + "` (\n" +
                    "  `eventid` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `channel` varchar(32) DEFAULT NULL,\n" +
                    "  `profileid` char(32) DEFAULT NULL,\n" +
                    "  `appkey` varchar(36) DEFAULT NULL,\n" +
                    "  `zonekey` varchar(32) DEFAULT NULL,\n" +
                    "  `eventtype` varchar(32) DEFAULT NULL,\n" +
                    "  `eventtime` timestamp NULL DEFAULT NULL,\n" +
                    "  `eventip` varchar(255) DEFAULT NULL,\n" +
                    "  `logstatus` varchar(255) DEFAULT NULL,\n" +
                    "  `roleid` varchar(64) DEFAULT NULL,\n" +
                    "  `rolename` varchar(255) DEFAULT NULL,\n" +
                    "  `value` double DEFAULT NULL,\n" +
                    "  `goodsid` varchar(64) DEFAULT NULL,\n" +
                    "  `goodsname` varchar(255) DEFAULT NULL,\n" +
                    "  `paytype` varchar(32) DEFAULT NULL,\n" +
                    "  `platform` int(11) DEFAULT NULL,\n" +
                    "  `sdkversion` varchar(64) DEFAULT NULL,\n" +
                    "  `version` varchar(32) DEFAULT NULL,\n" +
                    "  `deviceid` varchar(64) DEFAULT NULL,\n" +
                    "  `osv` varchar(32) DEFAULT NULL,\n" +
                    "  `screen` varchar(32) DEFAULT NULL,\n" +
                    "  `source` varchar(32) DEFAULT NULL,\n" +
                    "  `mock` varchar(32) DEFAULT NULL,\n" +
                    "  `country` varchar(32) DEFAULT NULL,\n" +
                    "  `network` varchar(32) DEFAULT NULL,\n" +
                    "  `extra1` varchar(255) DEFAULT NULL,\n" +
                    "  `extra2` varchar(255) DEFAULT NULL,\n" +
                    "  `extra3` varchar(255) DEFAULT NULL,\n" +
                    "  `logindomain` varchar(32) DEFAULT NULL,\n" +
                    "  `adcode` varchar(36) DEFAULT NULL,\n" +
                    "  `uniquekey` varchar(32) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`eventid`),\n" +
                    "  UNIQUE KEY `unique_" + number + "` (`uniquekey`),\n" +
                    "  KEY `IDX_EVENTTIME` (`eventtime`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            System.out.println(str);
        }
    }

    private static void STAT() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `game_stat_item_" + number + "` (\n" +
                    "  `stati_id` bigint(16) NOT NULL AUTO_INCREMENT,\n" +
                    "  `appkey` varchar(128) DEFAULT NULL COMMENT 'appkey',\n" +
                    "  `zonekey` varchar(128) DEFAULT NULL COMMENT '分区的key',\n" +
                    "  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',\n" +
                    "  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',\n" +
                    "  `date_type` int(11) DEFAULT NULL COMMENT '1- day/2- week/3-month/4- year/0- 自定义',\n" +
                    "  `stat_domain` varchar(128) DEFAULT NULL COMMENT '统计的类型（域）',\n" +
                    "  `stat_section` varchar(128) DEFAULT NULL COMMENT '统计的二级分类',\n" +
                    "  `value` double DEFAULT NULL,\n" +
                    "  `group_id` bigint(20) DEFAULT NULL COMMENT '统计组ID',\n" +
                    "  PRIMARY KEY (`stati_id`),\n" +
                    "  UNIQUE KEY `_unqie` (`stat_domain`,`group_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            System.out.println(str);
        }
    }

    private static void toolslog() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `TOOLS_LOG_" + number + "` (\n" +
                    "  `LID` int(11) NOT NULL,\n" +
                    "  `BTYPE` varchar(32) DEFAULT NULL COMMENT '大类',\n" +
                    "  `STYPE` varchar(32) DEFAULT NULL COMMENT '小类',\n" +
                    "  `OPUSERID` varchar(32) NOT NULL COMMENT '操作人ID',\n" +
                    "  `OPTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',\n" +
                    "  `OPIP` varchar(20) DEFAULT NULL COMMENT '访问IP',\n" +
                    "  `SRCID` varchar(64) DEFAULT NULL COMMENT '操作对象ID',\n" +
                    "  `OPBEFORE` varchar(2000) DEFAULT NULL COMMENT '操作前',\n" +
                    "  `OPAFTER` varchar(2000) DEFAULT NULL COMMENT '操作后',\n" +
                    "  `DESCRIPTION` varchar(2000) DEFAULT NULL COMMENT '描述',\n" +
                    "  PRIMARY KEY (`LID`),\n" +
                    "  KEY `IDX_TOOLS_LOG_BTYPE` (`BTYPE`),\n" +
                    "  KEY `IDX_TOOLS_LOG_STYPE` (`STYPE`),\n" +
                    "  KEY `IDX_TOOLS_LOG_OPUSERID` (`OPUSERID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            System.out.println(str);
        }
    }

    private static void sync() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `share_user_log_" + number + "` (\n" +
                    "  `share_user_log_id` bigint(16) NOT NULL AUTO_INCREMENT,\n" +
                    "  `uno` varchar(36) NOT NULL,\n" +
                    "  `share_id` bigint(16) NOT NULL,\n" +
                    "  `account_domain` varchar(32) NOT NULL,\n" +
                    "  `sharedate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `sharetime` date NOT NULL,\n" +
                    "  `sharetype` smallint(6) NOT NULL DEFAULT '0',\n" +
                    "  `shareurl` varchar(256) DEFAULT NULL COMMENT '分享地址',\n" +
                    "  PRIMARY KEY (`share_user_log_id`),\n" +
                    "  KEY `IDX_SHARE_USER_LOG_" + number + "_UNO` (`uno`),\n" +
                    "  KEY `IDX_SHARE_USER_LOG_" + number + "_SHAREID` (`share_id`),\n" +
                    "  KEY `IDX_SHARE_USER_LOG_" + number + "_CREATETIME` (`sharetime`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            System.out.println(str);
        }
    }

    private static void point() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `user_day_point_" + number + "` (\n" +
                    "  `user_day_pointid` bigint(16) NOT NULL AUTO_INCREMENT,\n" +
                    "  `user_no` varchar(36) NOT NULL,\n" +
                    "  `point_value` int(4) DEFAULT NULL,\n" +
                    "  `action_type` smallint(2) NOT NULL,\n" +
                    "  `point_date` date NOT NULL,\n" +
                    "  `profileid` char(32) DEFAULT NULL COMMENT 'profileid',\n" +
                    "  PRIMARY KEY (`user_day_pointid`),\n" +
                    "  KEY `IDX_USER_DAY_POINT_UNO` (`user_no`),\n" +
                    "  KEY `IDX_USER_DAY_POINT_POINTDATE` (`point_date`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            System.out.println(str);
        }
    }

    private static void advertise() {
        for (int i = 1; i <= 12; i++) {
            String number = year + StringUtil.appendZore(i, 2);
            String str = "CREATE TABLE `ADVERTISE_EVENT_" + number + "` (\n" +
                    "  `EVENTID` bigint(16) NOT NULL,\n" +
                    "  `PUBLISHID` varchar(36) NOT NULL,\n" +
                    "  `LOCATIONCODE` varchar(36) DEFAULT NULL,\n" +
                    "  `EVENTTYPE` varchar(32) NOT NULL,\n" +
                    "  `UNO` varchar(36) DEFAULT NULL,\n" +
                    "  `SESSIONID` varchar(64) DEFAULT NULL,\n" +
                    "  `GLOBALID` varchar(36) DEFAULT NULL,\n" +
                    "  `EVENTCOUNT` bigint(16) DEFAULT '0',\n" +
                    "  `EVENTDESC` varchar(512) DEFAULT NULL,\n" +
                    "  `EVENTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `EVENTIP` varchar(32) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`EVENTID`) USING BTREE,\n" +
                    "  KEY `IDX_ADVERTISE_EVENT_" + number + "_PUBLISHID` (`PUBLISHID`) USING BTREE,\n" +
                    "  KEY `IDX_ADVERTISE_EVENT_" + number + "_DATE` (`EVENTDATE`) USING BTREE\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            System.out.println(str);
        }
    }
}
