package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;

import net.sf.json.JSONObject;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-11-22
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/cmstools/gamedb")
public class CmsToolsGameDBController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(CmsToolsGameDBController.class);

    @RequestMapping(value = "/list")
    public ModelAndView cmsGameList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "30") int pageSize,
                                    @RequestParam(value = "valistatus", required = false) String validstatus,
                                    @RequestParam(value = "searchname", required = false) String searchname)

    {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            MongoQueryExpress queryExpress = new MongoQueryExpress();
            queryExpress.add(new MongoSort[]{new MongoSort(GameDBField.ID, MongoSortOrder.DESC)});
            if (!StringUtil.isEmpty(validstatus)) {
                queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, validstatus));
                mapMessage.put("validstatus", validstatus);
            }
            if (!StringUtil.isEmpty(searchname)) {
                queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, searchname));
                mapMessage.put("searchname", searchname);
            }
            PageRows<GameDB> pageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("list", pageRows.getRows());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }

        return new ModelAndView("/gameresource/gamedb/cmstools/gamedblist", mapMessage);
    }

}
