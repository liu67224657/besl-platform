package com.enjoyf.webapps.joyme.weblogic.usercenter;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.TimeLineActionType;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.service.timeline.UserTimelineDomain;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service(value = "userTimelineWebLogic")
public class UserTimelineWebLogic {

    private static Logger logger = LoggerFactory.getLogger(UserTimelineWebLogic.class);

    public UserTimeline buildUserTimeline(String profileid, String actionType, String type, String extendBody, Date time) throws ServiceException {
        logger.debug("buildUserTimeline profileid." + profileid + " ,actionType:" + actionType + " ,type" + type);

        TimeLineActionType timeLineActionType = TimeLineActionType.getByCode(actionType);
        if (timeLineActionType == null) {
            return null;
        }
        //
        UserTimeline userTimeline = new UserTimeline();
        userTimeline.setActionType(timeLineActionType);
        userTimeline.setCreateTime(new Timestamp(time.getTime()));
        userTimeline.setDomain(UserTimelineDomain.MY.getCode());
        userTimeline.setType(type);
        userTimeline.setProfileId(profileid);
        userTimeline.setExtendBody(extendBody);
        return TimeLineServiceSngl.get().buildUserTimeline(userTimeline);
    }


    public PageRows<UserTimeline> queryUserTimeline(String profileid, String domain, String type, Pagination page) throws ServiceException {
        logger.debug("queryUserTimeline profileid:" + profileid);
        PageRows<UserTimeline> pageRows = TimeLineServiceSngl.get().queryUserTimeline(profileid, domain, type, page);
        return pageRows;
    }

    public boolean delUserTimeline(String profileid, String domain, String type, String destId, String destProfileid) throws ServiceException {
        UserTimeline userTimeline = new UserTimeline();
        userTimeline.setDestProfileid(destProfileid);
        userTimeline.setDestId(Long.parseLong(destId));
        userTimeline.setDomain(domain);
        userTimeline.setType(type);
        userTimeline.setProfileId(profileid);
        return TimeLineServiceSngl.get().delUserTimeline(userTimeline);
    }
}
