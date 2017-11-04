package com.enjoyf.webapps.tools.weblogic.gameres;

import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.WikiResource;
import com.enjoyf.platform.service.gameres.WikiResourceField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-11
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "wikiResourceWebLogic")
public class WikiResourceWebLogic {

    public PageRows<WikiResource> queryWikiResources(QueryExpress queryExpress, Pagination page) throws ServiceException {
        return GameResourceServiceSngl.get().queryWikiResourceByPage(queryExpress, page);
    }

    public WikiResource getWikiReosurce(long resid) throws ServiceException {
        return GameResourceServiceSngl.get().getWikiResource(new QueryExpress().add(QueryCriterions.eq(WikiResourceField.RESOURCEID, resid)));
    }

    public boolean isNameExist(String resourceName, long resourceIdItself) throws ServiceException {
        //todo 通过sql语句判断 and resourceId<>xxxx
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(WikiResourceField.WIKINAME, resourceName));

        WikiResource wikiResource = GameResourceServiceSngl.get().getWikiResource(getExpress);

        return resourceIdItself == 0 ? wikiResource != null : (wikiResource != null && (wikiResource.getResourceId() != resourceIdItself));
    }

    public boolean isCodeExist(String resourceCode, long resourceIdItself) throws ServiceException {
        //todo 通过sql语句判断 and resourceId<>xxxx
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(WikiResourceField.WIKICODE, resourceCode));

        WikiResource wikiResource = GameResourceServiceSngl.get().getWikiResource(getExpress);

        return resourceIdItself == 0 ? wikiResource != null : (wikiResource != null && (wikiResource.getResourceId() != resourceIdItself));
    }


    public void sort(String sort, Long id) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WikiGameresField.RECOMMEND, 1));

        WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.ID, id)));

        if ("bottom".equals(sort)) {
            queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.DESC));
        } else if ("up".equals(sort)) {
            queryExpress.add(QueryCriterions.lt(WikiGameresField.DISPLAYORDER, wikiGameres.getDisplayOrder()));
            queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.DESC));
        } else if ("down".equals(sort)) {
            queryExpress.add(QueryCriterions.gt(WikiGameresField.DISPLAYORDER, wikiGameres.getDisplayOrder()));
            queryExpress.add(QuerySort.add(WikiGameresField.DISPLAYORDER, QuerySortOrder.ASC));
        }
        WikiGameres sortGameRes = AskServiceSngl.get().getWikiGameresByQueryExpress(queryExpress);

        if ("up".equals(sort) || "down".equals(sort)) {
            updateExpress1.set(WikiGameresField.DISPLAYORDER, wikiGameres.getDisplayOrder());
            AskServiceSngl.get().modifyRecommend(sortGameRes.getId(), true, updateExpress1);

            updateExpress2.set(WikiGameresField.DISPLAYORDER, sortGameRes.getDisplayOrder());
            AskServiceSngl.get().modifyRecommend(wikiGameres.getId(), true, updateExpress2);
        } else if ("top".equals(sort)) {
            updateExpress1.set(WikiGameresField.DISPLAYORDER, Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            AskServiceSngl.get().modifyRecommend(wikiGameres.getId(), true, updateExpress1);
        } else {
            updateExpress1.set(WikiGameresField.DISPLAYORDER, sortGameRes.getDisplayOrder() + 1);
            AskServiceSngl.get().modifyRecommend(wikiGameres.getId(), true, updateExpress1);
        }
    }
}


