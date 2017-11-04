package com.enjoyf.webapps.tools.webpage.controller.gameclient.tag;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppService;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.*;
import com.enjoyf.platform.service.naming.RegistrantId;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhimingli
 * Date: 2014/12/18
 * Time: 17:53
 */
@Controller
@RequestMapping(value = "/gameclient/tag/dede")
public class TagDedearchivesController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "tagid", required = false, defaultValue = "") long tagid,
                             @RequestParam(value = "removestaus", required = false) String removestaus,
                             @RequestParam(value = "title", required = false, defaultValue = "") String title,
                             @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, tagid));

            if (!StringUtil.isEmpty(removestaus)) {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
            } else {
                queryExpress.add(QueryCriterions.ne(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            }
            if (!StringUtil.isEmpty(title)) {
                queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, "%" + title + "%"));
            }
            if (!StringUtil.isEmpty(platform)) {
                if (platform.equals("0")) {
                    queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, 1));
                } else if (platform.equals("1")) {
                    queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, 1));
                }
            }


            PageRows<TagDedearchives> pageRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, tagid, 0, queryExpress, pagination);
            List<TagDedearchives> list = pageRows.getRows();
            Map<Long, TagDedearchiveCheat> cheatMap = new HashMap<Long, TagDedearchiveCheat>();
            Set<String> commentIds = new HashSet<String>();
            for (int i = 0; i < list.size(); i++) {
                TagDedearchives dedearchives = list.get(i);
                if (ArchiveContentType.MIYOU_COMMENT.getCode() == dedearchives.getArchiveContentType().getCode()) {
                    commentIds.add(dedearchives.getDede_archives_id());
                    continue;
                }
                list.get(i).setDede_archives_pubdate_str(longDatetoStr(dedearchives.getDede_archives_pubdate()));
                TagDedearchiveCheat cheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(Long.valueOf(dedearchives.getDede_archives_id()));
                if (cheat != null) {
                    cheatMap.put(Long.valueOf(dedearchives.getDede_archives_id()), cheat);
                }
            }

            Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIds);
            mapMessage.put("commentBeanMap", commentBeanMap);
            mapMessage.put("title", title);
            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("tagid", tagid);
            mapMessage.put("cheatMap", cheatMap);
            mapMessage.put("removestaus", removestaus);
            mapMessage.put("platform", platform);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/dedelist", mapMessage);
        }

        return new ModelAndView("/gameclient/tag/dedelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/gameclient/tag/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tag_name", required = false) String tag_name,
                               @RequestParam(value = "type", required = false) Integer type,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/taglist", mapMessage);
        }
        return new ModelAndView("redirect:/gameclient/tag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "tagid", required = false, defaultValue = "") long tagid,
                                   @RequestParam(value = "removestaus", required = false, defaultValue = "valid") String removestaus,
                                   @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                                   @RequestParam(value = "startRowIdx", required = false, defaultValue = "0") int startRowIdx,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("title", title);
        mapMessage.put("tagid", tagid);
        mapMessage.put("removestaus", removestaus);
        mapMessage.put("platform", platform);
        mapMessage.put("startRowIdx", startRowIdx);

        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
            TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(queryExpress);
            tagDedearchives.setDede_archives_pubdate_str(DateUtil.convert2String(tagDedearchives.getDede_archives_pubdate(), DateUtil.PATTERN_DATE_TIME));
            if (tagDedearchives != null) {
                TagDedearchiveCheat cheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(Long.valueOf(tagDedearchives.getDede_archives_id()));
                if (cheat.getRead_num() <= 0) {
                    cheat.setRead_num((int) (Math.random() * 2000) + 2000);
                }
                if (cheat.getAgree_num() <= 0) {
                    cheat.setAgree_num(cheat.getRead_num() / 100);
                }
                mapMessage.put("cheat", cheat);
            }
            mapMessage.put("tagDedearchives", tagDedearchives);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/taglist", mapMessage);
        }
        return new ModelAndView("/gameclient/tag/dedemodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "tagid", required = false) String tagid,
                               @RequestParam(value = "display_type", required = false) Integer display_type,
                               @RequestParam(value = "agree_num", required = false) String agree_num,
                               @RequestParam(value = "read_num", required = false) String read_num,
                               @RequestParam(value = "agree_percent", required = false) String agree_percent,
                               @RequestParam(value = "dede_archives_id", required = false) String dede_archives_id,
                               @RequestParam(value = "dede_archives_title", required = false) String dede_archives_title,
                               @RequestParam(value = "dede_archives_pubdate", required = false) String dede_archives_pubdate,
                               @RequestParam(value = "dede_archives_description", required = false) String dede_archives_description,
                               @RequestParam(value = "dede_archives_showios", required = false) String dede_archives_showios,
                               @RequestParam(value = "dede_archives_showandroid", required = false) String dede_archives_showandroid,

                               @RequestParam(value = "removestaus", required = false, defaultValue = "valid") String removestaus,
                               @RequestParam(value = "title", required = false, defaultValue = "") String title,
                               @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                               @RequestParam(value = "startRowIdx", required = false, defaultValue = "0") int startRowIdx,
                               HttpServletRequest request) {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            long pubdate = 0l;
            updateExpress.set(TagDedearchivesFiled.TAG_DISPLYTYPE, display_type);
            updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, dede_archives_title);
            updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_DESCRIPTION, dede_archives_description);
            if (!StringUtil.isEmpty(dede_archives_pubdate)) {
                pubdate = DateUtil.formatStringToDate(dede_archives_pubdate, DateUtil.PATTERN_DATE_TIME).getTime();
                updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, pubdate);
            }
            if (!StringUtil.isEmpty(dede_archives_showios)) {
                updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, Integer.valueOf(dede_archives_showios));
            }
            if (!StringUtil.isEmpty(dede_archives_showandroid)) {
                updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, Integer.valueOf(dede_archives_showandroid));
            }
            if (!StringUtil.isEmpty(read_num)) {
                UpdateExpress up = new UpdateExpress();

                up.set(TagDedearchiveCheatFiled.READ_NUM, Integer.valueOf(read_num));
                up.set(TagDedearchiveCheatFiled.AGREE_NUM, Integer.valueOf(agree_num));
                up.set(TagDedearchiveCheatFiled.AGREE_PERCENT, Double.valueOf(agree_percent));

                JoymeAppServiceSngl.get().modifyTagDedearchiveCheat(Long.valueOf(dede_archives_id), up);
            }
            boolean bval = JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), dede_archives_id, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
            if (bval) {
                writeToolsLog(LogOperType.TAG_ARCHIVE_UPDATE, "玩霸文章:,tagid:" + tagid + ",archiveid:" + id);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(
            HttpServletRequest request,
            @RequestParam(value = "tagid", required = true) Long tagid,
            @RequestParam(value = "archiveId", required = true) String archiveId,
            @RequestParam(value = "updatestatus", required = true) String updatestatus,
            @RequestParam(value = "platform", required = true) String platform,
            @RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "pubdate", required = false, defaultValue = "0") Long dede_archives_pubdate,
            @RequestParam(value = "contenttype", required = false) int contenttype,
            @RequestParam(value = "removestaus", required = false) String removestaus,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "startRowIdx", required = false, defaultValue = "0") int startRowIdx
    ) {
        HashMap map = new HashMap();
        map.put("tagid", tagid);
        map.put("platform", platform);
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(updatestatus).getCode());
            if (updatestatus.equals(ValidStatus.VALID.getCode())) {
                updateExpress.set(TagDedearchivesFiled.DISPLAY_ORDER, System.currentTimeMillis());
            }
            JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), archiveId, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);


            UpdateExpress up = new UpdateExpress();
            if (updatestatus.equals("removed") || updatestatus.equals("invalid")) {
                up.increase(AnimeTagField.TOTAL_SUM, -1);
            } else {
                up.increase(AnimeTagField.TOTAL_SUM, 1);
            }
            try {
                boolean bval = JoymeAppServiceSngl.get().modifyAnimeTag(tagid, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid)), up);

//                    //如果是发布，需要去修改cms文章的时间
//                    if (bval && dede_archives_pubdate > 0 && ValidStatus.getByCode(removestaus).equals(ValidStatus.VALID)) {
//                        DedeArchives dedeArchives = new DedeArchives();
//                        dedeArchives.setId(Integer.valueOf(archiveId));
//                        dedeArchives.setPubdate((int) (dede_archives_pubdate / 1000));
//                        JoymeAppServiceSngl.get().modifyDedeArchivePubdateById(dedeArchives);
//                    }
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            writeToolsLog(LogOperType.TAG_ARCHIVE_UPDATE, "玩霸文章:,tagid:" + tagid + ",archiveid:" + id + ",removestaus=" + updatestatus);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx);
    }

    @ResponseBody
    @RequestMapping(value = "/sort")
    public String sort(@RequestParam(value = "desc", required = false) String desc,
                       @RequestParam(value = "id", required = false) String id,
                       @RequestParam(value = "archiveId", required = false) String archiveId,
                       @RequestParam(value = "title", required = false) String title,
                       @RequestParam(value = "tagid", required = false) String tagid,
                       @RequestParam(value = "removestaus", required = false) String removestaus,
                       @RequestParam(value = "platform", required = false) String platform,
                       @RequestParam(value = "querysort", required = false) String querysort,
                       @RequestParam(value = "startRowIdx", required = false, defaultValue = "0") int startRowIdx,
                       @RequestParam(value = "display_order", required = false) String display_order) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            mapMessage.put("tagid", tagid);
            mapMessage.put("removestaus", removestaus);
            mapMessage.put("platform", platform);
            if (StringUtil.isEmpty(querysort)) {
                UpdateExpress updateExpress1 = new UpdateExpress();
                UpdateExpress updateExpress2 = new UpdateExpress();
                QueryExpress queryExpress = new QueryExpress();
                //第一个
                TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, id)));
                if (desc.equals("up")) {
                    queryExpress.add(QueryCriterions.gt(TagDedearchivesFiled.DISPLAY_ORDER, tagDedearchives.getDisplay_order()))
                            .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.ASC))
                            .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, Long.valueOf(tagid)));
                    if (!StringUtil.isEmpty(removestaus)) {
                        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
                    }

                    if (!StringUtil.isEmpty(platform)) {
                        if (platform.equals("0")) {
                            queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, 1));
                        } else if (platform.equals("1")) {
                            queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, 1));
                        }
                    }
                } else {
                    queryExpress.add(QueryCriterions.lt(TagDedearchivesFiled.DISPLAY_ORDER, tagDedearchives.getDisplay_order()))
                            .add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.DESC))
                            .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, Long.valueOf(tagid)));
                    if (!StringUtil.isEmpty(removestaus)) {
                        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(removestaus).getCode()));
                    }

                    if (!StringUtil.isEmpty(platform)) {
                        if (platform.equals("0")) {
                            queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, 1));
                        } else if (platform.equals("1")) {
                            queryExpress.add(QueryCriterions.like(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, 1));
                        }
                    }
                }

                //第二个
                PageRows<TagDedearchives> appRows = JoymeAppServiceSngl.get().queryTagDedearchivesByPage(true, Long.valueOf(tagid), 0, queryExpress, new Pagination(1, 1, 1));

                if (appRows != null && !CollectionUtil.isEmpty(appRows.getRows())) {

                    updateExpress1.set(TagDedearchivesFiled.DISPLAY_ORDER, tagDedearchives.getDisplay_order());
                    JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), String.valueOf(archiveId), new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, appRows.getRows().get(0).getId())), updateExpress1, ArchiveRelationType.TAG_RELATION);

                    updateExpress2.set(TagDedearchivesFiled.DISPLAY_ORDER, appRows.getRows().get(0).getDisplay_order());
                    JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), String.valueOf(archiveId), new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, tagDedearchives.getId())), updateExpress2, ArchiveRelationType.TAG_RELATION);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                if (tagDedearchives != null && appRows != null && appRows.getRows() != null && appRows.getRows().get(0) != null) {
                    JSONObject result = new JSONObject();
                    result.put("oneid", tagDedearchives.getId());
                    result.put("twoid", appRows.getRows().get(0).getId());
                    jsonObject.put("result", result);
                }
                return jsonObject.toString();
            } else {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(TagDedearchivesFiled.DISPLAY_ORDER, Long.valueOf(display_order));
                JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), archiveId, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
            }
            writeToolsLog(LogOperType.TAG_ARCHIVE_SORT, "玩霸文章:,tagid:" + tagid + ",archiveid:" + id + "," + desc);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping(value = "/modifysort")
    public ModelAndView modifySort(@RequestParam(value = "desc", required = false) String desc,
                             @RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "archiveId", required = false) String archiveId,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "tagid", required = false) String tagid,
                             @RequestParam(value = "removestaus", required = false) String removestaus,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "querysort", required = false) String querysort,
                             @RequestParam(value = "startRowIdx", required = false, defaultValue = "0") int startRowIdx,
                             @RequestParam(value = "display_order", required = false) String display_order) {
        try {
            if(!StringUtil.isEmpty(display_order)){
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(TagDedearchivesFiled.DISPLAY_ORDER, Long.valueOf(display_order));
                JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), archiveId, queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);

                writeToolsLog(LogOperType.TAG_ARCHIVE_SORT, "玩霸文章:,tagid:" + tagid + ",archiveid:" + id + "," + desc);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/list?platform=" + platform + "&removestaus=" + removestaus + "&title=" + title + "&tagid=" + tagid + "&pager.offset=" + startRowIdx);
    }

    @RequestMapping(value = "/createreplypage")
    public ModelAndView replayPage(@RequestParam(value = "unikey", required = false) String unikey,
                                   @RequestParam(value = "tagid", required = false) Long tagid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey)));
            if (commentBean == null) {
                TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, unikey)));
                if (tagDedearchives == null) {

                    return new ModelAndView("redirect:/gameclient/tag/dede/list?tagid=" + tagid, mapMessage);
                }
                String commentId = CommentUtil.genCommentId(unikey, CommentDomain.CMS_COMMENT);
                commentBean = new CommentBean();
                commentBean.setUri(tagDedearchives.getDede_archives_url());
                commentBean.setTitle(tagDedearchives.getDede_archives_title());
                commentBean.setDomain(CommentDomain.CMS_COMMENT);
                commentBean.setUniqueKey(unikey);
                commentBean.setCommentId(commentId);
                commentBean.setCreateTime(new Date());
                commentBean.setRemoveStatus(ActStatus.UNACT);
                CommentServiceSngl.get().createCommentBean(commentBean);
            }
            mapMessage.put("commentBean", commentBean);
            mapMessage.put("tagid", tagid);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameclient/tag/dede/list", mapMessage);
        }
        return new ModelAndView("/gameclient/tag/createreplypage", mapMessage);
    }


    @RequestMapping(value = "/createreply")
    public ModelAndView createReplay(HttpServletRequest request, @RequestParam(value = "unikey", required = false) String unikey,
                                     @RequestParam(value = "nick", required = false) String nick,
                                     @RequestParam(value = "text", required = false) String text,
                                     @RequestParam(value = "textare", required = false) String textare,
                                     @RequestParam(value = "appkey", defaultValue = "17yfn24TFexGybOF0PqjdY") String appkey,
                                     @RequestParam(value = "tagid", required = false) Long tagid,
                                     @RequestParam(value = "pid", required = false) String pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.UNI_KEY, unikey)));
            mapMessage.put("commentBean", commentBean);
            if (profile == null) {
                mapMessage.put("errorMsg", "profile.has.notexists");
                mapMessage.put("nick", nick);
                mapMessage.put("textare", textare);
                mapMessage.put("tagid", tagid);
                return new ModelAndView("/gameclient/tag/createreplypage", mapMessage);
            }

            ReplyBody replyBody = ReplyBody.parse(postreply(text));
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                mapMessage.put("errorMsg", "error.viewline.item.input.wrong.data");
                mapMessage.put("nick", nick);
                mapMessage.put("textare", textare);
                mapMessage.put("tagid", tagid);

                return new ModelAndView("/gameclient/tag/createreplypage", mapMessage);
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(commentBean.getCommentId());

            reply.setReplyUno(profile.getUno());
            reply.setReplyProfileId(profile.getProfileId());
            reply.setReplyProfileKey(authApp.getProfileKey());
            if (!StringUtil.isEmpty(pid)) {
                CommentReply replay = CommentServiceSngl.get().getCommentReplyById(commentBean.getCommentId(), Long.parseLong(pid));
                if (replay != null) {
                    if (replay.getParentId() == 0) {
                        reply.setParentId(replay.getReplyId());
                        reply.setParentUno(replay.getReplyUno());
                        reply.setParentProfileId(replay.getReplyProfileId());
                        reply.setParentProfileKey(replay.getRootProfileKey());

                        reply.setRootId(replay.getReplyId());
                        reply.setRootUno(replay.getReplyUno());
                        reply.setRootProfileId(replay.getReplyProfileId());
                        reply.setRootProfileKey(replay.getRootProfileKey());
                        reply.setFloorNum(commentBean.getTotalRows() + 1);//todo 以后删掉
                    } else {
                        reply.setParentId(replay.getReplyId());
                        reply.setParentUno(replay.getReplyUno());
                        reply.setParentProfileId(replay.getReplyProfileId());
                        reply.setParentProfileKey(replay.getRootProfileKey());

                        reply.setRootId(replay.getRootId());
                        reply.setRootUno(replay.getRootUno());
                        reply.setRootProfileId(replay.getRootProfileId());
                        reply.setRootProfileKey(replay.getRootProfileKey());
                        reply.setFloorNum(replay.getTotalRows() + 1);//todo 以后删掉
                    }
                } else {
                    mapMessage.put("errorMsg", "reply.rootid.is.null");
                    mapMessage.put("nick", nick);
                    mapMessage.put("textare", textare);
                    mapMessage.put("tagid", tagid);
                    mapMessage.put("pid", pid);
                    return new ModelAndView("/gameclient/tag/createreplypage", mapMessage);
                }
            } else {
                reply.setSubKey(commentBean.getUniqueKey());
                reply.setFloorNum(commentBean.getTotalRows() + 1);
            }

            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp());
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);

            reply.setDomain(CommentDomain.CMS_COMMENT);
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameclient/tag/dede/list?tagid=" + tagid, mapMessage);
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/list?tagid=" + tagid);
    }


    @RequestMapping(value = "/batchpublish")
    public ModelAndView batchPublish(@RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") int pageStartIndex,
                                     @RequestParam(value = "tagid", required = false, defaultValue = "") long tagid,
                                     @RequestParam(value = "removestaus", required = false, defaultValue = "valid") String removestaus,
                                     @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                     @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                                     @RequestParam(value = "ids", required = false) String idStr,
                                     @RequestParam(value = "updatestaus", required = false) String updateStatus,
                                     HttpServletRequest request
    ) {
        try {
            Map<String, String> idMap = new HashMap<String, String>();
            if (idStr.indexOf(",") > 0) {
                String[] idArr = idStr.split(",");
                for (String id : idArr) {
                    idMap.put(id.split("_")[0], id.split("_")[1]);
                }
            } else {
                if (!StringUtil.isEmpty(idStr)) {
                    idMap.put(idStr.split("_")[0], idStr.split("_")[1]);
                }
            }
            for (String id : idMap.keySet()) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.getByCode(updateStatus).getCode());
                if (updateStatus.equals(ValidStatus.VALID.getCode())) {
                    updateExpress.set(TagDedearchivesFiled.DISPLAY_ORDER, System.currentTimeMillis());
                }
                boolean bool = JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), idMap.get(id), queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);

                if (bool) {
                    UpdateExpress up = new UpdateExpress();
                    if (updateStatus.equals("removed") || updateStatus.equals("invalid")) {
                        up.increase(AnimeTagField.TOTAL_SUM, -1);
                    } else {
                        up.increase(AnimeTagField.TOTAL_SUM, 1);
                    }

                    boolean bval = JoymeAppServiceSngl.get().modifyAnimeTag(tagid, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid)), up);

                    //                    //如果是发布，需要去修改cms文章的时间
                    //                    if (bval && dede_archives_pubdate > 0 && ValidStatus.getByCode(removestaus).equals(ValidStatus.VALID)) {
                    //                        DedeArchives dedeArchives = new DedeArchives();
                    //                        dedeArchives.setId(Integer.valueOf(archiveId));
                    //                        dedeArchives.setPubdate((int) (dede_archives_pubdate / 1000));
                    //                        JoymeAppServiceSngl.get().modifyDedeArchivePubdateById(dedeArchives);
                    //                    }
                    writeToolsLog(LogOperType.TAG_ARCHIVE_UPDATE, "玩霸文章:,tagid:" + tagid + ",archiveid:" + id + ",removestaus=" + updateStatus);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/list?tagid=" + tagid + "&removestaus=" + removestaus + "&title=" + title + "&platform=" + platform + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/list?tagid=" + tagid + "&removestaus=" + removestaus + "&title=" + title + "&platform=" + platform + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/batchtimer")
    public ModelAndView batchTimer(@RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") int pageStartIndex,
                                   @RequestParam(value = "tagid", required = false, defaultValue = "") long tagid,
                                   @RequestParam(value = "removestaus", required = false, defaultValue = "valid") String removestaus,
                                   @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                                   @RequestParam(value = "ids", required = false) String idStr,
                                   @RequestParam(value = "pulishtime", required = false) String publishTimeStr,
                                   HttpServletRequest request
    ) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            Date publishDate = null;
            if (!StringUtil.isEmpty(publishTimeStr)) {
                publishDate = df.parse(publishTimeStr);
            }

            Set<String> idSet = new HashSet<String>();
            Set<String> archiveIdSet = new HashSet<String>();
            if (idStr.indexOf(",") > 0) {
                String[] idArr = idStr.split(",");
                for (String id : idArr) {
                    idSet.add(id.split("_")[0]);
                    archiveIdSet.add(id.split("_")[1]);
                }
            } else {
                if (!StringUtil.isEmpty(idStr)) {
                    idSet.add(idStr.split("_")[0]);
                    archiveIdSet.add(idStr.split("_")[1]);
                }
            }
            JoymeAppServiceSngl.get().addTagDedearchivesTimerTask(tagid, idSet, publishDate);
            writeToolsLog(LogOperType.TAG_ARCHIVE_UPDATE, "玩霸文章定时发布:tagid:" + tagid + ",archiveid:" + archiveIdSet.toArray());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/timerlist?tagid=" + tagid + "&removestaus=" + removestaus + "&title=" + title + "&platform=" + platform + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/timerlist?tagid=" + tagid + "&removestaus=" + removestaus + "&title=" + title + "&platform=" + platform + "&pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/timerlist")
    public ModelAndView timerList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                  @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                  @RequestParam(value = "tagid", required = false) long tagid,
                                  @RequestParam(value = "errorMsg", required = false) String errorMsg,
                                  HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("tagid", tagid);
        mapMessage.put("errorMsg", errorMsg);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            PageRows<TagDedearchives> pageRows = JoymeAppServiceSngl.get().queryTagDedearchivesTimerList(tagid, pagination);
            if (pageRows != null) {
                Map<Long, TagDedearchiveCheat> cheatMap = new HashMap<Long, TagDedearchiveCheat>();
                Set<String> commentIds = new HashSet<String>();
                for (TagDedearchives tagDedearchives : pageRows.getRows()) {
                    TagDedearchives dedearchives = tagDedearchives;
                    if (ArchiveContentType.MIYOU_COMMENT.getCode() == dedearchives.getArchiveContentType().getCode()) {
                        commentIds.add(dedearchives.getDede_archives_id());
                        continue;
                    }
                    tagDedearchives.setDede_archives_pubdate_str(longDatetoStr(dedearchives.getDede_archives_pubdate()));
                    TagDedearchiveCheat cheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(Long.valueOf(dedearchives.getDede_archives_id()));
                    if (cheat != null) {
                        cheatMap.put(Long.valueOf(dedearchives.getDede_archives_id()), cheat);
                    }
                }

                Map<String, CommentBean> commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIds);
                mapMessage.put("commentBeanMap", commentBeanMap);
                mapMessage.put("cheatMap", cheatMap);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameclient/tag/timerlist", mapMessage);
        }
        return new ModelAndView("/gameclient/tag/timerlist", mapMessage);
    }

    @RequestMapping(value = "/deltimer")
    public ModelAndView delTimer(@RequestParam(value = "tagid", required = false) long tagid,
                                 @RequestParam(value = "ids", required = false) String ids,
                                 @RequestParam(value = "timerdate", required = false) String timerDateStr,
                                 @RequestParam(value = "pageStartIndex", required = false) int pageStartIndex,
                                 HttpServletRequest request
    ) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            Date publishDate = null;
            if (!StringUtil.isEmpty(timerDateStr)) {
                publishDate = df.parse(timerDateStr);
            }

            Set<String> idSet = new HashSet<String>();
            if (ids.indexOf(",") > 0) {
                String[] idArr = ids.split(",");
                for (String id : idArr) {
                    idSet.add(id);
                }
            } else {
                if (!StringUtil.isEmpty(ids)) {
                    idSet.add(ids);
                }
            }
            JoymeAppServiceSngl.get().delTagDedearchivesTimerTaskCache(tagid, idSet, publishDate);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/gameclient/tag/dede/timerlist?tagid=" + tagid + "&pager.offset=" + pageStartIndex + "&errorMsg=system.error");
        }
        return new ModelAndView("redirect:/gameclient/tag/dede/timerlist?tagid=" + tagid + "&pager.offset=" + pageStartIndex);
    }

    private String longDatetoStr(Long longdate) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(longdate);
    }


    private String postreply(String text) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(text);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_POST_REPLY);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

    public static void main(String[] args) {


        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("22222222222222222222");
            }
        };
        Timer timer2 = new Timer();
        timer2.schedule(timerTask2, 2000);

    }

}
