package com.enjoyf.webapps.tools.webpage.controller.forigncontentreply;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONArray;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/15.
 */

@Controller
@RequestMapping(value = "/json/comment/vote/wiki")
public class JsonContentWikiVoteController extends ToolsBaseController {

      //用于显示一个投票下面所有的选项用于投票的编辑页面
          @RequestMapping(value = "/option/list")
          @ResponseBody
          public String optionList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                                 @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                                 @RequestParam(value = "commentId", required = true) String commentId) {
              Map<String, Object> mapMessage = new HashMap<String, Object>();
              JsonBinder binder = JsonBinder.buildNormalBinder();
              try {

                  int pageStartIndex = (page - 1) * rows;
                  int pageSize = rows;
                  QueryExpress queryExpress = new QueryExpress();

                  //只显示valid的数据，按display_order由小到大排序
                  queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.COMMENT_ID,commentId ));
              //    queryExpress.add(QueryCriterions.eq(CommentVoteOptionField.REMOVE_STATUS, ValidStatus.VALID.getCode()));

                  queryExpress.add(new QuerySort(CommentVoteOptionField.REMOVE_STATUS, QuerySortOrder.DESC));
                  queryExpress.add(new QuerySort(CommentVoteOptionField.DISPLAY_ORDER, QuerySortOrder.ASC));

                  int curPage = (pageStartIndex / pageSize) + 1;
                  Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

                  PageRows<CommentVoteOption> pageRows = CommentServiceSngl.get().queryCommentVoteOptionByPage(queryExpress, pagination);
                  int total = CommentServiceSngl.get().countCommentVoteOption(queryExpress);
                  if (pageRows != null) {
                      mapMessage.put("rows", pageRows.getRows());
                      mapMessage.put("total", total);
                  }


              } catch (Exception e) {
                  GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
                  mapMessage = putErrorMessage(mapMessage, "system.error");
              }


              return binder.toJson(mapMessage);
          }

}
