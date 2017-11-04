package com.enjoyf.webapps.joyme.weblogic.mobilegame;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.webapps.joyme.dto.mobilegame.MobileGameDTO;
import com.enjoyf.webapps.joyme.dto.mobilegame.MobileGameUserDTO;
import com.enjoyf.webapps.joyme.dto.mobilegame.MobilegameIndexDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-22
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "mobileGameWebLogic")
public class MobileGameWebLogic {

    private static int SHORE_COMMMENT_PAGE_SIZE = 2;
    private static int INDEX_PAGE_SIZE = 10;
    private static int GAG_COMMMENT_PAGE_SIZE = 30;

    //首页加载更多
    public MobilegameIndexDTO indexLoadMore(Long lind, int pageInt) throws ServiceException {
        MobilegameIndexDTO dto = new MobilegameIndexDTO();
        List<MobileGameDTO> gamedtolist = new ArrayList<MobileGameDTO>();

        QueryExpress queryExpressItem = new QueryExpress();
        queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
        queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lind)));
        queryExpressItem.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

        Pagination pagination = new Pagination(INDEX_PAGE_SIZE * pageInt, pageInt, INDEX_PAGE_SIZE);
        //查询ClientLineItem
        PageRows<ClientLineItem> clientLineItemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpressItem, pagination);

        if (!CollectionUtil.isEmpty(clientLineItemPageRows.getRows())) {
            dto.setMaxPage(clientLineItemPageRows.getPage().getMaxPage());

            Set<Long> gamedbId = new HashSet<Long>();
            Set<Long> contentId = new HashSet<Long>();
            for (ClientLineItem item : clientLineItemPageRows.getRows()) {
                gamedbId.add(Long.valueOf(item.getDirectId()));
                contentId.add(Long.valueOf(item.getContentid()));
            }

            //查询游戏基本信息
            Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gamedbId);


            //查询评论对象
            Map<Long, ForignContent> forignContentMap = getForignContentMap(contentId);

            for (ClientLineItem item : clientLineItemPageRows.getRows()) {
                GameDB db = gameDBMap.get(Long.valueOf(item.getDirectId()));
                MobileGameDTO gameDTO = new MobileGameDTO();
                gameDTO.setGamedbid(db.getGameDbId());
                gameDTO.setGamename(db.getGameName());
                gameDTO.setGameicon(db.getGameIcon());
                ForignContent forignContent = forignContentMap.get(db.getGameDbId());


                gamedtolist.add(gameDTO);
                if (forignContent != null) {
                    gameDTO.setAverage_score(forignContent.getAverage_score());
                    gameDTO.setAverage_score_per((forignContent.getAverage_score() / 10.0) * 100);
                    gameDTO.setReplynum(forignContent.getReplyNum());
                    gameDTO.setContentid(forignContent.getContentId());
                }

                QueryExpress queryExpressReply = new QueryExpress();
                queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, forignContent.getContentId()));
                queryExpressReply.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
                queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                queryExpressReply.add(QueryCriterions.ne(ForignContentReplyField.BODY, ""));
                PageRows<ForignContentReply> list = ContentServiceSngl.get().queryForignContentReplyByPage(queryExpressReply, new Pagination(SHORE_COMMMENT_PAGE_SIZE, 1, SHORE_COMMMENT_PAGE_SIZE));

                List<MobileGameUserDTO> shortcommentlist = new ArrayList<MobileGameUserDTO>();
                if (!CollectionUtil.isEmpty(list.getRows())) {

                    //返回用户集合
                    Set<String> repleyUnoSet = new HashSet<String>();
                    for (ForignContentReply reply : list.getRows()) {
                        repleyUnoSet.add(reply.getReplyUno());
                    }
                    //TODO 删除
                    Map<String, AccountVirtual> returnAccountVirtualMap = null;//JoymeAppServiceSngl.get().queryAccountVirtualByUnos(repleyUnoSet);

                    for (ForignContentReply reply : list.getRows()) {
                        MobileGameUserDTO mobileGameUserDTO = new MobileGameUserDTO();
                        mobileGameUserDTO.setMsg(reply.getBody());
                        AccountVirtual accountVirtual = returnAccountVirtualMap.get(reply.getReplyUno());
                        if (accountVirtual != null) {
                            mobileGameUserDTO.setHeader(accountVirtual.getHeadicon().getPic());
                            mobileGameUserDTO.setName(accountVirtual.getScreenname());
                        }
                        shortcommentlist.add(mobileGameUserDTO);
                    }
                }
                gameDTO.setShortcommentlist(shortcommentlist);
            }
        }

        dto.setCurPage(pageInt);
        dto.setGamedtolist(gamedtolist);
        return dto;
    }

    //首页
    public Map<String, Object> index(Long lind, int pageInt, ClientLine clientLine) throws ServiceException {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        MobilegameIndexDTO dto = new MobilegameIndexDTO();
        dto.setLinename(clientLine.getLineName());
        dto.setLinedesc(clientLine.getLine_desc());
        dto.setIndexpic(clientLine.getBigpic());

        //吐槽
        ForignContent forignContentGag = ContentServiceSngl.get().getForignContentByFidCdomain(lind + "", ForignContentDomain.GAG);
        if (forignContentGag != null) {
            returnMap.put("forignContentGag", forignContentGag);
            returnMap.put("gagContentId", forignContentGag.getContentId());

            List<ForignContentReply> gagForignContentReplyList = ContentServiceSngl.get().queryMobileGameGagForignReply(forignContentGag.getContentId(), GAG_COMMMENT_PAGE_SIZE);
            if (!CollectionUtil.isEmpty(gagForignContentReplyList)) {
                List<MobileGameUserDTO> mobileGameGagDTOs = new ArrayList<MobileGameUserDTO>();

                Set<String> repleyUnoSet = new HashSet<String>();
                for (ForignContentReply reply : gagForignContentReplyList) {
                    repleyUnoSet.add(reply.getReplyUno());
                }
                //TODO 删除
                Map<String, AccountVirtual> returnAccountVirtualMap = null;//JoymeAppServiceSngl.get().queryAccountVirtualByUnos(repleyUnoSet);


                for (ForignContentReply reply : gagForignContentReplyList) {
                    MobileGameUserDTO mobileGameGagDTO = new MobileGameUserDTO();
                    mobileGameGagDTO.setMsg(reply.getBody());
                    mobileGameGagDTOs.add(mobileGameGagDTO);
                    AccountVirtual accountVirtual = returnAccountVirtualMap.get(reply.getReplyUno());
                    if (accountVirtual != null) {
                        mobileGameGagDTO.setHeader(accountVirtual.getHeadicon().getPic());
                    }
                }
                dto.setGaglist(mobileGameGagDTOs);
            }
        }

        QueryExpress queryExpressItem = new QueryExpress();
        queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
        queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lind));
        queryExpressItem.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

        Pagination pagination = new Pagination(INDEX_PAGE_SIZE * pageInt, pageInt, INDEX_PAGE_SIZE);
        //查询ClientLineItem
        PageRows<ClientLineItem> clientLineItemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpressItem, pagination);
        returnMap.put("maxPage", clientLineItemPageRows.getPage().getMaxPage());
        Set<Long> gamedbId = new HashSet<Long>();
        Set<Long> contentId = new HashSet<Long>();
        if (!CollectionUtil.isEmpty(clientLineItemPageRows.getRows())) {
            for (ClientLineItem item : clientLineItemPageRows.getRows()) {
                gamedbId.add(Long.valueOf(item.getDirectId()));
                contentId.add(Long.valueOf(item.getContentid()));
            }
        }

        //查询游戏基本信息
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gamedbId);

        List<MobileGameDTO> gamedtolist = new ArrayList<MobileGameDTO>();

        //查询评论对象
        Map<Long, ForignContent> forignContentMap = getForignContentMap(contentId);


        for (ClientLineItem item : clientLineItemPageRows.getRows()) {
            GameDB db = gameDBMap.get(Long.valueOf(item.getDirectId()));
            MobileGameDTO gameDTO = new MobileGameDTO();
            gameDTO.setGamedbid(db.getGameDbId());
            gameDTO.setGamename(db.getGameName());
            gameDTO.setGameicon(db.getGameIcon());
            ForignContent forignContent = forignContentMap.get(db.getGameDbId());


            gamedtolist.add(gameDTO);
            if (forignContent != null) {
                gameDTO.setAverage_score(forignContent.getAverage_score());
                gameDTO.setAverage_score_per((forignContent.getAverage_score() / 10.0) * 100);
                gameDTO.setReplynum(forignContent.getReplyNum());
                gameDTO.setContentid(forignContent.getContentId());
            }

            QueryExpress queryExpressReply = new QueryExpress();
            queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, forignContent.getContentId()));
            queryExpressReply.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpressReply.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpressReply.add(QueryCriterions.ne(ForignContentReplyField.BODY, ""));
            PageRows<ForignContentReply> list = ContentServiceSngl.get().queryForignContentReplyByPage(queryExpressReply, new Pagination(SHORE_COMMMENT_PAGE_SIZE, 1, SHORE_COMMMENT_PAGE_SIZE));
            List<MobileGameUserDTO> shortcommentlist = new ArrayList<MobileGameUserDTO>();
            if (!CollectionUtil.isEmpty(list.getRows())) {

                Set<String> repleyUnoSet = new HashSet<String>();
                for (ForignContentReply reply : list.getRows()) {
                    repleyUnoSet.add(reply.getReplyUno());
                }
                //TODO 删除
                Map<String, AccountVirtual> returnAccountVirtualMap = null;//JoymeAppServiceSngl.get().queryAccountVirtualByUnos(repleyUnoSet);
                for (ForignContentReply reply : list.getRows()) {
                    MobileGameUserDTO mobileGameUserDTO = new MobileGameUserDTO();
                    mobileGameUserDTO.setMsg(reply.getBody());
                    AccountVirtual accountVirtual = returnAccountVirtualMap.get(reply.getReplyUno());
                    if (accountVirtual != null) {
                        mobileGameUserDTO.setHeader(accountVirtual.getHeadicon().getPic());
                        mobileGameUserDTO.setName(accountVirtual.getScreenname());
                    }
                    shortcommentlist.add(mobileGameUserDTO);
                }
            }
            gameDTO.setShortcommentlist(shortcommentlist);
        }
        dto.setGamedtolist(gamedtolist);
        returnMap.put("dto", dto);
        return returnMap;
    }


    public Map<Long, ForignContent> getForignContentMap(Set<Long> contentId) throws ServiceException {
        Map<Long, ForignContent> forignContentIdMap = ContentServiceSngl.get().getForignContentBySet(contentId);
        Map<Long, ForignContent> forignContentMap = new HashMap<Long, ForignContent>();
        for (Map.Entry<Long, ForignContent> entry : forignContentIdMap.entrySet()) {
            ForignContent fr = entry.getValue();
            forignContentMap.put(Long.valueOf(fr.getForignId()), fr);
        }
        return forignContentMap;
    }

}
