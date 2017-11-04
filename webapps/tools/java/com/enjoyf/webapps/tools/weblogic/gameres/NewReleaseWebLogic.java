package com.enjoyf.webapps.tools.weblogic.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.game.NewReleaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-22
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "newGameInfoWebLogic")
public class NewReleaseWebLogic {

    private Logger logger = LoggerFactory.getLogger(NewReleaseWebLogic.class);

    public void newGameInfoSort(long newGameInfoId, String sort) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(NewReleaseField.VALID_STATUS, ValidStatus.VALID.getCode()));
        NewRelease newRelease = GameResourceServiceSngl.get().getNewGameInfo(newGameInfoId);
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(NewReleaseField.DISPLAY_ORDER, newRelease.getDisplayOrder()));
            queryExpress.add(QuerySort.add(NewReleaseField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(NewReleaseField.DISPLAY_ORDER, newRelease.getDisplayOrder()));
            queryExpress.add(QuerySort.add(NewReleaseField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }
        List<NewRelease> list = GameResourceServiceSngl.get().queryNewGameInfo(queryExpress);

        if (!CollectionUtil.isEmpty(list)) {
            updateExpress1.set(NewReleaseField.DISPLAY_ORDER, newRelease.getDisplayOrder());
            GameResourceServiceSngl.get().modifyNewGameInfo(list.get(0).getNewReleaseId(), updateExpress1);

            updateExpress2.set(NewReleaseField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
            GameResourceServiceSngl.get().modifyNewGameInfo(newRelease.getNewReleaseId(), updateExpress2);
        }
        GameResourceServiceSngl.get().queryNewGameInfo(new QueryExpress().add(QueryCriterions.eq(NewReleaseField.VALID_STATUS, ValidStatus.VALID.getCode())).add(QuerySort.add(NewReleaseField.DISPLAY_ORDER, QuerySortOrder.ASC)));
    }

    public PageRows<NewReleaseDTO> queryNewGameInfoDTO(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<NewRelease> infoPageRows = GameResourceServiceSngl.get().queryNewGameInfoByPage(queryExpress, pagination);
        if (infoPageRows == null || CollectionUtil.isEmpty(infoPageRows.getRows())) {
            return null;
        }
        NewReleaseDTO dto = null;
        List<NewReleaseDTO> list = new ArrayList<NewReleaseDTO>();
        for (NewRelease info : infoPageRows.getRows()) {
            dto = getNewGameInfoDTO(info.getNewReleaseId());
            list.add(dto);
        }
        PageRows<NewReleaseDTO> pageRows = new PageRows<NewReleaseDTO>();
        pageRows.setRows(list);
        pageRows.setPage(pagination);
        return pageRows;
    }

    public NewReleaseDTO getNewGameInfoDTO(long newGameInfoId) throws ServiceException {
        List<NewReleaseDTO> list = new LinkedList<NewReleaseDTO>();
        Long[] tagIds = null;
        NewRelease newRelease = GameResourceServiceSngl.get().getNewGameInfo(newGameInfoId);
        if(newRelease ==null){
            return null;
        }
        List<NewReleaseTagRelation> newTagRelationList = GameResourceServiceSngl.get().queryNewTagRelation(new QueryExpress().add(QueryCriterions.eq(NewReleaseTagRelationField.STATUS, ActStatus.UNACT.getCode())).add(QueryCriterions.eq(NewReleaseTagRelationField.NEW_RELEASE_ID, newGameInfoId)));
        if (CollectionUtil.isEmpty(newTagRelationList)) {
            return null;
        }
        tagIds = new Long[newTagRelationList.size()];
        int i = 0;
        for (NewReleaseTagRelation newTagRelation : newTagRelationList) {
            tagIds[i++] = newTagRelation.getNewGameTagId();
        }
        List<NewReleaseTag> newGameTagList = GameResourceServiceSngl.get().queryNewGameTag(new QueryExpress().add(QueryCriterions.in(NewReleaseTagField.NEW_RELEASE_TAG_ID, tagIds)));
        if (CollectionUtil.isEmpty(newGameTagList)) {
            return null;
        }
        List<CityRelation> cityRelationList = GameResourceServiceSngl.get().queryCityRelation(new QueryExpress().add(QueryCriterions.eq(CityRelationField.NEW_GAME_INFO_ID, newGameInfoId)));
        if (CollectionUtil.isEmpty(cityRelationList)) {
            return null;
        }
        List<City> cityList = GameResourceServiceSngl.get().queryCity(new QueryExpress().add(QueryCriterions.eq(CityField.CITY_ID, cityRelationList.get(0).getCityId())));
        if (CollectionUtil.isEmpty(cityList)) {
            return null;
        }
        ShareBaseInfo shareBaseInfo = SyncServiceSngl.get().getShareInfoById(newRelease.getShareId());
//        if (shareBaseInfo == null) {
//            return null;
//        }
        NewReleaseDTO dto = new NewReleaseDTO();
        dto.setNewRelease(newRelease);
        dto.setNewReleaseTagList(newGameTagList);
        dto.setCity(cityList.get(0));
        dto.setShareBaseInfo(shareBaseInfo);
        return dto;
    }
}
