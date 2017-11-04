package com.enjoyf.webapps.tools.webpage.controller.pushmsg;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.service.message.PushMessageCode;
import com.enjoyf.platform.service.message.PushMessageField;
import com.enjoyf.platform.service.message.PushMessageOptions;
import com.enjoyf.platform.service.message.PushMessageType;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.pushmsg.PushMessageWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-6-13
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/msg")
public class PushMsgController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TEST_ENV = "test";
    private static final String PRO_ENV = "product";

    @Resource(name = "pushMessageWebLogic")
    private PushMessageWebLogic pushMessageWebLogic;

    public static List<ActStatus> sendStatuses = new ArrayList<ActStatus>();
    public static List<PushMessageType> pushMessageTypes = new ArrayList<PushMessageType>();
    public static List<PushMessageCode> pushMsgCodes = new ArrayList<PushMessageCode>();

    static {
        sendStatuses.add(ActStatus.ACTED);
        sendStatuses.add(ActStatus.UNACT);
        pushMessageTypes.addAll(PushMessageType.getAll());
        pushMsgCodes.addAll(PushMessageCode.getAll());
    }

    @RequestMapping(value = "/msglist")
    public ModelAndView msgList(@RequestParam(value = "pushStartDate", required = false) String pushDate,
                                @RequestParam(value = "pushEndDate", required = false) String pushDate2,
                                @RequestParam(value = "sendStatus", required = false) String sendStatus,
                                @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        if(logger.isDebugEnabled()){
            logger.debug("Method msgList invoked. pushDate:" + pushDate + ", sendStatus: " + sendStatus);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        try {
            PageRows<PushMessage> pageRows = pushMessageWebLogic.getPushMessages(
                    Strings.isNullOrEmpty(pushDate) ? DateUtil.adjustDate(DateUtil.ignoreTime(new Date()), Calendar.DAY_OF_MONTH, -5)
                            : DateUtil.StringTodate(pushDate, DateUtil.PATTERN_DATE),
                    Strings.isNullOrEmpty(pushDate2) ? DateUtil.StringTodate(DateUtil.DateToString(new Date(), DateUtil.DATE_FORMAT) + " 23:59:59", DateUtil.DEFAULT_DATE_FORMAT2)
                            : DateUtil.StringTodate((pushDate2 + ToolsLog.END_TIME_PRIFIX), DateUtil.PATTERN_DATE_TIME),
                    sendStatus, pagination);

            msgMap.put("rows", pageRows.getRows());
            msgMap.put("sendStatuses", sendStatuses);
            msgMap.put("sendStatus", sendStatus);
            msgMap.put("pushStartDate", pushDate);
            msgMap.put("pushEndDate", pushDate2);
            msgMap.put("page", pageRows.getPage());

        } catch (ServiceException e) {

            GAlerter.lab("get PushMessages occurred an exception : " + e);
        }

        return new ModelAndView("/pushmsg/iosmsglist", msgMap);
    }


    @RequestMapping(value = "/preaddpushmsg")
    public ModelAndView preAddPushMsg() {
        Map<String, Object> msgMap = new HashMap<String, Object>();

        msgMap.put("pushMessageTypes", pushMessageTypes);
        msgMap.put("pushMsgCodes", pushMsgCodes);

        return new ModelAndView("/pushmsg/createpushmsg", msgMap);
    }

    @RequestMapping(value = "/addpushmsg")
    public ModelAndView addPushMsg(@RequestParam(value = "msgBody", required = false) String msgBody,
                                   @RequestParam(value = "pushMessageType", required = false) String pushMessageType,
                                   @RequestParam(value = "pushMessageCode", required = false) String pushMessageCode,
                                   @RequestParam(value = "url", required = false) String url,
                                   @RequestParam(value = "categoryAspect", required = false) String categoryAspect,
                                   @RequestParam(value = "categoryCode", required = false) String categoryCode) {

        if (logger.isDebugEnabled()) {
            logger.debug("Method addPushMsg invoked. msgBody:" + msgBody + ", pushMessageType: " + pushMessageType + ", pushMessageCode:"
            + pushMessageCode + ", url:" + url + ", categoryAspect:" + categoryAspect + ", categoryCode:" + categoryCode);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        msgMap.put("errorMsgMap", errorMsgMap);

        PushMessage pushMessage = new PushMessage();
        PushMessageOptions msgOptions = new PushMessageOptions();

        if (!StringUtil.isEmpty(msgBody)) {
            pushMessage.setMsgBody(msgBody);
        }
        if (!StringUtil.isEmpty(pushMessageType)) {
            pushMessage.setPushMessageType(PushMessageType.getByCode(pushMessageType));
        }
        if (!StringUtil.isEmpty(pushMessageCode)) {
            pushMessage.setPushMsgCode(PushMessageCode.getByCode(pushMessageCode));
        }

        if (!StringUtil.isEmpty(url)) {

            url = url.trim();
            String[] brokenString = url.split("/");

            if (brokenString.length < 2) {
                errorMsgMap.put("system", "error.exception");
                return new ModelAndView("/pushmsg/createpushmsg", msgMap);
            } else {
                String contentId = brokenString[brokenString.length - 1];
                String domain = brokenString[brokenString.length - 2];

                ProfileBlog profileBlog = null;
                try {
                    //
                    profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);

                    if(profileBlog == null){
                        errorMsgMap.put("system", "error.exception");
                        return new ModelAndView("/pushmsg/createpushmsg", msgMap);
                    }
                } catch (ServiceException e) {

                    GAlerter.lab("there is a exception when get profileBlog by domain: " , e);
                }


                msgOptions.setCid(contentId);
                msgOptions.setCuno(profileBlog.getUno());

                pushMessage.setMsgOptions(msgOptions);
            }
        }

        if (!StringUtil.isEmpty(categoryAspect) && !StringUtil.isEmpty(categoryCode)) {
            //分类编码放入cuno中，分类角度放入domainz中
            msgOptions.setCuno(categoryCode);
            msgOptions.setDomain(categoryAspect);

            pushMessage.setMsgOptions(msgOptions);
        }

        pushMessage.setSendStatus(ActStatus.UNACT);

        try {
            pushMessageWebLogic.addPushMessage(pushMessage);
        } catch (ServiceException e) {

            GAlerter.lab("there is a exception when save PushMessage : ", e);
        }

        return new ModelAndView("forward:/msg/msglist");

    }

    @RequestMapping(value = "/push")
    public ModelAndView pushMsg(@RequestParam(value = "pushMsgId", required = false, defaultValue = "0") long pushMsgId,
                                @RequestParam(value = "serviceType", required = false) String serviceType,
                                @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        if(logger.isDebugEnabled()){
            logger.debug("push message parameter pushMsgId: " + pushMsgId + ", serviceType: " + serviceType);

        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        QueryExpress queryExpress = new QueryExpress();
        boolean isTestEnv = false;

        if (pushMsgId >= 0L ) {
            queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId));
        }
        if (serviceType.equals(TEST_ENV)) {
            isTestEnv = true;
        }

        msgMap.put("serviceType", serviceType);
        msgMap.put("page", pagination);

        try {
            pushMessageWebLogic.pushMsg(queryExpress, isTestEnv);
        } catch (ServiceException e) {

            GAlerter.lab("push message caught an exception: ", e);
        }

        return new ModelAndView("forward:/msg/msglist", msgMap);
    }


    @RequestMapping(value = "/batchpushmsg")
    public ModelAndView batchPushMsg(@RequestParam(value = "pushmsgids") String pushMsgIds,
                                     @RequestParam(value = "serviceType", required = false) String serviceType,
                                     @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
                                     @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
                                     @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {

        if (logger.isDebugEnabled()) {
            logger.debug("batch push message parameter pushMsgIds: " + pushMsgIds + ", serviceType: " + serviceType);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        Pagination pagination = new Pagination(items, (pagerOffset / maxPageItems) + 1, WebappConfig.get().getHomePageSize());

        if(!StringUtil.isEmpty(pushMsgIds) && !StringUtil.isEmpty(serviceType)){
            String[] pushMsgIdArray = pushMsgIds.split(",");

            for (String pushMsgId : pushMsgIdArray) {

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, Long.parseLong(pushMsgId)));

                boolean isTestEnv = false;

                if(serviceType.equals(TEST_ENV)){
                    isTestEnv = true;
                }

                try {
                    pushMessageWebLogic.pushMsg(queryExpress, isTestEnv);
                } catch (ServiceException e) {

                    GAlerter.lab("batch push message caught an exception: ", e);
                }

            }

        }

        msgMap.put("serviceType", serviceType);
        msgMap.put("page", pagination);

        return new ModelAndView("forward:/msg/msglist", msgMap);
    }


    @RequestMapping(value = "/preeditmsg")
    public ModelAndView preeditPushMsg(@RequestParam(value = "pushMsgId", required = false) String pushMsgId) {
        if (logger.isDebugEnabled()) {
            logger.debug("preedit PushMessage pushMsgId: " + pushMsgId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        msgMap.put("pushMessageTypes", pushMessageTypes);
        msgMap.put("pushMsgCodes", pushMsgCodes);

        PushMessage pushMessage = null;
        try {
            pushMessage = pushMessageWebLogic.getPushMessageById(pushMsgId);

            if(pushMessage.getPushMsgCode().equals(PushMessageCode.content)){
                msgMap.put("profileDomain", ProfileServiceSngl.get().getProfileBlogByUno(pushMessage.getMsgOptions().getCuno()).getDomain());
            }

            msgMap.put("cid", pushMessage.getMsgOptions().getCid());
            msgMap.put("cuno", pushMessage.getMsgOptions().getCuno());
            msgMap.put("domain", pushMessage.getMsgOptions().getDomain());
            msgMap.put("pushMessage", pushMessage);

        } catch (ServiceException e) {

            GAlerter.lab("query PushMessage by PushMessageId: " + pushMsgId);
        }

        return new ModelAndView("/pushmsg/precreatepushmsg", msgMap);

    }

    @RequestMapping(value = "/editmsg")
    public ModelAndView editPushMsg(@RequestParam(value = "pushMsgId", required = false) String pushMsgId,
                                    @RequestParam(value = "msgBody", required = false) String msgBody,
                                    @RequestParam(value = "pushMessageType", required = false) String pushMessageType,
                                    @RequestParam(value = "pushMessageCode", required = false) String pushMessageCode,
                                    @RequestParam(value = "url", required = false) String url,
                                    @RequestParam(value = "categoryAspect", required = false) String categoryAspect,
                                    @RequestParam(value = "categoryCode", required = false) String categoryCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("edit PushMessage pushMsgId: " + pushMsgId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        Map<String, String> errorMsgMap = new HashMap<String, String>();
        msgMap.put("errorMsgMap", errorMsgMap);

//        PushMessage pushMessage = new PushMessage();
        PushMessageOptions msgOptions = new PushMessageOptions();
        UpdateExpress updateExpress = new UpdateExpress();

        if (!StringUtil.isEmpty(msgBody)) {
//            pushMessage.setMsgBody(msgBody);
            updateExpress.set(PushMessageField.MSGBODY, msgBody);
        }
        if (!StringUtil.isEmpty(pushMessageType)) {
//            pushMessage.setPushMessageType(PushMessageType.getByCode(pushMessageType));
            updateExpress.set(PushMessageField.PUSHMSGTYPE, pushMessageType);
        }
        if (!StringUtil.isEmpty(pushMessageCode)) {
            updateExpress.set(PushMessageField.PUSHMSGCODE, pushMessageCode);
//            pushMessage.setPushMsgCode(PushMessageCode.getByCode(pushMessageCode));
        }

        if (!StringUtil.isEmpty(url)) {

            url = url.trim();
            String[] brokenString = url.split("/");

            if (brokenString.length < 2) {
                errorMsgMap.put("system", "error.exception");
                return new ModelAndView("/pushmsg/createpushmsg", msgMap);
            } else {
                String contentId = brokenString[brokenString.length - 1];
                String domain = brokenString[brokenString.length - 2];

                ProfileBlog profileBlog = null;
                try {
                    //
                    profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);

                    if(profileBlog == null){
                        errorMsgMap.put("system", "error.exception");
                        return new ModelAndView("/pushmsg/precreatepushmsg", msgMap);
                    }
                } catch (ServiceException e) {

                    GAlerter.lab("there is a exception when get profileBlog by domain: " , e);
                }


                msgOptions.setCid(contentId);
                msgOptions.setCuno(profileBlog.getUno());

                updateExpress.set(PushMessageField.MSGOPTIONS, msgOptions.toJson());
//                pushMessage.setMsgOptions(msgOptions);
            }
        }

        if (!StringUtil.isEmpty(categoryAspect) && !StringUtil.isEmpty(categoryCode)) {
            //分类编码放入cuno中，分类角度放入domainz中
            msgOptions.setCuno(categoryCode);
            msgOptions.setDomain(categoryAspect);

//            pushMessage.setMsgOptions(msgOptions);
            updateExpress.set(PushMessageField.MSGOPTIONS, msgOptions.toJson());
        }

//        pushMessage.setSendStatus(ActStatus.UNACT);
        updateExpress.set(PushMessageField.SENDSTATUS, ActStatus.UNACT.getCode());

        try {
            pushMessageWebLogic.modifyPushMessage(updateExpress, pushMsgId);
        } catch (ServiceException e) {

            GAlerter.lab("there is a exception when save PushMessage : ", e);
        }

        return new ModelAndView("forward:/msg/msglist");

    }

}
