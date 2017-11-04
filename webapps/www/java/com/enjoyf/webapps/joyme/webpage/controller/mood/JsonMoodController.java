package com.enjoyf.webapps.joyme.webpage.controller.mood;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.MoodHotdeployConfig;
import com.enjoyf.platform.service.content.Mood;
import com.enjoyf.platform.service.content.MoodJson;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: tony diao
 * Date: 15-02-03
 * Time: 下午12:08
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/json/mood")
public class JsonMoodController extends BaseRestSpringController {
    private JsonBinder binder = JsonBinder.buildNormalBinder();

    @RequestMapping
    @ResponseBody
    public String jsonMood(@RequestParam(value = "callback", required = false) String callback,
                           HttpServletRequest request) {

        if (StringUtil.isEmpty(callback)) {
            callback = "callback_mood";
        }

        try {

            Map<String, List<Mood>> moodMap = HotdeployConfigFactory.get().getConfig(MoodHotdeployConfig.class).getImageMap();
            Map<String, List<MoodJson>> moodJsonMap = new TreeMap<String, List<MoodJson>>(new Comparator<String>() {
                public int compare(String obj1, String obj2) {
                    //降序排序
                    return obj2.compareTo(obj1);
                }
            });

            String domain = WebappConfig.get().getUrlLib();
            for (Map.Entry<String, List<Mood>> entry : moodMap.entrySet()) {
                List<MoodJson> list = new ArrayList<MoodJson>();
                for (Mood mood : entry.getValue()) {

                    MoodJson moodJson = new MoodJson(mood.getCode(), mood.getImgUrl());
                    list.add(moodJson);
                }
                moodJsonMap.put(entry.getKey(), list);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", moodJsonMap);

            return callback + "(" + jsonObject.toString() + ")";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return callback + "(" + ResultCodeConstants.SYSTEM_ERROR.getJsonString() + ")";
        }
    }


}
