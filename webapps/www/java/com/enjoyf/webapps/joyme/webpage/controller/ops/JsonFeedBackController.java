package com.enjoyf.webapps.joyme.webpage.controller.ops;

import com.enjoyf.platform.service.misc.Feedback;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:意见反馈相关的ACTION
 * </p>
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Controller
@RequestMapping(value = "/json/ops/feedback")
public class JsonFeedBackController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(JsonFeedBackController.class);
    @RequestMapping(value = "/send")
    @ResponseBody
    public String sendReply(HttpServletRequest request,
                                   @RequestParam(value = "feedbackBody") String feedbackBody) {
        String uno = getUserBySession(request).getBlogwebsite().getUno();
        Feedback feedback = new Feedback();
        feedback.setFeedbackBody(feedbackBody);
        feedback.setFeedbackIp(getIp(request));
        feedback.setUno(uno);
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            feedback = MiscServiceSngl.get().postFeedback(feedback);

            if(feedback!=null){
                return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_S));
            }
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E));
        }

        return JsonBinder.buildNonNullBinder().toJson(new JoymeResultMsg(JoymeResultMsg.CODE_E));
    }
}
