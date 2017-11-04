package com.enjoyf.platform.serv.point.quartz;

import com.enjoyf.platform.serv.point.PointLogic;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.wiki.WikiNoticeEvent;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeDestType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by pengxu on 2016/12/2.
 */
public class PrestigeQuartzJob extends FivewhQuartzJob {
    private static final String PRESTIGE_RANK_TYPE = "prestige";
    private static final int GET_NUM = 100;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        PointLogic pointLogic = (PointLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(PointLogic.class.getName());
        //获得上个月的key
        String lastMonthKey = DateUtil.getLastMonthRankLinKey(PRESTIGE_RANK_TYPE, Calendar.DAY_OF_MONTH);
        //获取上个月声望数量
        int size = pointLogic.queryPrestigeSize(lastMonthKey);
        if (size > 0) {
            int count = pointLogic.queryPrestigeSize("");//总数

            Pagination pagination = new Pagination(size, 1, GET_NUM);
            for (int i = 1; i <= pagination.getMaxPage(); i++) {
                pagination.setCurPage(i);
                Map<String, Integer> prestigeMap = pointLogic.queryPretigeByMonth(lastMonthKey, pagination); //查询上个月声望有变化的用户
                executJob(lastMonthKey, prestigeMap, pointLogic, count);
            }

        }
    }

    private void executJob(String key, Map<String, Integer> prestigeMap, PointLogic pointLogic, int count) {
        if (prestigeMap == null) {
            return;
        }
        Set<String> pids = prestigeMap.keySet();
        //用户排名
        Map<String, Integer> rankMap = pointLogic.queryUserPrestigeRanks(pids);
        //用户总声望
        Map<String, Integer> userPrestigeMap = pointLogic.queryUserPresige(pids);

        int score;
        int rank;
        for (String pid : pids) {
            rank = rankMap.get(pid);//排名
            if (rank < 0) {
                continue;
            }
            score = userPrestigeMap.get(pid);//分数

            double doubleSize = (double) count;
            double doubleRank = (double) rank;
            double percentage = (doubleSize - doubleRank) / doubleSize * 100;
            percentage = Math.round(percentage);//产品要求四舍五入

            WikiNoticeEvent wikiNoticeEvent = new WikiNoticeEvent();
            wikiNoticeEvent.setType(NoticeType.SYSTEM);
            wikiNoticeEvent.setCreateTime(new Date());
            wikiNoticeEvent.setProfileId(pid);
            wikiNoticeEvent.setAppkey("default");
            WikiNoticeBody wikiNoticeBody = new WikiNoticeBody();

            wikiNoticeBody.setWikiNoticeDestType(WikiNoticeDestType.PRESTIGE);//8是声望
            wikiNoticeBody.setPrestige(score);
            if (score < 1) {
                percentage = 0d;
            }
            wikiNoticeBody.setDesc((int) percentage + "%");
            wikiNoticeEvent.setBody(wikiNoticeBody);


            try {
                EventDispatchServiceSngl.get().dispatch(wikiNoticeEvent);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + "executJob e", e);
            }
        }

    }

}
