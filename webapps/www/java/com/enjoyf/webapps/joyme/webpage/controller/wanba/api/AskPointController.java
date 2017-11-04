package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.comment.AbstractCommentController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by zhimingli on 2016/10/25 0025.
 * <p/>
 * 分享加积分
 */

@Controller
@RequestMapping("/wanba/api/point")
public class AskPointController extends AbstractCommentController {

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //分享成功加积分
    @ResponseBody
    @RequestMapping("/share/complete")
    public String complete(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();


        String pid = HTTPUtil.getParam(request, "pid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(appkey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            //玩霸加积分
            int sharePoint = pointWebLogic.modifyUserPoint(PointActionType.WANBA_SHARE, pid, AppUtil.getAppKey(appkey), WanbaPointType.SHARE, null);
            String pointText = "";
            if (sharePoint > 0) {
                pointText = i18nSource.getMessage("point.share.success", new Object[]{WanbaPointType.SHARE}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointText);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }
}
