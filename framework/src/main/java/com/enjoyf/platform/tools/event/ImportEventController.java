package com.enjoyf.platform.tools.event;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.event.EventHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.db.wikiurl.WikiUrlHandler;
import com.enjoyf.platform.serv.comment.CommentCache;
import com.enjoyf.platform.serv.comment.CommentConfig;
import com.enjoyf.platform.serv.event.TaskRedisManager;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.service.EventConfig;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportEventController {


    private static long CONTENT_ID = 61000l;

    private static EventConfig config;
    private static EventHandler commentHandler;
    private static UserCenterHandler userCenterHandler;
    private static TaskRedisManager redisManager;
    private static CommentCache commentCache;
    private static WikiUrlHandler wikiUrlHandler;

    public static void main(String[] args) {
//        FiveProps servProps = Props.instance().getServProps();
//        redisManager = new TaskRedisManager(servProps);
        //getTaskLog();

//        String obj = "ip|819269260|172.16.100.110" ;
//        String[] s = obj.split("\\|");
//        for(String a:s){
//            System.out.println(a);
//        }

    }

    private static void getTaskLog() {
        long uid = 4358721l;
        String profileId = "e5e20e13257f318d3efdbee4f2054f3d";
        String groupId = "youku.sign.0";
        redisManager.putFLag(profileId, groupId, 6);
        System.out.println(redisManager.getFLag(profileId, groupId));
    }

}
