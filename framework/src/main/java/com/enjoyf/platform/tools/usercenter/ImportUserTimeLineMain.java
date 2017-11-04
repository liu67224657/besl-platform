package com.enjoyf.platform.tools.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.usertimeline.UserTimelineHandler;
import com.enjoyf.platform.serv.timeline.TimeLineRedis;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.service.timeline.UserTimelineField;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.List;

/**
 * 只在dev和 alpha执行
 *
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2017/1/20
 */
public class ImportUserTimeLineMain {


    public static void main(String[] args) throws DbException {
        FiveProps fiveProps = Props.instance().getServProps();

        UserTimelineHandler userTimelineHandler = new UserTimelineHandler("timeline", fiveProps);

        TimeLineRedis timeLineRedis = new TimeLineRedis(fiveProps);


        QueryExpress queryExpress = new QueryExpress();
        List<UserTimeline> list = userTimelineHandler.queryUserTimeline(queryExpress);
        int i=0;
        System.out.println("start size is:"+list.size());
        for (UserTimeline userTimeline : list) {
            timeLineRedis.putUserTimeLineByProfileId(userTimeline);
            System.out.println(userTimeline);
            i++;
        }
        System.out.println("end size is"+i);

    }
}
