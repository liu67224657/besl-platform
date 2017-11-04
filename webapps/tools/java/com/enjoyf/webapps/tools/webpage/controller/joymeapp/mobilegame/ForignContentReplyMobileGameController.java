package com.enjoyf.webapps.tools.webpage.controller.joymeapp.mobilegame;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.processor.ReplaceLineBreakProcessor;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-10
 * Time: 下午5:29
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/mobilegame/reply")
public class ForignContentReplyMobileGameController extends ToolsBaseController {
    private static ReplaceLineBreakProcessor replaceLineBreakProcessor = new ReplaceLineBreakProcessor("<br/>");


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "id") String id,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "removeStaus", required = false) String removeStaus,
                             @RequestParam(value = "word", required = false) String word
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            ForignContentDomain forignContentDomain = ForignContentDomain.SHORT_COMMENTS;
            if (type.equals("gag")) {
                forignContentDomain = ForignContentDomain.GAG;
            }
            ForignContent forignContent = ContentServiceSngl.get().getForignContentByFidCdomain(id, forignContentDomain);
            if (forignContent != null) {
                QueryExpress replyqueryExpress = new QueryExpress();
                replyqueryExpress.add(QuerySort.add(ForignContentField.DISPLAY_ORDER, QuerySortOrder.ASC));
                replyqueryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_ID, forignContent.getContentId()));
                replyqueryExpress.add(QueryCriterions.ne(ForignContentReplyField.BODY, ""));
                if (!StringUtil.isEmpty(removeStaus)) {
                    replyqueryExpress.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.getByCode(removeStaus).getCode()));
                }
                if (!StringUtil.isEmpty(word)) {
                    replyqueryExpress.add(QueryCriterions.like(ForignContentReplyField.BODY, "%" + word + "%"));
                }
                mapMessage.put("contentid", forignContent.getContentId());
                mapMessage.put("forignContent", forignContent);
                PageRows<ForignContentReply> forignContentReplyPageRows = ContentServiceSngl.get().queryForignContentReplyByPage(replyqueryExpress, pagination);
                if (forignContentReplyPageRows != null) {

                    //查询用户的昵称
                    Set<String> unos = new HashSet<String>();
                    for (ForignContentReply reply : forignContentReplyPageRows.getRows()) {
                        unos.add(reply.getReplyUno());
                    }
                    //TODO 删除
                    Map<String, AccountVirtual> returnAccountVirtualMap = null;//JoymeAppServiceSngl.get().queryAccountVirtualByUnos(unos);


                    List<ForignContentReply> mapList = new ArrayList<ForignContentReply>();
                    for (ForignContentReply reply : forignContentReplyPageRows.getRows()) {
                        reply.setReplyUno(returnAccountVirtualMap.get(reply.getReplyUno()).getScreenname());
                        mapList.add(reply);
                    }

                    mapMessage.put("list", mapList);
                    mapMessage.put("page", forignContentReplyPageRows.getPage());
                }


            }
            mapMessage.put("id", id);
            mapMessage.put("type", type);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/mobilegame/reply/mobilegamereplylist", mapMessage);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "replyid", required = false) long replyid,
                               @RequestParam(value = "del", required = false) String del,
                               @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "type", required = false) String deltype,
                               @RequestParam(value = "listtype", required = false) String listtype,
                               @RequestParam(value = "contentid", required = false) String contentid) {
        UpdateExpress updateExpress = new UpdateExpress();
        try {
            //删除or恢复
            if (StringUtil.isEmpty(deltype)) {
                if (del.equals("y")) {//删除
                    updateExpress.set(ForignContentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode());
                } else {
                    updateExpress.set(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                }
            } else {
                //置顶or取消置顶
                if (deltype.equals("down")) {
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                    queryExpress.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
                    queryExpress.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, Long.valueOf(contentid)));
                    queryExpress.add(QueryCriterions.gt(ForignContentReplyField.DISPLAY_ORDER, 0L));
                    PageRows<ForignContentReply> forignContentReplyPageRows = ContentServiceSngl.get().queryForignContentReplyByPage(queryExpress, new Pagination(1, 1, 1));
                    long display_order = Integer.MAX_VALUE;
                    if (!CollectionUtil.isEmpty(forignContentReplyPageRows.getRows())) {
                        display_order = forignContentReplyPageRows.getRows().get(0).getDisplay_order() - 1;
                    }
                    updateExpress.set(ForignContentReplyField.DISPLAY_ORDER, display_order);
                } else {
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                    queryExpress.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
                    queryExpress.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, Long.valueOf(contentid)));
                    queryExpress.add(QueryCriterions.lt(ForignContentReplyField.DISPLAY_ORDER, 0L));
                    PageRows<ForignContentReply> forignContentReplyPageRows = ContentServiceSngl.get().queryForignContentReplyByPage(queryExpress, new Pagination(1, 1, 1));
                    long display_order = -1;
                    if (!CollectionUtil.isEmpty(forignContentReplyPageRows.getRows())) {
                        display_order = forignContentReplyPageRows.getRows().get(0).getDisplay_order() - 1;
                    }
                    updateExpress.set(ForignContentReplyField.DISPLAY_ORDER, display_order);
                }
            }

            ForignContentDomain forignContentDomain = ForignContentDomain.SHORT_COMMENTS;
            if (listtype.equals("gag")) {
                forignContentDomain = ForignContentDomain.GAG;
            }
            ContentServiceSngl.get().modifyForignContentReply(replyid, updateExpress, Long.valueOf(contentid), forignContentDomain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/mobilegame/reply/list?id=" + id + "&pager.offset=" + pageStartIndex + "&type=" + listtype);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createpage(@RequestParam(value = "id") String id,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "contentid", required = false) String contentid
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("id", id);
        mapMessage.put("type", type);
        mapMessage.put("contentid", contentid);
        return new ModelAndView("/joymeapp/mobilegame/reply/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                               @RequestParam(value = "type", required = false) String listtype,
                               @RequestParam(value = "contentid", required = false) String contentid,
                               @RequestParam(value = "screenname", required = false) String screenname,
                               @RequestParam(value = "body", required = false) String body,
                               @RequestParam(value = "score", required = false) String score,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        try {

            mapMessage.put("id", id);
            mapMessage.put("type", listtype);
            mapMessage.put("contentid", contentid);
            mapMessage.put("screenname", screenname);
            mapMessage.put("body", body);
            mapMessage.put("score", score);
            //敏感词
            if (ContextFilterUtils.checkSimpleEditorBlackList(body)) {
                mapMessage.put("errorMsg", "评论中含有不适当的内容！");
                return new ModelAndView("/joymeapp/mobilegame/reply/createpage", mapMessage);
            }

            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.SCREENNAME, screenname.trim()));
            AccountVirtual accountVirtual = null;//JoymeAppServiceSngl.get().getAccountVirtual(queryExpress);
            if (accountVirtual == null) {
                mapMessage.put("errorMsg", "用户不存在");
                return new ModelAndView("/joymeapp/mobilegame/reply/createpage", mapMessage);
            }

            String ip = getIp(request);
            ForignContentReply reply = new ForignContentReply();
            reply.setReplyUno(accountVirtual.getUno());

            //去除换行
            ResolveContent resolveContent = new ResolveContent();
            resolveContent.setContent(body.trim());
            reply.setBody(replaceLineBreakProcessor.process(resolveContent).getContent());

            reply.setContentId(Long.valueOf(contentid));
            reply.setCreateTime(new Date());
            reply.setCreateIp(ip);
            if (!StringUtil.isEmpty(score)) {
                reply.setScore(Double.valueOf(score));
            }

            QueryExpress queryExpressReply = new QueryExpress();
            queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpressReply.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, Long.valueOf(contentid)));
            queryExpressReply.add(QueryCriterions.gt(ForignContentReplyField.DISPLAY_ORDER, 0L));
            PageRows<ForignContentReply> forignContentReplyPageRows = ContentServiceSngl.get().queryForignContentReplyByPage(queryExpressReply, new Pagination(1, 1, 1));
            long display_order = Integer.MAX_VALUE;
            if (!CollectionUtil.isEmpty(forignContentReplyPageRows.getRows())) {
                display_order = forignContentReplyPageRows.getRows().get(0).getDisplay_order() - 1;
            }
            reply.setDisplay_order(display_order);


            ForignContentDomain forignContentDomain = ForignContentDomain.SHORT_COMMENTS;
            if (listtype.equals("gag")) {
                forignContentDomain = ForignContentDomain.GAG;
            }
            reply.setForignContentDomain(forignContentDomain);
            ContentServiceSngl.get().postForignReply(reply);
        } catch (ServiceException e) {
            if (e.getValue() == 11014) {
                mapMessage.put("errorMsg", "发布评论时间间隔太短了");
                return new ModelAndView("/joymeapp/mobilegame/reply/createpage", mapMessage);
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/mobilegame/reply/list?id=" + id + "&pager.offset=" + pageStartIndex + "&type=" + listtype);
    }

    protected String getIp(HttpServletRequest request) {
        return HTTPUtil.getRemoteAddr(request);
    }

}
