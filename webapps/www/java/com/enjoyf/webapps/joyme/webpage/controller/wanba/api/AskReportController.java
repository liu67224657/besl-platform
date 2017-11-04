package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zhimingli on 2016/12/7 0007.
 * 举报
 */

@Controller
@RequestMapping("/wanba/api/report")
public class AskReportController {

    @ResponseBody
    @RequestMapping("/savereport")
    public String savereport(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");

        String type = HTTPUtil.getParam(request, "type");
        //check param
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(type)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        int typeInt = 0;
        try {
            typeInt = Integer.parseInt(type);
        } catch (NumberFormatException ignored) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", ignored);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }


        try {
            String qid = HTTPUtil.getParam(request, "qid");
            String aid = HTTPUtil.getParam(request, "aid");

            //todo error 另外 要捕获Exception不是ServiceException
            Question question = AskServiceSngl.get().getQuestion(Long.valueOf(qid));
            if (question != null) {
                AskReport askReport = new AskReport();

                //qid
                if (typeInt == 1) {

                    askReport.setItemType(ItemType.QUESTION);
                    askReport.setDestId(Long.valueOf(qid));
                } else if (typeInt == 2) {//aid
                    askReport.setItemType(ItemType.ANSWER);
                    askReport.setDestId(Long.valueOf(aid));
                }
                askReport.setReportId(AskUtil.getAskReportId(String.valueOf(askReport.getDestId()), askReport.getItemType()));
                askReport.setDestProfileId(profileId);
                askReport.setCreateTime(new Date());
                askReport.setExtstr(question.getTitle());
                AskServiceSngl.get().insertAskReport(askReport);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return WanbaResultCodeConstants.SUCCESS.getJsonString();
    }
}
