package com.enjoyf.platform.util;

import com.enjoyf.platform.db.TableUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
class CreateTableUtil {

    public static void main(String[] args) {

        System.out.println(" USE STATS;");
//        System.out.println("DROP TABLE IF EXISTS `game_stat_group`;\n" +
//                "CREATE TABLE `game_stat_group` (\n" +
//                "  `group_id` bigint(16) NOT NULL AUTO_INCREMENT,\n" +
//                "  `group_name` varchar(128) DEFAULT NULL,\n" +
//                "  `status` int(11) DEFAULT '0' COMMENT '是否统计玩的状态1-完成 0-统计中，默认0',\n" +
//                "  PRIMARY KEY (`group_id`)\n" +
//                ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;\n" +
//                "");
        String onlineInfo = "DROP TABLE IF EXISTS `online_info_XX`;\n" +
                "CREATE TABLE `online_info_XX` (\n" +
                "  `online_id` bigint(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `appkey` varchar(36) NOT NULL,\n" +
                "  `zonekey` varchar(32) NOT NULL,\n" +
                "  `value` int(11) NOT NULL,\n" +
                "  `report_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  `stat_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
                "  `platform` int(11) DEFAULT '0',\n" +
                "  `source` int(11) DEFAULT '0',\n" +
                "  PRIMARY KEY (`online_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;\n";

        String gameItem = "DROP TABLE IF EXISTS `game_stat_item_XX`;\n" +
                "CREATE TABLE `game_stat_item_XX` (\n" +
                "  `stati_id` bigint(16) NOT NULL AUTO_INCREMENT,\n" +
                "  `appkey` varchar(128) DEFAULT NULL COMMENT 'appkey',\n" +
                "  `zonekey` varchar(128) DEFAULT NULL COMMENT '分区的key',\n" +
                "  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',\n" +
                "  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',\n" +
                "  `date_type` int(11) DEFAULT NULL COMMENT '1- day/2- week/3-month/4- year/0- 自定义',\n" +
                "  `stat_domain` varchar(128) DEFAULT NULL COMMENT '统计的类型（域）',\n" +
                "  `stat_section` varchar(128) DEFAULT NULL COMMENT '统计的二级分类',\n" +
                "  `value` int(11) DEFAULT NULL COMMENT '统计数据',\n" +
                "  `group_id` bigint(20) DEFAULT NULL COMMENT '统计组ID',\n" +
                "  PRIMARY KEY (`stati_id`),\n" +
                "  UNIQUE KEY `_unqie` (`stat_domain`,`group_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;";

        String eventLog = "DROP TABLE IF EXISTS `gamevent_log_XX`;\n" +
                "CREATE TABLE `gamevent_log_XX` (\n" +
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
                "  `value` int(11) DEFAULT NULL,\n" +
                "  `goodsid` varchar(64) DEFAULT NULL,\n" +
                "  `goodsname` varchar(255) DEFAULT NULL,\n" +
                "  `paytype` varchar(32) DEFAULT NULL,\n" +
                "  `platform` int(11) DEFAULT NULL,\n" +
                "  `sdkversion` varchar(32) DEFAULT NULL,\n" +
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
                "  PRIMARY KEY (`eventid`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;\n";
//
        CreateTableByMonth("ALTER TABLE `gamevent_log_XX` ADD UNIQUE `unique` (profileid, appkey, zonekey, eventtype, eventtime,channel);\n", "2015");
        CreateTableByMonth("ALTER TABLE `gamevent_log_XX` ADD UNIQUE `unique` (profileid, appkey, zonekey, eventtype, eventtime,channel);\n", "2016");
//        CreateTableByMonth(gameItem, "2015");
//        CreateTableByMonth(eventLog, "2015");

//
//        CreateTableByUno("CREATE TABLE `user_props_default_XX` (\n" +
//                "  `uno` varchar(36) NOT NULL,\n" +
//                "  `propkey` varchar(32) NOT NULL,\n" +
//                "  `idx` int(12) NOT NULL DEFAULT '0',\n" +
//                "  `upvalue` varchar(1024) NOT NULL,\n" +
//                "  `initialdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',\n" +
//                "  `modifydate` timestamp NULL DEFAULT NULL,\n" +
//                "  PRIMARY KEY (`uno`,`propkey`,`idx`)\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n",100);


    }

    private static void CreateTableByUno(String tableScript, int size) {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += tableScript.replace("XX", StringUtil.appendZore(i, 2));
        }

        System.out.println(s);
    }

    private static void CreateTableByMonth(String tableScript, String year) {
        String s = "";
        for (int i = 1; i <= 12; i++) {
            s += tableScript.replace("XX", year + StringUtil.appendZore(i, 2));
        }

        System.out.println(s);
    }

    private static void CreateTimeLineTableByParamUno(String tableScript, String param, int size) {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += tableScript.replace("XX", StringUtil.appendZore(i, 2));
        }
        s = s.replace("<PARAM>", param);
        System.out.println(s);
    }

    private static void getTableNoByUno(String uno) {
        System.out.println(TableUtil.getTableNumSuffix(uno.hashCode(), 100));
    }
}
