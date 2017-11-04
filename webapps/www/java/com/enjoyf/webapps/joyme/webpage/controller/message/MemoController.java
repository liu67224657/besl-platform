package com.enjoyf.webapps.joyme.webpage.controller.message;

import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import com.enjoyf.webapps.joyme.webpage.util.MemoReturnUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p/>
 * Description:小纸条读取的action
 * </p>
 * /memo/readmemo  读取小纸条
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@RequestMapping(value = "/message/memo")
@Controller
public class MemoController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(MemoController.class);

    /**
     * 读取小纸条，需要倆个参数
     * memoKey   小纸条的类型
     * returnUrl 返回地址
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/readmemo")
    public ModelAndView readMemo(HttpServletRequest request,
                                 @RequestParam(value = "typeCode") String typeCode,
                                 @RequestParam(value = "url", required = false) String url) {

        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            return new ModelAndView("/login");
        }

        String uno = userSession.getBlogwebsite().getUno();

        if (typeCode.equals(NoticeType.COMPLETE_PROFILE.getCode())) {
            //完善信息
            if (StringUtil.isEmpty(url)) {
                url = "redirect:/home";
            } else {
                url = "redirect:" + url;
            }
            request.getSession().setAttribute(Constant.SESSION_KEY_USER_INFOREBACKURL, url);
        }

        //判断key是否合法
        NoticeType noticeType = NoticeType.getByCode(typeCode);
        if (noticeType == null && StringUtil.isEmpty(url)) {
            logger.error(this.getClass().getName() + " read Memo.type error.code:" + typeCode);
            return new ModelAndView("/views/jsp/common/error");
        }

        try {
            MessageServiceSngl.get().readNoticeByType(uno, noticeType);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }

        return new ModelAndView(StringUtil.isEmpty(url) ? MemoReturnUrl.getUrlByKey(noticeType) : url);
    }
}
