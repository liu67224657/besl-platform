package com.enjoyf.platform.service.usercenter.test;


import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ActivityUserEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Calendar;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public class ActvitiyUserMain {

    public static void main(String[] args) throws ServiceException {

//        List<Profile> profileList= UserCenterServiceSngl.get().queryProfile(new QueryExpress());
//
//        System.out.println(UserCenterUtil.getAppSumId("3LNN98uYx60V0Ho6H0Kant", "mt"));
//
//
//        Calendar calendar = Calendar.getInstance();
//        for (int i = 3000; i < 4000; i++) {
//            ActivityUserEvent event = new ActivityUserEvent();
//            calendar.add(Calendar.SECOND, 1);
//            event.setActionTime(calendar.getTime());
//
//            Profile profile=profileList.get(i%profileList.size());
//
//            event.setActionType(ActivityActionType.MODIFY_ARTICLE);
//            event.setSubkey("mt");
//            event.setAppkey("3LNN98uYx60V0Ho6H0Kant");
//            event.setProfileId(profile.getProfileId());
//            event.setUno(profile.getUno());
//            event.setObjectType(ActivityObjectType.WIKI_ARTICLE);
//            event.setObjectId("index.shtml");
//
//            EventDispatchServiceSngl.get().dispatch(event);
//        }


//        while (true) {
//        }

        PageRows<ActivityProfile> rows = UserCenterServiceSngl.get().queryActivityProfile("3LNN98uYx60V0Ho6H0Kant", "mt", new Pagination(2 * 10, 2, 10));


        System.out.println(rows.getPage().getTotalRows());
        for (ActivityProfile activityUser : rows.getRows()) {
            System.out.println(activityUser);
        }

        System.out.println(UserCenterServiceSngl.get().getActvitiyUserSum("3LNN98uYx60V0Ho6H0Kant", "mt"));

    }
}
