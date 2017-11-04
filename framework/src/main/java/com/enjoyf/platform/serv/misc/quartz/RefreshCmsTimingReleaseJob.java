package com.enjoyf.platform.serv.misc.quartz;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.RefreshCMSTiming;
import com.enjoyf.platform.service.misc.RefreshCMSTimingField;
import com.enjoyf.platform.service.misc.RefreshReleaseType;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by zhimingli on 2015/7/28.
 */
public class RefreshCmsTimingReleaseJob extends FivewhQuartzJob {
    private long ONE_DAY = 24 * 60 * 60 * 1000L;
    private long SLEEP_TIME = 5 * 60 * 1000L;
    private String SECREY_KEY = "7eJw!9d#";
    //php cms刷新栏目
    private String CMS_REFRESH_URL = "http://article." + WebappConfig.get().DOMAIN + "/plus/mkhtml.php";

    //cmsimage定时刷新
    private String CMSIMAGE_REFRESH_URL = "http://cmsimage." + WebappConfig.get().DOMAIN + "/refresh/cleanCache.do";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        HttpClientManager httpClient = new HttpClientManager();
        //GAlerter.lan(new Date() + "=============================RefreshCmsTimingReleaseJob");
        QueryExpress queryExpress = new QueryExpress();
        try {
            long curTimes = System.currentTimeMillis();
            queryExpress.add(QueryCriterions.eq(RefreshCMSTimingField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.leq(RefreshCMSTimingField.RELEASE_TIME, curTimes));
            List<RefreshCMSTiming> list = MiscServiceSngl.get().queryRefreshCMSTiming(queryExpress);
            if (list.size() <= 0) {
                return;
            }
            long curTime = 0l;
            String joymes = "";
            StringBuffer cmsidStr = new StringBuffer();
            for (RefreshCMSTiming refreshCMSTiming : list) {
                curTime = System.currentTimeMillis() / 1000;

                //如果任务发布的时间大于查询的时间，任务下个轮询执行。
                if (refreshCMSTiming.getRelease_time() >= curTimes) {
                    continue;
                }

                joymes = MD5Util.Md5(String.valueOf(refreshCMSTiming.getCms_id()) + String.valueOf(curTime) + SECREY_KEY);
                HttpParameter[] params = new HttpParameter[]{
                        new HttpParameter("typeid", refreshCMSTiming.getCms_id()),
                        new HttpParameter("time", curTime),
                        new HttpParameter("joymes", joymes)};

                HttpResult result = httpClient.get(CMS_REFRESH_URL, params);
                if (result.getReponseCode() != 200) {
                    GAlerter.lab(RefreshCmsTimingReleaseJob.class.getName() + " response not 200.e: CMS_REFRESH_URL=" + CMS_REFRESH_URL + ",typeid=" + refreshCMSTiming.getCms_id() + ",time=" + curTime + ",joymes=" + joymes);
                }

                cmsidStr.append(refreshCMSTiming.getCms_id() + ",");
                //更新状态
                QueryExpress qu = new QueryExpress();
                qu.add(QueryCriterions.eq(RefreshCMSTimingField.TIME_ID, refreshCMSTiming.getTime_id()));
                UpdateExpress up = new UpdateExpress();
                if (refreshCMSTiming.getRefreshReleaseType().equals(RefreshReleaseType.EVERY_DAYE)) {
                    up.set(RefreshCMSTimingField.RELEASE_TIME, (refreshCMSTiming.getRelease_time() + ONE_DAY));
                } else {
                    up.set(RefreshCMSTimingField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                }
                MiscServiceSngl.get().modifyRefreshCMSTiming(up, qu);

            }

            //休息5分钟再调用cmsimage刷新
            Thread.sleep(SLEEP_TIME);

            if (!StringUtil.isEmpty(cmsidStr.toString())) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId("sysadmin+");
                log.setOperType(LogOperType.CMS_REFRESH_MODIFY);
                log.setOpTime(new Date());
                log.setOpIp("127.0.0.1");
                log.setOpAfter("定时任务刷新cmsid:" + cmsidStr);
                ToolsServiceSngl.get().saveLog(log);
                httpClient.get(CMSIMAGE_REFRESH_URL, null);
            }

        } catch (Exception e) {
            GAlerter.lab(RefreshCmsTimingReleaseJob.class.getName() + " occured Exception.e: CMS_REFRESH_URL=" + CMS_REFRESH_URL, e);
        }
    }
}
