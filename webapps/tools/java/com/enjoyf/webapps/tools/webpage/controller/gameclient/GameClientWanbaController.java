package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.clientline.ClientLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/19
 * Description:
 */
@Controller
@RequestMapping(value = "/gameclient/clientline/miyou")
public class GameClientWanbaController extends ToolsBaseController {

    @Resource(name = "clientLineWebLogic")
    private ClientLineWebLogic clientLineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/page")
    public ModelAndView page(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "status", required = false, defaultValue = "") String valildStatus,
                             @RequestParam(value = "nick", required = false) String nick
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

//            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
//            if (ValidStatus.getByCode(valildStatus) != null) {
//                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, valildStatus));
//            }
//            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()));
//                    .add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.ACTING.getCode()));
            if (!StringUtil.isEmpty(nick)) {
                mapMessage.put("nick", nick);
                List<Profile> profiles = UserCenterServiceSngl.get().queryProfile(new QueryExpress().add(QueryCriterions.like(ProfileField.NICK, "%" + nick + "%")));
                Set<String> profileSet = new HashSet<String>();
                for (Profile profile : profiles) {
                    profileSet.add(profile.getProfileId());
                }
                if (!CollectionUtil.isEmpty(profileSet)) {
                    queryExpress.add(QueryCriterions.in(CommentBeanField.URI, profileSet.toArray()));
                } else {
                    return new ModelAndView("/gameclient/miyou/list", mapMessage);
                }
            }
            if (!StringUtil.isEmpty(valildStatus)) {
                queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, valildStatus));
            }

            queryExpress.add(QuerySort.add(CommentBeanField.CREATE_TIME, QuerySortOrder.DESC));
            PageRows<CommentBean> itemPageRows = CommentServiceSngl.get().queryCommentBeanByPage(queryExpress, pagination);
            if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                mapMessage.put("list", itemPageRows.getRows());
                mapMessage.put("page", itemPageRows.getPage());
                Set<String> profileIdS = new HashSet<String>();
                for (CommentBean commentBean : itemPageRows.getRows()) {
                    profileIdS.add(commentBean.getUri());
                }
                Map<String, Profile> profiles = UserCenterServiceSngl.get().queryProfiles(profileIdS);

                mapMessage.put("profiles", profiles.values());
            } else {
                mapMessage.put("page", pagination);
            }

            mapMessage.put("status", valildStatus);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameclient/miyou/list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        return new ModelAndView("gameclient/miyou/createpage", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "nick", required = false) String nick,
                               @RequestParam(value = "pic", required = false) MultipartFile pic) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);

            if (profile == null) {
                mapMessage.put("errorMsg", "该昵称不存在");
                return new ModelAndView("gameclient/miyou/createpage", mapMessage);
            }

            String tempDir = "/opt/uploads/temp/image/";
            if (!FileUtil.isFileOrDirExist(tempDir)) {
                boolean bVal = FileUtil.createDirectory(tempDir);
                if (bVal) {
                    System.out.println("not create temp dir");
                }
            }
            String fileName = tempDir + System.currentTimeMillis() + pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf("."), pic.getOriginalFilename().length());

            try {
                FileUtil.createFile(fileName, pic.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
//                String url = "http://up001.joyme.dev/json/upload/profilepic";
                String url = "http://up001." + WebappConfig.get().getDomain() + "/json/upload/profilepic";
                HttpResult result = new HttpClientManager().postMultipart(url,
                        new HttpParameter[]{
                                new HttpParameter("picfile", new File(fileName)),
                                new HttpParameter("token", "joymeplatform"),
                                new HttpParameter("profileid", profile.getProfileId())
                        });

                if (result.getReponseCode() != 200) {
                    //todo
                    return new ModelAndView("redirect:/gameclient/clientline/miyou/page", mapMessage);
                }

                JSONObject jsonObject = JSONObject.fromObject(result.getResult());

                String picurl = (String) ((JSONObject) jsonObject.get("result")).get("pic");

                CommentBean commentBean = new CommentBean();
                commentBean.setUniqueKey(UUID.randomUUID().toString());
                commentBean.setUri(profile.getProfileId());
                commentBean.setDomain(CommentDomain.GAMECLIENT_MIYOU);
                commentBean.setCommentId(Md5Utils.md5(commentBean.getUniqueKey() + CommentDomain.GAMECLIENT_MIYOU.getCode()));
                commentBean.setPic(picurl);
                commentBean.setTitle(profile.getNick());
                commentBean.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                commentBean = CommentServiceSngl.get().createCommentBean(commentBean);

            } finally {
                FileUtil.deleteFileOrDir(fileName);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        } catch (FileNotFoundException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("redirect:/gameclient/clientline/miyou/page", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String sort(@PathVariable(value = "sort") String sort,
                       @RequestParam(value = "lineid", required = false) Long lineId,
                       @RequestParam(value = "itemid", required = false) Long itemId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if (lineId == null || itemId == null) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            return binder.toJson(resultObjectMsg);
        }
        Long returnItemId = null;
        try {
            returnItemId = ClientLineWebLogic.sort(sort, lineId, itemId);
            if (returnItemId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("itemid", itemId);
        mapMessage.put("returnitemid", returnItemId);
        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView deleteLine(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int pageSize,
                                   @RequestParam(value = "status", required = false, defaultValue = "") String valildStatus,
                                   @RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "relstatus", required = false) String reStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
//        if (itemId == null) {
//            return new ModelAndView("redirect:/gameclient/clientline/miyou/page");
//        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(CommentBeanField.REMOVE_STATUS, valildStatus);
        try {
            boolean bool = CommentServiceSngl.get().modifyCommentBeanById(id, updateExpress);
//            if (bool) {
//                ToolsLog log = new ToolsLog();
//
//                log.setOpUserId(getCurrentUser().getUserid());
//                log.setOperType(LogOperType.DELETE_CLIENT_LINE_ITEM);
//                log.setOpTime(new Date());
//                log.setOpIp(getIp());
//                log.setOpAfter("clientLineItemId:" + itemId);
//
//                addLog(log);
//            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        //
        return new ModelAndView("redirect:/gameclient/clientline/miyou/page?status=" + reStatus);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView modifyLine(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
            @RequestParam(value = "status", required = false, defaultValue = "") String valildStatus,
            @RequestParam(value = "itemid", required = false) Long itemId,
            @RequestParam(value = "lineid", required = false) Long lineId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (itemId == null) {
            return new ModelAndView("redirect:/gameclient/clientline/miyou/page");
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_CLIENT_LINE_ITEM);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("clientLineItemId:" + itemId);

                addLog(log);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        //
        return new ModelAndView("redirect:/gameclient/clientline/miyou/page?pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&status=" + valildStatus);
    }

    @RequestMapping(value = "/batchupdate")
    public ModelAndView delete(@RequestParam(value = "itemid", required = false) String itemId,
                               @RequestParam(value = "updatestatuscode", required = false) String code,
                               @RequestParam(value = "status", required = false) String status) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String[] itemString = itemId.split("\\@");
            for (int i = 0; i < itemString.length; i++) {
                CommentServiceSngl.get().modifyCommentBeanById(itemString[i], new UpdateExpress().set(CommentBeanField.REMOVE_STATUS, code));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("redirect:/gameclient/clientline/miyou/page?status=" + status);
    }

    @ResponseBody
    @RequestMapping(value = "/refresh")
    public String refresh() {

        JSONObject jsonObject = new JSONObject();
        try {
            CommentServiceSngl.get().refreshCommentBeanCache();

        } catch (ServiceException e) {
        }
        return jsonObject.toString();
    }

}
