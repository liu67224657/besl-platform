package com.enjoyf.webapps.joyme.webpage.controller.notice;

import com.enjoyf.platform.service.notice.AppNoticeSum;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.notice.NoticeDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.notice.WikiNoticeWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by pengxu on 2016/12/13.
 */
@Controller
@RequestMapping(value = "/usercenter/notice")
public class JoymeWebViewNoticeController extends BaseRestSpringController {

    @Resource(name = "wikiNoticeWebLogic")
    private WikiNoticeWebLogic wikiNoticeWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    @RequestMapping(value = "/{ntype}")
    public ModelAndView report(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("ntype") String ntype,
                               @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                               @RequestParam(value = "psize", required = false, defaultValue = "10") Integer psize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            NoticeType noticeType = NoticeType.getByCode(ntype);
            mapMessage.put("noticeType", noticeType.getCode());

            if (userSession == null) {
                mapMessage.put("isnotlogin", "true");   //判断是否弹登录框
                if (noticeType.equals(NoticeType.REPLY)) {
                    return new ModelAndView("/views/jsp/notice/webview/noticelist", mapMessage);
                } else if (noticeType.equals(NoticeType.AGREE)) {
                    return new ModelAndView("/views/jsp/notice/webview/agreelist", mapMessage);
                } else if (noticeType.equals(NoticeType.AT)) {
                    return new ModelAndView("/views/jsp/notice/webview/atlist", mapMessage);
                } else if (noticeType.equals(NoticeType.FOLLOW)) {
                    return new ModelAndView("/views/jsp/notice/webview/followlist", mapMessage);
                } else {
                    return new ModelAndView("/views/jsp/notice/webview/syslist", mapMessage);
                }
            }

            mapMessage.put("profileid", userSession.getProfileId());
            Pagination pagination = new Pagination(p * psize, p, psize);
            //查询消息
            PageRows<UserNotice> pageRows = NoticeServiceSngl.get().queryUserNotice(userSession.getProfileId(), DEFAULT_APPKEY, noticeType, pagination);
            List<NoticeDTO> list = new ArrayList<NoticeDTO>();
            List<String> profileSet = new ArrayList<String>();
            profileSet.add(userSession.getProfileId());
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (UserNotice userNotice : pageRows.getRows()) {
                    NoticeDTO noticeDTO = new NoticeDTO();
                    noticeDTO.setCreateTime(userNotice.getCreateTime());
                    noticeDTO.setAppkey(userNotice.getAppkey());
                    noticeDTO.setNoticeType(userNotice.getNoticeType());
                    noticeDTO.setProfileId(userNotice.getProfileId());
                    noticeDTO.setNoticeTimeString(DateUtil.getLastMonthLastDay(userNotice.getCreateTime()));

                    profileSet.add(userNotice.getProfileId());
                    if (!StringUtil.isEmpty(userNotice.getBody())) {
                        WikiNoticeBody wikiNoticeBody = WikiNoticeBody.fromJson(userNotice.getBody());
                        if (!StringUtil.isEmpty(wikiNoticeBody.getPageurl())) {
                            wikiNoticeBody.setPageurl(URLDecoder.decode(wikiNoticeBody.getPageurl(), "utf-8"));
                        }
                        if (!StringUtil.isEmpty(wikiNoticeBody.getContenturl())) {
                            wikiNoticeBody.setContenturl(URLDecoder.decode(wikiNoticeBody.getContenturl(), "utf-8"));
                        }
                        noticeDTO.setBody(wikiNoticeBody);
                        if (wikiNoticeBody != null) {
                            if (!StringUtil.isEmpty(wikiNoticeBody.getDestProfileId())) {
                                profileSet.add(wikiNoticeBody.getDestProfileId());
                            }
                            if (!StringUtil.isEmpty(wikiNoticeBody.getOtherProfileId())) {
                                profileSet.add(wikiNoticeBody.getOtherProfileId());
                            }
                        }
                    }
                    list.add(noticeDTO);
                }
            }
            if (!CollectionUtil.isEmpty(profileSet)) {
                Map<String, UserinfoDTO> profileMap = userCenterWebLogic.buildUserinfoMap(profileSet,false);
                mapMessage.put("profileMap", profileMap);
            }

            Set<NoticeType> noticeTypes = new HashSet<NoticeType>();
            noticeTypes.addAll(NoticeType.getAll());
            //清除当前页面消息未读数
            NoticeServiceSngl.get().readNotice(userSession.getProfileId(), DEFAULT_APPKEY, noticeType);

            //查询未读消息数量
            Map<String, AppNoticeSum> noticeSumMap = wikiNoticeWebLogic.queryNoticeSum(userSession.getProfileId(), DEFAULT_APPKEY, "", "", noticeTypes);

            mapMessage.put("noticeSumMap", noticeSumMap);
            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
//            mapMessage.put("lastMonth", DateUtil.getLastMonthLastDay());

//            Map<String, Map<String, String>> map = PointServiceSngl.get().queryChooseLottery(profileSet);
//            mapMessage.put("giftmap", map);


            if (noticeType.equals(NoticeType.REPLY)) {
                return new ModelAndView("/views/jsp/notice/webview/noticelist", mapMessage);
            } else if (noticeType.equals(NoticeType.AGREE)) {
                return new ModelAndView("/views/jsp/notice/webview/agreelist", mapMessage);
            } else if (noticeType.equals(NoticeType.AT)) {
                return new ModelAndView("/views/jsp/notice/webview/atlist", mapMessage);
            } else if (noticeType.equals(NoticeType.FOLLOW)) {
                return new ModelAndView("/views/jsp/notice/webview/followlist", mapMessage);
            } else {
                return new ModelAndView("/views/jsp/notice/webview/syslist", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
    }


    @RequestMapping(value = "/deletenotice")
    @ResponseBody
    public ModelAndView deleteNotice(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "ntype", required = false) String ntype) {
        if (StringUtil.isEmpty(ntype)) {
            return new ModelAndView("redirect:/usercenter/notice/" + ntype);
        }
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/usercenter/notice/" + ntype);
        }

        try {
            NoticeServiceSngl.get().deleteUserNoticeAllByType(userSession.getProfileId(), DEFAULT_APPKEY, NoticeType.getByCode(ntype));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return new ModelAndView("redirect:/usercenter/notice/" + ntype);
        }

        return new ModelAndView("redirect:/usercenter/notice/" + ntype);
    }
}
