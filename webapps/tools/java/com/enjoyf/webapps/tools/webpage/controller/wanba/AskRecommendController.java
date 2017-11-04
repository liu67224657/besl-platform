package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/25 0025.
 */

@Controller
@RequestMapping(value = "/wanba/askrecommend")
public class AskRecommendController extends ToolsBaseController {

    private static String RECOMMEND_TAGID = "-10000";//推荐


    private static String TOOLS_RECOMMEND_TOP = "_tools.recommend.top_";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "tagid", required = false, defaultValue = "-10000") String tagid,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {

            String linekey = AskUtil.getAskLineKey(tagid, WanbaItemDomain.RECOMMEND);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(WanbaItemField.OWNPROFILEID, tagid));
            queryExpress.add(QuerySort.add(WanbaItemField.SCORE, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.eq(WanbaItemField.LINEKEY, linekey));


            PageRows<WanbaItem> pageRows = AskServiceSngl.get().queryWanbaItem(queryExpress, pagination);

            List<WanbaItem> list = new ArrayList<WanbaItem>();
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {


                Set<Long> queryIdSet = new HashSet<Long>();
                for (WanbaItem item : pageRows.getRows()) {
                    if (item.getItemType().equals(ItemType.QUESTION)) {
                        list.add(item);
                        queryIdSet.add(Long.valueOf(item.getDestId()));
                    }
                }

                Map<Long, Question> questionMap = AskServiceSngl.get().queryQuestionByIds(queryIdSet);
                mapMessage.put("questionMap", questionMap);

            }
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("tagid", tagid);
            mapMessage.put("nowscore", System.currentTimeMillis());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/wanba/askrecommend/list", mapMessage);
    }


    @ResponseBody
    @RequestMapping(value = "/create")
    public String create(@RequestParam(value = "destId", required = false) String destId,
                         HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", 1);
        jsonObject.put("msg", "success");
        String linekey = AskUtil.getAskLineKey(RECOMMEND_TAGID, WanbaItemDomain.RECOMMEND);
        try {


//            String itemid = AskUtil.getAskItemId(destId, linekey, ItemType.QUESTION);
//            if (item != null) {
//                jsonObject.put("rs", WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_EXISTS.getCode());
//                jsonObject.put("msg", "问题已存在，请检查");
//                return jsonObject.toString();
//            } else {
//
//                WanbaItem item = AskServiceSngl.get().getWanbaItem(itemid);

            WanbaItem item = new WanbaItem();
            item.setOwnProfileId(RECOMMEND_TAGID);
            item.setCreateTime(new Date());
            item.setItemDomain(WanbaItemDomain.RECOMMEND);
            item.setLineKey(linekey);
            item.setValidStatus(ValidStatus.VALID);
            item.setItemType(ItemType.QUESTION);
            String destIdArr[] = destId.split(",");

            StringBuffer msgbuf = new StringBuffer();
            for (int i = 0; i < destIdArr.length; i++) {
                String tempDestId = destIdArr[i].trim();
                if (!StringUtil.isEmpty(tempDestId)) {
                    Question question = AskServiceSngl.get().getQuestion(Long.valueOf(tempDestId));
                    if (question == null) {
                        msgbuf.append(tempDestId + ",");
                        continue;
                    }
                    if (question.getRemoveStatus().equals(IntValidStatus.VALID)) {
                        msgbuf.append(tempDestId + ",");
                        continue;
                    }

                    if (question.getAcceptAnswerId() <= 0) {
                        msgbuf.append(tempDestId + ",");
                        continue;
                    }

                    WanbaItem tempitem = AskServiceSngl.get().getWanbaItem(AskUtil.getAskItemId(tempDestId, linekey, ItemType.QUESTION));
                    if (tempitem != null) {
                        msgbuf.append(tempDestId + ",");
                        continue;
                    }

                    item.setScore(System.currentTimeMillis() - i);
                    item.setDestId(tempDestId);
                    item = AskServiceSngl.get().addWanbaItem(item);

                    //如果推荐是游戏，那么就添加到游戏标签下面
                    if (question.getGameId() > 0l) {
                        String gameRecommendKey = AskUtil.getAskLineKey(String.valueOf(question.getGameId()), WanbaItemDomain.RECOMMEND);
                        WanbaItem gameRecommendItem = new WanbaItem();
                        gameRecommendItem.setOwnProfileId(String.valueOf(question.getGameId()));
                        gameRecommendItem.setItemType(ItemType.QUESTION);
                        gameRecommendItem.setCreateTime(question.getCreateTime());
                        gameRecommendItem.setItemDomain(WanbaItemDomain.RECOMMEND);
                        gameRecommendItem.setDestId(String.valueOf(question.getQuestionId()));
                        gameRecommendItem.setDestProfileId(question.getAskProfileId());
                        gameRecommendItem.setLineKey(gameRecommendKey);
                        gameRecommendItem.setScore(Double.valueOf(System.currentTimeMillis()));
                        try {
                            AskServiceSngl.get().addItemByLineKey(gameRecommendItem);
                        } catch (Exception e) {

                        }
                    }
                }
            }

            if (!StringUtil.isEmpty(msgbuf.toString())) {
                jsonObject.put("rs", WanbaResultCodeConstants.WANBA_ASK_QUESTION_OUT_EXISTS.getCode());
                jsonObject.put("msg", msgbuf.toString() + " 添加失败");
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return jsonObject.toString();
    }


    @ResponseBody
    @RequestMapping(value = "/remove")
    public String remove(@RequestParam(value = "itemid") String itemid,
                         @RequestParam(value = "tagid", required = false, defaultValue = "-10000") String tagid) {
        try {
            AskServiceSngl.get().removeItemByLineKey(itemid);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.FAILED.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping(value = "/release")
    public String release(@RequestParam(value = "itemid", required = false) String itemid,
                          @RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "tagid", required = false, defaultValue = "-10000") String tagid) {
        try {
            WanbaItem item = AskServiceSngl.get().getWanbaItem(itemid);
            if (item != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(WanbaItemField.VALIDSTATUS, ValidStatus.getByCode(status).getCode());
                AskServiceSngl.get().modifyWanbaItem(itemid, updateExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.FAILED.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "desc", required = true) String desc,
                             @RequestParam(value = "destId", required = true) String destId,
                             @RequestParam(value = "tagid", required = false, defaultValue = "-10000") String tagid) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        try {

            String linekey = AskUtil.getAskLineKey(tagid, WanbaItemDomain.RECOMMEND);
            String itemId = AskUtil.getAskItemId(destId, linekey, ItemType.QUESTION);


            //第一个
            WanbaItem wanbaItem = AskServiceSngl.get().getWanbaItem(itemId);
            if (desc.equals("up")) {
                queryExpress.add(QueryCriterions.gt(WanbaItemField.SCORE, wanbaItem.getScore()))
                        .add(QuerySort.add(WanbaItemField.SCORE, QuerySortOrder.ASC))
                        .add(QueryCriterions.eq(WanbaItemField.LINEKEY, linekey));
            } else {
                queryExpress.add(QueryCriterions.lt(WanbaItemField.SCORE, wanbaItem.getScore()))
                        .add(QuerySort.add(WanbaItemField.SCORE, QuerySortOrder.DESC))
                        .add(QueryCriterions.eq(WanbaItemField.LINEKEY, linekey));
            }

            //第二个

            PageRows<WanbaItem> pageRows = AskServiceSngl.get().queryWanbaItem(queryExpress, new Pagination(1, 1, 1));

            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {

                updateExpress1.set(WanbaItemField.SCORE, wanbaItem.getScore());
                AskServiceSngl.get().modifyWanbaItem(pageRows.getRows().get(0).getItemId(), updateExpress1);

                updateExpress2.set(WanbaItemField.SCORE, pageRows.getRows().get(0).getScore());
                AskServiceSngl.get().modifyWanbaItem(wanbaItem.getItemId(), updateExpress2);
            }

            writeToolsLog(LogOperType.TAG_ANIME_SORT, "玩霸问题游戏排序,destId:" + destId + ",orderby:" + desc);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/askrecommend/list?tagid=" + tagid, mapMessage);
    }


    @RequestMapping(value = "/recommendprofile")
    public ModelAndView recommendprofile(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<String> strings = AskServiceSngl.get().getWanbaRecommentProfiles();

            if (CollectionUtil.isEmpty(strings)) {
                strings = new ArrayList<String>();
            }

            for (int i = strings.size(); i < 10; i++) {
                strings.add("");
            }
            mapMessage.put("strings", strings);

            String msg = "";
            String success = request.getParameter("success");
            if (!StringUtil.isEmpty(success)) {
                if ("success".equals(success)) {
                    msg = "成功";
                } else {
                    msg = "失败";
                }
            }
            mapMessage.put("msg", msg);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/askrecommend/recommend_profile", mapMessage);
    }


    @RequestMapping(value = "/addrecommendprofile")
    public ModelAndView addrecommendprofile(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<String> strings = new ArrayList<String>();
            String[] guests = request.getParameterValues("pid");
            if (guests.length > 0) {
                for (int i = 0; i < guests.length; i++) {
                    String str = guests[i];
                    if (!StringUtil.isEmpty(str)) {
                        str = str.trim();
                    }
                    strings.add(str);
                }
            }
            AskServiceSngl.get().updateWanbaRecommentProfiles(strings);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/wanba/askrecommend/recommendprofile?success=fail");
        }
        return new ModelAndView("redirect:/wanba/askrecommend/recommendprofile?success=success");
    }


    //置顶
    @RequestMapping("/top")
    @ResponseBody
    public String questionDelte(@RequestParam(value = "tagid", required = false, defaultValue = "-10000") String tagid,
                                @RequestParam(value = "destId", required = false) String destId,
                                @RequestParam(value = "type", required = false) int type) {
        try {
            String linekey = AskUtil.getAskLineKey(tagid, WanbaItemDomain.RECOMMEND);
            String itemId = AskUtil.getAskItemId(destId, linekey, ItemType.QUESTION);


            //第一个
            WanbaItem wanbaItem = AskServiceSngl.get().getWanbaItem(itemId);

            //type 1置顶 2取消置顶
            if (type == 1) {

                String value = MiscServiceSngl.get().getRedisMiscValue(TOOLS_RECOMMEND_TOP);

                //4637231999000D   --->  2116-12-12 23:59:59
                double score = StringUtil.isEmpty(value) ? 4637231999000D : Double.valueOf(value);

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(WanbaItemField.SCORE, score + 1);

                AskServiceSngl.get().modifyWanbaItem(wanbaItem.getItemId(), updateExpress);

                MiscServiceSngl.get().saveRedisMiscValue(TOOLS_RECOMMEND_TOP, String.valueOf((score + 1)), -1);
            } else {


                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(WanbaItemField.SCORE, Double.valueOf(System.currentTimeMillis()));
                AskServiceSngl.get().modifyWanbaItem(wanbaItem.getItemId(), updateExpress);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
