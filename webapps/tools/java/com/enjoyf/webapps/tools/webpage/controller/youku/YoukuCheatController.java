package com.enjoyf.webapps.tools.webpage.controller.youku;

import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheatFiled;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheatType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by zhimingli on 2015/6/12.
 */

@Controller
@RequestMapping(value = "/youku/cheat")
public class YoukuCheatController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchiveCheatFiled.ARCHIVE_TYPE, TagDedearchiveCheatType.YOUKU.getCode()));
            queryExpress.add(QuerySort.add(TagDedearchiveCheatFiled.CHEATING_TIME, QuerySortOrder.DESC));
            PageRows<TagDedearchiveCheat> pageRows = JoymeAppServiceSngl.get().queryTagDedearchiveCheat(queryExpress, pagination);
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                Set<Integer> idSet = new HashSet<Integer>();
                for (TagDedearchiveCheat cheat : pageRows.getRows()) {
                    idSet.add(cheat.getDede_archives_id().intValue());
                }
                Map<Integer, Archive> archiveMap = JoymeAppServiceSngl.get().queryArchiveMapByIds(idSet);
                mapMessage.put("archiveMap", archiveMap);
            }
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return new ModelAndView("/youku/cheat/cheatlist", mapMessage);
        }
        return new ModelAndView("/youku/cheat/cheatlist", mapMessage);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyVersion(@RequestParam(value = "dede_archives_id", required = false, defaultValue = "0") Integer dede_archives_id,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        try {
            Archive archive = JoymeAppServiceSngl.get().getArchiveById(dede_archives_id);
            TagDedearchiveCheat cheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(dede_archives_id.longValue());
            mapMessage.put("archive", archive);
            mapMessage.put("cheat", cheat);
        } catch (Exception e) {
        }
        return new ModelAndView("/youku/cheat/modifycheat", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView preModifyApp(@RequestParam(value = "dede_archives_id", required = false, defaultValue = "0") Long dede_archives_id,
                                     @RequestParam(value = "read_num", required = false, defaultValue = "0") Integer read_num,
                                     @RequestParam(value = "agree_num", required = false, defaultValue = "0") Integer agree_num,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer pageStartIndex
    ) {
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TagDedearchiveCheatFiled.READ_NUM, read_num);
            updateExpress.set(TagDedearchiveCheatFiled.AGREE_NUM, agree_num);
            JoymeAppServiceSngl.get().modifyTagDedearchiveCheat(dede_archives_id, updateExpress);
        } catch (Exception e) {
            return null;
        }

        return new ModelAndView("redirect:/youku/cheat/list?pager.offset=" + pageStartIndex);
    }
}
