package com.enjoyf.webapps.joyme.webpage.controller.task;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/3
 * Description:
 */
public class TaskController {


    public ModelAndView sign(HttpServletRequest request, HttpServletResponse response, String profileId) {


        UserPropKey userPropKey = new UserPropKey(UserPropDomain.DEFAULT, profileId, "task.mine.sign");

        try {
            UserProperty property = UserPropsServiceSngl.get().getUserProperty(userPropKey);

            if (property == null) {
                UserPropsServiceSngl.get().increaseUserProperty(userPropKey, 1);
                //todo 第一次 +积分
            } else {
                Date date = property.getValue().equals("1") ? property.getInitialDate() : property.getModifyDate();
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int propDay = calendar.get(Calendar.DAY_OF_YEAR);

                calendar.setTime(now);
                int nowDay = calendar.get(Calendar.DAY_OF_YEAR);

                if (nowDay == propDay) {
                    //todo 同一天签到直接返回错误
                } else if (nowDay - propDay > 1) {
                    //todo 不是连续签到清空 按第一次加积分
                    property.setModifyDate(now);
                    property.setUserPropKey(userPropKey);
                    property.setValue("1");
                    UserPropsServiceSngl.get().modifyUserProperty(property);

                    //todo 第一次 +积分
                } else {
                    property = UserPropsServiceSngl.get().increaseUserProperty(userPropKey, 1);
                    //todo 连续加积分
                    if (property.getIntValue() % 7 == 0) {
                        //todo 连续天登陆二外加积分
                    }

                }

            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }


        return new ModelAndView("");
    }
}
