package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.codehaus.jackson.type.TypeReference;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class AbstractGameClientBaseController extends BaseRestSpringController {

    protected static final int MAX_LIKE_GAME = 6;
    protected static final Long TAGID = -2l;
    protected static final String TASK_GROUP_ID_NEW = "newbietasks";
    protected static final String TASK_GROUP_ID_EVERYDAY = "dailytasks";
    protected static final String TASK_GROUP_ID_TIMETASK = "timedtask";
    protected static final String TASK_GROUP_ID_SIGN = "gameclient.sign";
    //  protected static final String KEY_GIFT_TASKxTAB_FLAG = "tabflag";
    //  protected static final String VALUE_GIFT_TAB_FLAG = "midou";

    protected String getSignGroup(AppPlatform appPlatform) {
        return TaskUtil.getTaskGroupId(TASK_GROUP_ID_SIGN, appPlatform);
    }

    protected Pagination getPaginationbyRequest(HttpServletRequest request) {
        String pageNo = request.getParameter("pnum");
        String pageCount = request.getParameter("pcount");

        int pagNum = 1;
        try {
            pagNum = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
        }
        int pageSize = 24;
        try {
            pageSize = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
        }

        return new Pagination(pagNum * pageSize, pagNum, pageSize);
    }

    protected String getLikeGameTaskID(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".followgames";
    }

    protected String getHotGameTaskID(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".hotgames";
    }

    protected String getMiyouPageTaskId(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".likefans";
    }

    protected String getSaveProfilePicTaskId(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".uploadimages";
    }

    //赞1篇文章
    protected String getLikearticleNewbietasks(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".likearticle";
    }

    //新关注1个话题
    protected String getSubscribeNewbietasks(AppPlatform appPlatform) {
        return "newbietasks." + appPlatform.getCode() + ".subscribe";
    }

    protected String getDayilLikeFans(AppPlatform appPlatform) {
        return "dailytasks." + appPlatform.getCode() + ".likefans";
    }

    protected String getDayilViewTest(AppPlatform appPlatform) {
        return "dailytasks." + appPlatform.getCode() + ".viewtest";
    }

    //浏览3篇话题文章
    protected String getReadarticleDailytasks(AppPlatform appPlatform) {
        return "dailytasks." + appPlatform.getCode() + ".readarticle";
    }

    //赞3篇喜欢的文章
    protected String getLikearticleDailytasks(AppPlatform appPlatform) {
        return "dailytasks." + appPlatform.getCode() + ".likearticle";
    }

    protected Set<String> getTaskGroupId(AppPlatform appPlatform) {
        Set<String> groupIds = new HashSet<String>();
        groupIds.add(TaskUtil.getTaskGroupId(TASK_GROUP_ID_EVERYDAY, appPlatform));
        groupIds.add(TaskUtil.getTaskGroupId(TASK_GROUP_ID_NEW, appPlatform));
        groupIds.add(TaskUtil.getTaskGroupId(TASK_GROUP_ID_TIMETASK, appPlatform));
        return groupIds;
    }

    protected void sendOutTask(Profile profile, AppPlatform appPlatform, String ip, String appKey, Date date, String directId, String clientId) {
        TaskAwardEvent taskEvent = new TaskAwardEvent();
        taskEvent.setDoTaskDate(date);
        taskEvent.setTaskAction(TaskAction.LIKE_GAME);
//        taskEvent.setTaskId(getLikeGameTaskID(appPlatform));
        taskEvent.setProfileId(profile.getProfileId());
        taskEvent.setTaskIp(ip);
        taskEvent.setUno(profile.getUno());
        taskEvent.setAppkey(AppUtil.getAppKey(appKey));
        taskEvent.setProfileKey(profile.getProfileKey());
        taskEvent.setAppPlatform(appPlatform);
        taskEvent.setDirectId(directId);
        taskEvent.setClientId(clientId);
        try {
            EventDispatchServiceSngl.get().dispatch(taskEvent);
        } catch (ServiceException e) {
        }
    }

    protected String replaceHtmlText(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        text = text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replace("'", "&apos;");
        return text;
    }

    public static List<String> fromJson(String jsonString) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

      public static BufferedImage getBufferedImage(String imgUrl) {
        URL url = null;
        InputStream is = null;
        BufferedImage img = null;
        try {
            url = new URL(imgUrl);
            is = url.openStream();
            img = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return img;
    }

}
