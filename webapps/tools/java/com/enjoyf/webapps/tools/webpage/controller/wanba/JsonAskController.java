package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.apache.openjpa.jdbc.sql.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/26
 */
@Controller
@RequestMapping("/wanba/json/ask")
public class JsonAskController extends ToolsBaseController {

    @RequestMapping("/question/delete")
    @ResponseBody
    public String questionDelte(@RequestParam(value = "qid") Long questionId) {

        try {
            boolean bval = AskServiceSngl.get().deleteQuestion(questionId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping("/answer/delete")
    @ResponseBody
    public String answerRemove(@RequestParam(value = "aid") Long aid) {

        try {
            boolean bval = AskServiceSngl.get().deleteAnswer(aid);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

}
