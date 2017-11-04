package com.enjoyf.platform.service.lottery.test;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.lottery.LotteryHandler;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-28
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws ServiceException {


//        Lottery lottery = new Lottery();
//
//        lottery.setBaseRate(10000);
//        lottery.setLotteryDesc("liuhao2016");
//        lottery.setLotteryName("liuhao2016");
//        lottery.setAwardLevelCount(3);
//        lottery.setCreateDate(new Date());
//        lottery.setCreateIp("127.0.0.1");
//        lottery.setValidStatus(ValidStatus.VALID);
//        lottery.setLastModifyDate(new Date());
//        lottery.setLastModifyIp("");
//        lottery.setLotteryTimesType(LotteryTimesType.ONE_DAY);
//        try {
//            lottery = LotteryServiceSngl.get().createLottery(lottery);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }

        try {
            Lottery lottery = LotteryServiceSngl.get().getLotteryById(10075);
            System.out.print(lottery);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //10075
        LotteryAward lotteryAward = new LotteryAward();
        lotteryAward.setLotteryAwardAmount(0);
        lotteryAward.setLotteryId(10075);
        lotteryAward.setLotteryAwardLevel(1);
        lotteryAward.setLotteryAwardMinRate(0);
        lotteryAward.setLotteryAwardMaxRate(1000);
        lotteryAward.setLotteryAwardName("iphone6s");
        lotteryAward.setLotteryAwardType(LotteryAwardType.VIRTUAL);
        lotteryAward.setValidStatus(ValidStatus.VALID);
        lotteryAward.setLotteryAwardId(10190);
//        LotteryServiceSngl.get().createLotteryAward(lotteryAward);
//        importLotteryItem(lotteryAward,0);


        LotteryAward lotteryAward1 = new LotteryAward();
        lotteryAward1.setLotteryId(10075);
        lotteryAward.setLotteryAwardAmount(3);
        lotteryAward1.setLotteryAwardLevel(2);
        lotteryAward1.setLotteryAwardMinRate(1000);
        lotteryAward1.setLotteryAwardMaxRate(3000);
        lotteryAward1.setLotteryAwardName("jd card");
        lotteryAward1.setLotteryAwardType(LotteryAwardType.VIRTUAL);
        lotteryAward1.setValidStatus(ValidStatus.VALID);
        lotteryAward1.setLotteryAwardId(10191);
//        LotteryServiceSngl.get().createLotteryAward(lotteryAward1);
        importLotteryItem(lotteryAward1,3);

        LotteryAward lotteryAward2 = new LotteryAward();
        lotteryAward2.setLotteryId(10075);
        lotteryAward.setLotteryAwardAmount(7000);
        lotteryAward2.setLotteryAwardLevel(2);
        lotteryAward2.setLotteryAwardMinRate(3001);
        lotteryAward2.setLotteryAwardMaxRate(10000);
        lotteryAward2.setLotteryAwardName("point");
        lotteryAward2.setLotteryAwardType(LotteryAwardType.VIRTUAL);
        lotteryAward2.setValidStatus(ValidStatus.VALID);
        lotteryAward2.setLotteryAwardId(10192);
//        LotteryServiceSngl.get().createLotteryAward(lotteryAward2);
//        importLotteryItem(lotteryAward2,7000);

        while (true){

        }


        /*try {
            LotteryServiceSngl.get().userLotteryAward(ProfileServiceSngl.get().getProfileBlogByScreenName("小小snail灬").getUno(), "127.0.0.1", new Date(), 10030L, 10030L);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

//        FiveProps fiveProps = Props.instance().getServProps();
//        try {
//            LotteryHandler lotteryHandler = new LotteryHandler("writeable", fiveProps);
//            int randomNum = lotteryHandler.getRandomMap(100L, 5);
//            System.out.println(randomNum);
//        } catch (DbException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

//        for (int i = 0; i < 1000; i++) {
//            int random = (int) (Math.random() * (1000 - 1)) + 1;
//            System.out.println(random);
//        }


//        for(int index=0;index<100;index++){
//            final int finalIndex = index;
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        sleep(5000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//        for (int i = 0; i < 1000; i++) {
//            try {
//                System.out.println(LotteryServiceSngl.get().lottery(10075, "thread" + i, "#####", new Date(), "thread" + i));
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }
//                }
//            };
//            thread.start();
//        }


    }

    private static void importLotteryItem(LotteryAward lotteryAward, int amount) throws ServiceException {
        List<LotteryAwardItem> itemlist = new ArrayList<LotteryAwardItem>();
        for (int i = 0; i < amount; i++) {
            LotteryAwardItem item = new LotteryAwardItem();
            item.setLotteryId(lotteryAward.getLotteryId());
            item.setLotteryAwardId(lotteryAward.getLotteryAwardId());
            item.setName1(lotteryAward.getLotteryAwardName() + "code");
            item.setValue1(lotteryAward.getLotteryAwardName() + " code" + UUID.randomUUID().toString());
            itemlist.add(item);

        }
        LotteryServiceSngl.get().createLotteryAwardItem(lotteryAward.getLotteryAwardId(), itemlist);
    }

}
