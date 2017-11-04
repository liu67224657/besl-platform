package com.enjoyf.platform.service.gameres.test;

import com.enjoyf.platform.service.gameres.GamePropertyInfo;
import com.enjoyf.platform.service.gameres.GamePropertyKeyNameCode;
import com.enjoyf.platform.service.gameres.GamePropertyValueType;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.service.ServiceException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-23
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        List<GamePropertyInfo> infoList = new LinkedList<GamePropertyInfo>();
        GamePropertyInfo info1 = new GamePropertyInfo();
        info1.setKeyName("名称");
        info1.setStringValue("我叫MT");
        info1.setKeyNameCode(GamePropertyKeyNameCode.NAME);
        info1.setGamePropertyValueType(GamePropertyValueType.STRING);
//        info1.setCreateDate(new Date());
//        info1.setCreateId("sysadmin");
//        info1.setCreateIp("127.0.0.1");
        infoList.add(info1);
//        GamePropertyInfo info2 = new GamePropertyInfo();
//        info2.setKeyName("描述");
//        info2.setStringValue("我叫MT的测试");
//        info2.setKeyNameCode(GamePropertyKeyNameCode.CHANNEL);
//        info2.setGamePropertyValueType(GamePropertyValueType.STRING);
//        info2.setCreateDate(new Date());
//        info2.setCreateId("sysadmin");
//        info2.setCreateIp("127.0.0.1");
//        infoList.add(info2);
//        GamePropertyInfo info = new GamePropertyInfo();
//        info.setKeyName("头像");
//        info.setKeyNameCode(GamePropertyKeyNameCode.ICON);
//        info.setStringValue("C:\\Users\\zhitaoshi\\Documents\\头像.jpg");
//        info.setGamePropertyValueType(GamePropertyValueType.STRING);
//        info.setCreateDate(new Date());
//        info.setCreateId("sysadmin");
//        info.setCreateIp("127.0.0.1");
//        infoList.add(info);
        try {
            List<GameDB> list = GameResourceServiceSngl.get().queryGamePropertyInfo(infoList);
            for (GameDB game : list) {
                System.out.println(game.toString());
            }
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
