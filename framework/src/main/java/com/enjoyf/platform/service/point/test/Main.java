package com.enjoyf.platform.service.point.test;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PinYinUtil;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static PointHandler pointHandler = null;
    public static ContentHandler contentHandler = null;


    private static String formatDateTime(String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today) || current.compareTo(today) == 0) {
            return "今天 " + time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {

            return "昨天 " + time.split(" ")[1];
        } else {
            int index = time.indexOf("-") + 1;
            return time.substring(index, time.length());
        }
    }

    public static void main(String[] args) {
        String a = "style\"aaa/style";
        a = a.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"","&quot;");
        System.out.print(a);
//        } catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


//        try {
//            List<ActivityGoods> list = PointServiceSngl.get().listActivityGoods(new QueryExpress());
//            for(ActivityGoods activityGoods:list){
//                PointServiceSngl.get().modifyActivityGoods(activityGoods.getActivityGoodsId(),new UpdateExpress().set(ActivityGoodsField.FIRST_LETTER, PinYinUtil.getFirstWordLetter(activityGoods.getActivitySubject())));
//            }
//            for (ActivityGoods activityGoods : list) {
//                if (activityGoods.getPlatform() == null) {
//                    PointServiceSngl.get().modifyActivityGoods(activityGoods.getActivityGoodsId(), new UpdateExpress().set(ActivityGoodsField.PLATFORM, AppPlatform.CLIENT.getCode()));
//                } else if (activityGoods.getPlatform().equals(AppPlatform.WEB)) {
//                    PointServiceSngl.get().modifyActivityGoods(activityGoods.getActivityGoodsId(), new UpdateExpress().set(ActivityGoodsField.PLATFORM, AppPlatform.ALL.getCode()));
//                }
//            }
//            for(ActivityGoods activityGoods:list){
//                System.out.println(activityGoods.getPlatform());
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
/*
        FiveProps fiveProps= Props.instance().getServProps();


        try {
            PointHandler pointHandler=new PointHandler("writeable",fiveProps);



            System.out.println( pointHandler.getGoodsById(10010l));




//            UserDayPoint udp=new UserDayPoint();
//            udp.setPointValue(1);
//            udp.setUserNo("1");
//            udp.setActionType(PointActionType.SHARE);
//            udp.setPointDate(new Date());
//
//           pointHandler.insertUserDayPoint(udp);
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/


/*
        UserPoint p = new UserPoint();
        try {
            p.setUserNo(ProfileServiceSngl.get().getProfileBlogByScreenName("小小snail灬").getUno());
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        p.setUserPoint(200);
        try {
            PointServiceSngl.get().addUserPoint(p);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/

//        Pagination pagination = new Pagination(2,1,2);
//       try {
//            PageRows<UserPoint> point = PointServiceSngl.get().queryUserPointByPage(new QueryExpress().add(QueryCriterions.eq(UserPointField.USERNO, "bbb")), pagination);
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        /*try {
            PointServiceSngl.get().modifyPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.USERNO, "bbb")), new UpdateExpress().set(UserPointField.USERNO, "ddd"), 10030L);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        /*PointActionHistory pointActionHistory = new PointActionHistory();
        //pointActionHistory.setActionHistoryId(111L);
        pointActionHistory.setActionDate(new Date());
        pointActionHistory.setActionTimestamp(new Date());
        pointActionHistory.setActionDescription("aaa");
        pointActionHistory.setUserNo("aaa");
        pointActionHistory.setActionType(1);
        pointActionHistory.setPointValue(1);
        try {
            PointServiceSngl.get().addPointActionHistory(pointActionHistory);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        //PointActionHistory actionHistory = PointServiceSngl.get().getPointActionHistory();

/*
     try {
        Goods goods = new Goods();
        goods.setGoodsId(1l);
        goods.setGoodsName("a");
        goods.setCreateDate(new Date());
        goods.setCreateIp("1");
        goods.setCreateUserId("1");
        goods.setConsumeTimesType(ConsumeTimesType.getByCode(1));
        goods.setGoodsAmount(2);
        goods.setGoodsExpireDate(new Date());
        goods.setRemoveStatus(ActStatus.UNACT);
        goods.setGoodsConsumePoint(1);
        goods.setGoodsPic("a");
        goods.setGoodsResetAmount(1);
        goods.setGoodsDesc("a");
        goods.setDisplayOrder(1);
        goods.setHot(true);
        goods.setNew(true);
        goods.setGoodsType(GoodsType.getByCode(1));
        PointServiceSngl.get().addGoods(goods);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/


/*
     try {
            Pagination pagination = new Pagination(2,1,2);
            System.out.println(PointServiceSngl.get().queryGoodsByPage(GoodsType.getByCode(1),pagination));
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/

/*
        GoodsItem item = new GoodsItem();
        item.setGoodsId(10040L);
        item.setConsumeDate(new Date());
        item.setCreateDate(new Date());
        item.setExchangeStatus(ActStatus.UNACT);
        item.setOwnUserNo("aaa");
        item.setSnName1("1");
        item.setSnValue1("1");
        item.setSnName2("2");
        item.setSnValue2("2");
        item.setSnName3("3");
        item.setSnValue3("3");
        List<GoodsItem> list = new ArrayList<GoodsItem>();
        list.add(item);
        try {
            PointServiceSngl.get().addGoodsItem(list);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
*/

/*
        try {
            PageRows<UserConsumeLog> log = PointServiceSngl.get().queryConsumeLog("111", new SimpleDateFormat("yyyy-MM-dd").parse("2013-06-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2013-06-20"), new Pagination(2, 1, 2));
            System.out.println(log.getRows());
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/

//        try {
//            List<UserRecentLogEntry> list = PointServiceSngl.get().queryUserRecentLog();
//            for (UserRecentLogEntry log : list) {
//                System.out.println(log.toString());
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


    }

}
