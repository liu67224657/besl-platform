package com.enjoyf.webapps.joyme.webpage.controller.notice;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.wiki.WikiNoticeEvent;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeDestType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.notice.NoticeDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.notice.WikiNoticeWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengxu on 2016/12/13.
 */
@Controller
@RequestMapping(value = "/joyme/api/notice")
public class JoymeNoticeController extends BaseRestSpringController {

    @Resource(name = "wikiNoticeWebLogic")
    private WikiNoticeWebLogic wikiNoticeWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    @RequestMapping(value = "/report")
    @ResponseBody
    public String report(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "pid", required = false) String pid,
                         @RequestParam(value = "ntype", required = false) String ntype,
                         @RequestParam(value = "destpid", required = false) String destpid,
                         @RequestParam(value = "url", required = false) String url,
                         @RequestParam(value = "curl", required = false) String contenturl,
                         @RequestParam(value = "desc", required = false) String desc,
                         @RequestParam(value = "desttype", required = false) String destType,
                         @RequestParam(value = "otherpid", required = false) String otherPid,
                         @RequestParam(value = "time", required = false) String time
    ) {
        if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(ntype) || StringUtil.isEmpty(destpid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            NoticeType noticeType = NoticeType.getByCode(ntype);
            if (noticeType != null) {
                WikiNoticeEvent wikiNoticeEvent = new WikiNoticeEvent();
                wikiNoticeEvent.setProfileId(pid);
                wikiNoticeEvent.setAppkey(DEFAULT_APPKEY);
                if (StringUtil.isEmpty(time)) {
                    wikiNoticeEvent.setCreateTime(new Date());
                } else {
                    try {
                        wikiNoticeEvent.setCreateTime(new Date(Long.parseLong(time)));
                    } catch (NumberFormatException e) {
                        wikiNoticeEvent.setCreateTime(new Date());
                    }
                }

                wikiNoticeEvent.setType(noticeType);

                WikiNoticeBody wikiNoticeBody = new WikiNoticeBody();
                wikiNoticeBody.setPageurl(StringUtil.isEmpty(url) ? "" : URLEncoder.encode(url, "UTF-8"));
                wikiNoticeBody.setContenturl(StringUtil.isEmpty(contenturl) ? "" : URLEncoder.encode(contenturl, "UTF-8"));
                wikiNoticeBody.setDesc(StringUtil.isEmpty(desc) ? "" : desc);
                wikiNoticeBody.setDestProfileId(destpid);
                if (StringUtil.isEmpty(destType)) {
                    wikiNoticeBody.setWikiNoticeDestType(WikiNoticeDestType.REPLY);
                } else {
                    wikiNoticeBody.setWikiNoticeDestType(WikiNoticeDestType.getByCode(Integer.parseInt(destType)));
                }
                wikiNoticeBody.setOtherProfileId(otherPid);
                wikiNoticeEvent.setBody(wikiNoticeBody);

                EventDispatchServiceSngl.get().dispatch(wikiNoticeEvent);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/list")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "ntype", required = false) String ntype,
                       @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                       @RequestParam(value = "psize", required = false, defaultValue = "10") Integer psize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            NoticeType noticeType = NoticeType.getByCode(ntype);

            if (userSession == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

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
                    noticeDTO.setTimeString(sdf.format(userNotice.getCreateTime()));
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
                Map<String, UserinfoDTO> profileMap = userCenterWebLogic.buildUserinfoMap(profileSet, false);
                mapMessage.put("profileMap", profileMap);
            }

            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
            jsonObject.put("result", mapMessage);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return jsonObject.toString();
    }
}
