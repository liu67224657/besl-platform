package com.enjoyf.webapps.tools.weblogic.editor;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.service.tools.stats.EditorStatDomain;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatDTO;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatsContentDTO;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatsGameDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-1
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "editorStatsWebLogic")
public class EditorStatsWebLogic {
    public List<EditorStatsContentDTO> statsEditorContent(Integer adminUno, StatDateType dateType, Date statDate) throws ServiceException {
        List<EditorStatsContentDTO> returnObj = new ArrayList<EditorStatsContentDTO>();

        List<StatsEditorItem> statsEditorItemList = ToolsServiceSngl.get().queryStatsEditorItem(new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorItemField.ADMINUNO, adminUno))
                .add(QueryCriterions.eq(StatsEditorItemField.ITEMTYPE, EditorStatsItemType.ARTICLE.getCode()))
                .add(QueryCriterions.eq(StatsEditorItemField.VALIDSTATUS, ValidStatus.VALID.getCode()))
        );


        Date startDate = dateType.getStartDateByType(statDate);
        Map<StatSection, StatItem> contentPVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_PV, dateType, startDate);
        Map<StatSection, StatItem> contentUVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_UV, dateType, startDate);
        Map<StatSection, StatItem> contentCPVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_CPV, dateType, startDate);
        Map<StatSection, StatItem> contentReplyMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_CMT, dateType, startDate);

        for (StatsEditorItem statsEditorItem : statsEditorItemList) {
            EditorStatsContentDTO dto = new EditorStatsContentDTO(statsEditorItem.getSourceId());
            dto.setLinkurl(statsEditorItem.getItemSrcNo());
            dto.setSubType(statsEditorItem.getItemSubType());
            dto.setCreateDate(statsEditorItem.getCreateDate());

            StatSection section = new StatSectionDefault(adminUno + "/" + statsEditorItem.getSourceId());
            if (contentPVMap.containsKey(section)) {
                dto.setPv(contentPVMap.get(section).getStatValue());
            }
            if (contentUVMap.containsKey(section)) {
                dto.setUv(contentUVMap.get(section).getStatValue());
            }
            if (contentCPVMap.containsKey(section)) {
                dto.setCpv(contentCPVMap.get(section).getStatValue());
            }
            if (contentReplyMap.containsKey(section)) {
                dto.setCmt(contentReplyMap.get(section).getStatValue());
            }
            returnObj.add(dto);
        }
        return returnObj;
    }

    public List<EditorStatsGameDTO> statsEditorGame(Integer adminUno, StatDateType dateType, Date statDate) throws ServiceException {
        List<EditorStatsGameDTO> returnObj = new ArrayList<EditorStatsGameDTO>();

        List<StatsEditorItem> gameItemList = ToolsServiceSngl.get().queryStatsEditorItem(new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorItemField.ADMINUNO, adminUno))
                .add(QueryCriterions.eq(StatsEditorItemField.ITEMTYPE, EditorStatsItemType.GAME.getCode()))
                .add(QueryCriterions.eq(StatsEditorItemField.VALIDSTATUS, ValidStatus.VALID.getCode()))
        );


        Date startDate = dateType.getStartDateByType(statDate);
        Map<StatSection, StatItem> gamePVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_GAME_PV, dateType, startDate);
        Map<StatSection, StatItem> gameUVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_GAME_UV, dateType, startDate);

        for (StatsEditorItem statsEditorItem : gameItemList) {
            EditorStatsGameDTO dto = new EditorStatsGameDTO(statsEditorItem.getSourceId());
            dto.setLinkurl(statsEditorItem.getItemSrcNo());
            dto.setCreateDate(statsEditorItem.getCreateDate());

            StatSection section = new StatSectionDefault(adminUno + "/" + statsEditorItem.getSourceId());
            if (gamePVMap.containsKey(section)) {
                dto.setPv(gamePVMap.get(section).getStatValue());
            }
            if (gameUVMap.containsKey(section)) {
                dto.setUv(gameUVMap.get(section).getStatValue());
            }

            returnObj.add(dto);
        }
        return returnObj;
    }

    public List<EditorStatDTO> statsEditor(StatDateType dateType, Date statDate) throws ServiceException {
        List<EditorStatDTO> returnObj = new ArrayList<EditorStatDTO>();

        List<StatsEditor> gameItemList = ToolsServiceSngl.get().queryStatsEditor(new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode())
                ));

        Date startDate = dateType.getStartDateByType(statDate);
        Map<StatSection, StatItem> contentPVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_PV, dateType, startDate);
        Map<StatSection, StatItem> contentUVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_UV, dateType, startDate);
        Map<StatSection, StatItem> contentCPVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_CPV, dateType, startDate);
        Map<StatSection, StatItem> contentReplyMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_CMT, dateType, startDate);
        Map<StatSection, StatItem> contentPostMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_ARTICLE_POST, dateType, startDate);
        Map<StatSection, StatItem> gamePVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_GAME_PV, dateType, startDate);
        Map<StatSection, StatItem> gameUVMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_GAME_UV, dateType, startDate);
        Map<StatSection, StatItem> gameMap = StatServiceSngl.get().queryStatItemsByDomain(EditorStatDomain.EDITOR_GAME_CREATE, dateType, startDate);


        for (StatsEditor statsEditor : gameItemList) {
            EditorStatDTO dto = new EditorStatDTO();
            dto.setAdminUno(statsEditor.getAdminUno());
            dto.setEditorName(statsEditor.getEditorName());

            StatSection section = new StatSectionDefault(statsEditor.getAdminUno() + "/all");
            if (contentPVMap.containsKey(section)) {
                dto.setPv(contentPVMap.get(section).getStatValue());
            }
            if (contentUVMap.containsKey(section)) {
                dto.setUv(contentUVMap.get(section).getStatValue());
            }
            if (contentCPVMap.containsKey(section)) {
                dto.setCpv(contentCPVMap.get(section).getStatValue());
            }
            if (contentReplyMap.containsKey(section)) {
                dto.setCmt(contentReplyMap.get(section).getStatValue());
            }
            if (gamePVMap.containsKey(section)) {
                dto.setGamepv(gamePVMap.get(section).getStatValue());
            }
            if (gameUVMap.containsKey(section)) {
                dto.setGameuv(gameUVMap.get(section).getStatValue());
            }
            if (gameMap.containsKey(section)) {
                dto.setPostGame(gameMap.get(section).getStatValue());
            }

            //..............
            StatSection gonglueSection = new StatSectionDefault(statsEditor.getAdminUno() + "/" + EditorStatsItemSubType.GONGLUE.getCode());
            StatSection pingceSection = new StatSectionDefault(statsEditor.getAdminUno() + "/" + EditorStatsItemSubType.PINGCE.getCode());
            StatSection zhuantiSection = new StatSectionDefault(statsEditor.getAdminUno() + "/" + EditorStatsItemSubType.ZHUANTI.getCode());
            if (contentPostMap.containsKey(gonglueSection)) {
                dto.setGonglue(contentPostMap.get(gonglueSection).getStatValue());
            }
            if (contentPostMap.containsKey(pingceSection)) {
                dto.setPingce(contentPostMap.get(pingceSection).getStatValue());
            }
            if (contentPostMap.containsKey(zhuantiSection)) {
                dto.setZhuanti(contentPostMap.get(zhuantiSection).getStatValue());
            }

            returnObj.add(dto);
        }
        return returnObj;
    }
}
