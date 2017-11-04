package com.enjoyf.webapps.joyme.weblogic.apiwiki;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.ask.search.WikiappSearchService;
import com.enjoyf.platform.service.ask.search.WikiappSearchType;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.misc.FiledValue;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.webapps.joyme.dto.apiwiki.ApiwikiTagDTO;
import com.enjoyf.webapps.joyme.dto.apiwiki.ContentDTO;
import com.enjoyf.webapps.joyme.dto.joymewiki.AlphabetGameDTO;
import com.enjoyf.webapps.joyme.dto.joymewiki.CollectDTO;
import com.enjoyf.webapps.joyme.dto.joymewiki.GameDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhimingli on 2017-3-23 0023.
 */

@Service(value = "apiwikiWebLogic")
public class ApiwikiWebLogic {

    private WikiappSearchService searchService = new WikiappSearchService();

    public ScoreRangeRows<ContentDTO> queryContentByScoreRangeRows(String tagid, ScoreRange range, Integer pnum, String appkey, String platform, String pid) throws ServiceException {
        ScoreRangeRows<ContentDTO> rangeRows = new ScoreRangeRows<ContentDTO>();

        ContentTag tag = AskServiceSngl.get().getContentTag(Long.valueOf(tagid));
        if (tag == null) {
            rangeRows.setRange(new ScoreRange(-1, System.currentTimeMillis(), range.getSize()));
            return rangeRows;
        }

        //TODO 如果后续有要查某个游戏下的文章
        //修改allLineKey及logic逻辑
        String allLineKey = AskUtil.getContentLineKey(ContentLineOwn.ALL_ARCHIVE, "");
        if (Integer.valueOf(tag.getTarget()) > 0) {
            allLineKey = AskUtil.getContentLineKey(ContentLineOwn.GAME_ALL_ARCHIVE, tag.getTarget());
        }

        ScoreRangeRows<Content> scoreRangeRows = AskServiceSngl.get().queryContentByScoreRangeRows(allLineKey, range, pnum, pid);
        if (scoreRangeRows == null || CollectionUtil.isEmpty(scoreRangeRows.getRows())) {
            rangeRows.setRange(new ScoreRange(-1, System.currentTimeMillis(), range.getSize()));
            return rangeRows;
        }
        List<Content> contentList = scoreRangeRows.getRows();

        //comment
        Set<String> commentIdSet = new HashSet<String>();
        Set<Long> cotentidIdSet = new HashSet<Long>();
        Set<Long> gameidIdSet = new HashSet<Long>();
        for (Content content : contentList) {
            commentIdSet.add(content.getCommentId());
            cotentidIdSet.add(content.getId());
            if (!StringUtil.isEmpty(content.getGameId())) {
                gameidIdSet.add(Long.valueOf(content.getGameId()));
            }
        }
        Map<String, CommentBean> commentMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIdSet);

        Map<Long, ContentSum> contentSumMap = AskServiceSngl.get().queryContentSumByids(cotentidIdSet);

        Map<Integer, Advertise> advertiseMap = getAdMap(appkey, platform);

        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameidIdSet);

        int advIndex = (pnum - 1) * range.getSize();

        List<ContentDTO> contentDTOList = new ArrayList<ContentDTO>();
        for (int i = 0; i < contentList.size(); i++) {
            ContentDTO dto = buildContentDTO(contentList.get(i), commentMap, contentSumMap, gameDBMap);
            if (dto == null) {
                continue;
            }
            contentDTOList.add(dto);

            //插入广告
            Advertise advertise = advertiseMap.get(advIndex + i + 1);
            if (advertise != null) {
                ContentDTO adDTO = buildAdvertiseDTO(advertise);
                adDTO.setType(2);
                contentDTOList.add(adDTO);
            }

        }


        rangeRows.setRows(contentDTOList);
        rangeRows.setRange(scoreRangeRows.getRange());
        return rangeRows;
    }

    /**
     * 通过游戏表ID组装返回值
     *
     * @param map
     * @return
     * @throws ServiceException
     */
    public List<GameDTO> buildGameDTOByMap(Map<Long, WikiGameres> map, Integer version) throws ServiceException {
        if (map.isEmpty()) {
            return null;
        }
        List<GameDTO> returnList = new ArrayList<GameDTO>();
        Set<Long> gameIds = new LinkedHashSet<Long>();
        gameIds.addAll(map.keySet());
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIds);
        for (Long id : gameIds) {
            returnList.add(buildGameDTO(gameDBMap.get(id), map.get(id), version));
        }
        return returnList;
    }

    /**
     * 需求变更
     *
     * @param allGames  所有游戏
     * @param followMap 用户关注的游戏
     * @return
     * @throws ServiceException
     */
    public List<GameDTO> buildGameDTOByMap(List<WikiGameres> allGames, Map<Long, WikiGameres> followMap, Integer version) throws ServiceException {
        if (CollectionUtil.isEmpty(allGames)) {
            return null;
        }
        List<GameDTO> returnList = new ArrayList<GameDTO>();
        Map<Long, WikiGameres> allGamesMap = new HashMap<Long, WikiGameres>();
        Set<Long> gameIds = new LinkedHashSet<Long>();
        for (WikiGameres wikiGameres : allGames) {
            gameIds.add(wikiGameres.getGameId());
            allGamesMap.put(wikiGameres.getGameId(), wikiGameres);
        }

        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIds);
        for (Long id : gameIds) {
            GameDTO gameDTO = buildGameDTO(gameDBMap.get(id), allGamesMap.get(id), version);
            gameDTO.setFollow(followMap.get(id) == null ? 0 : 1);
            returnList.add(gameDTO);
        }
        return returnList;
    }

    /**
     * 按首字母分类 旧需求 暂时不删
     *
     * @param wikiGameresList
     * @return
     * @throws ServiceException
     */
    public List<AlphabetGameDTO> queryAlphabetGames(List<WikiGameres> wikiGameresList) throws ServiceException {
        if (CollectionUtil.isEmpty(wikiGameresList)) {
            return null;
        }
        Set<Long> gameIdS = new HashSet<Long>();
        for (WikiGameres wikiGameres : wikiGameresList) {
            gameIdS.add(wikiGameres.getGameId());
        }
        //查询游戏库
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIdS);
        GameDB gameDB;
        String firstLetter;  //首字母
        Map<String, String> letterMap = StringUtil.getLetterMap();
        letterMap.put("#", "#");

        List<AlphabetGameDTO> alphabetGameDTOs = new ArrayList<AlphabetGameDTO>();
        List<GameDTO> gameDTOs = new ArrayList<GameDTO>();

        for (String letter : letterMap.keySet()) {
            for (WikiGameres wikiGameres : wikiGameresList) {
                gameDB = gameDBMap.get(wikiGameres.getGameId());
                if (wikiGameres != null && gameDB != null) {
                    firstLetter = PinYinUtil.getFirstWordLetter(gameDB.getGameName());
                    firstLetter = letterMap.get(firstLetter) == null ? "#" : firstLetter;
                    if (letter.equals(firstLetter)) {
                        gameDTOs.add(buildGameDTO(gameDBMap.get(wikiGameres.getGameId()), wikiGameres, 0));
                    }
                }
            }
            if (!CollectionUtil.isEmpty(gameDTOs)) {
                AlphabetGameDTO alphabetGameDTO = new AlphabetGameDTO();
                alphabetGameDTO.setTitle(letter.toUpperCase());
                alphabetGameDTO.setContent(gameDTOs);
                alphabetGameDTOs.add(alphabetGameDTO);
                gameDTOs = new ArrayList<GameDTO>();
            }
        }
        return alphabetGameDTOs;
    }

    /**
     * 需求变更 查询所有游戏 按首字母排序
     *
     * @param wikiGameresList
     * @return
     * @throws ServiceException
     */
    public List<GameDTO> queryAllGamesByAlphabetSort(List<WikiGameres> wikiGameresList, Map<Long, WikiGameres> followMap) throws ServiceException {
        if (CollectionUtil.isEmpty(wikiGameresList)) {
            return null;
        }
        List<GameDTO> returnList = new ArrayList<GameDTO>();
        Set<Long> gameIdS = new HashSet<Long>();

        for (WikiGameres wikiGameres : wikiGameresList) {
            gameIdS.add(wikiGameres.getGameId());
        }
        //查询游戏库
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIdS);
        GameDB gameDB;
        String firstLetter;  //首字母
        Map<String, String> letterMap = StringUtil.getLetterMap();
        letterMap.put("#", "#");
        for (String letter : letterMap.keySet()) {
            for (WikiGameres wikiGameres : wikiGameresList) {
                gameDB = gameDBMap.get(wikiGameres.getGameId());
                firstLetter = PinYinUtil.getFirstWordLetter(gameDB.getGameName());
                firstLetter = letterMap.get(firstLetter) == null ? "#" : firstLetter;
                if (letter.equals(firstLetter)) {
                    GameDTO gameDTO = buildGameDTO(gameDBMap.get(wikiGameres.getGameId()), wikiGameres, 0);
                    gameDTO.setFollow(followMap.get(wikiGameres.getGameId()) == null ? 0 : 1);
                    returnList.add(gameDTO);
                }
            }
        }
        return returnList;
    }


    private GameDTO buildGameDTO(GameDB gameDB, WikiGameres wikiGameres, Integer version) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFocusnum(wikiGameres == null ? "" : String.valueOf(wikiGameres.getFocusSum()));
        gameDTO.setGamename(gameDB == null ? "" : gameDB.getGameName());
        gameDTO.setPicurl(wikiGameres == null ? "" : wikiGameres.getHeadPic());


        //TODO 1.2.0版本以上直接跳首页
        if (version >= 120) {
            gameDTO.setJt("-2");
            gameDTO.setJi(gameDB == null ? "" : "http://wiki." + WebappConfig.get().DOMAIN + "/" + gameDB.getWikiKey() + "/%E9%A6%96%E9%A1%B5?useskin=MediaWikiBootstrap2");
        } else {
            gameDTO.setJt("11");    //wiki相关跳转
            gameDTO.setJi(gameDB == null ? "" : "http://hezuo." + WebappConfig.get().DOMAIN + "/wiki/index.php?c=wiki&a=aclist&wikikey=" + gameDB.getWikiKey());
        }
        gameDTO.setGameid(String.valueOf(gameDB.getGameDbId()));

        gameDTO.setTime(wikiGameres.getUpdateTime().getTime());
        gameDTO.setRecommend(wikiGameres.getRecommend());
        return gameDTO;
    }

    private ContentDTO buildContentDTO(Content content, Map<String, CommentBean> commentMap, Map<Long, ContentSum> contentSumMap, Map<Long, GameDB> gameDBMap) {
        ContentDTO dto = new ContentDTO();
        dto.setContentid(content.getId());
        dto.setPicurl(StringUtil.isEmpty(content.getPic()) ? "" : content.getPic());
        dto.setTitle(StringUtil.isEmpty(content.getTitle()) ? "" : content.getTitle());
        dto.setDesc(StringUtil.isEmpty(content.getDescription()) ? "" : content.getDescription());
        dto.setTime(content.getPublishTime().getTime());
        dto.setJi(StringUtil.isEmpty(content.getWebUrl()) ? "" : content.getWebUrl());
        dto.setJt("-2");

        CommentBean bean = commentMap.get(content.getCommentId());
        if (bean != null) {
            dto.setReplynum(bean.getCommentSum() < 0 ? 0 : bean.getCommentSum());
        }


        ContentSum contentSum = contentSumMap.get(content.getId());
        if (contentSum != null) {
            dto.setPraisenum(contentSum.getAgree_num() < 0 ? 0 : contentSum.getAgree_num());
        }

        if (StringUtil.isEmpty(content.getGameId())) {
            dto.setGamename("");
        } else {
            GameDB gameDB = gameDBMap.get(Long.valueOf(content.getGameId()));
            dto.setGamename(gameDB == null ? "" : gameDB.getGameName());
        }

        return dto;
    }


    public Map<Integer, Advertise> getAdMap(String appkey, String platform) {
        Map<Integer, Advertise> retutnMap = new HashMap<Integer, Advertise>();
        if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(platform)) {
            return retutnMap;
        }
        try {
            List<Advertise> list = AskServiceSngl.get().queryAdvertiseByLineKey(AskUtil.getAdvertiseLinekey(AppUtil.getAppKey(appkey), Integer.valueOf(platform), AdvertiseDomain.ARTICLE_ADVERTISE));
            for (Advertise advertise : list) {
                if (!StringUtil.isEmpty(advertise.getExtend())) {
                    AdExtendJson json = AdExtendJson.toObject(advertise.getExtend());
                    retutnMap.put(json.getIndex(), advertise);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return retutnMap;
    }


    public List<ContentDTO> headitems(String appkey, String platform, int pnum) {
        List<ContentDTO> retutnList = new ArrayList<ContentDTO>();
        if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(platform) || pnum != 1) {
            return retutnList;
        }
        try {
            List<Advertise> list = AskServiceSngl.get().queryAdvertiseByLineKey(AskUtil.getAdvertiseLinekey(AppUtil.getAppKey(appkey), Integer.valueOf(platform), AdvertiseDomain.CAROUSEL));
            for (Advertise advertise : list) {
                retutnList.add(buildAdvertiseDTO(advertise));
            }
        } catch (ServiceException e) {
            e.printStackTrace();

        }
        return retutnList;
    }

    private ContentDTO buildAdvertiseDTO(Advertise advertise) {
        ContentDTO dto = new ContentDTO();
        dto.setPicurl(StringUtil.isEmpty(advertise.getPic()) ? "" : advertise.getPic());
        dto.setTitle(StringUtil.isEmpty(advertise.getTitle()) ? "" : advertise.getTitle());
        dto.setDesc(StringUtil.isEmpty(advertise.getDescription()) ? "" : advertise.getDescription());
        dto.setTime(advertise.getCreateDate().getTime());
        if (advertise.getType() == JoymewikiJt.WAP_WIKI.getCode()) {
            String target = advertise.getTarget();
            dto.setJi(StringUtil.isEmpty(target) ? "" : target);
            dto.setJt("-2");
        } else {
            dto.setJi(StringUtil.isEmpty(advertise.getTarget()) ? "" : advertise.getTarget());
            dto.setJt(String.valueOf(advertise.getType()));
        }
        return dto;
    }


    /**
     * 搜索solr 文章
     */
    public PageRows<ContentDTO> searchArchivByText(String text, Pagination page) throws ServiceException {
        PageRows<ContentDTO> rows = new PageRows<ContentDTO>();
        PageRows<FiledValue> contentIds = searchService.searchByType(WikiappSearchType.ARCHIVE, page.getCurPage(), page.getPageSize(), text);
        if (CollectionUtil.isEmpty(contentIds.getRows())) {
            return rows;
        }


        Set<Long> cotentidIdSet = new HashSet<Long>();
        for (FiledValue filedValue : contentIds.getRows()) {
            cotentidIdSet.add(Long.valueOf(filedValue.getKey()));
        }

        Map<Long, Content> contentMap = AskServiceSngl.get().queryContentByids(cotentidIdSet);


        Set<Long> gameidIdSet = new HashSet<Long>();
        Set<String> commentIdSet = new HashSet<String>();
        for (Long contentId : contentMap.keySet()) {
            commentIdSet.add(contentMap.get(contentId).getCommentId());

            Content content = contentMap.get(contentId);
            if (!StringUtil.isEmpty(content.getGameId())) {
                gameidIdSet.add(Long.valueOf(content.getGameId()));
            }
        }

        Map<String, CommentBean> commentMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIdSet);
        Map<Long, ContentSum> contentSumMap = AskServiceSngl.get().queryContentSumByids(cotentidIdSet);

        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameidIdSet);

        List<ContentDTO> list = new ArrayList<ContentDTO>();
        for (FiledValue filedValue : contentIds.getRows()) {
            Content content = contentMap.get(Long.valueOf(filedValue.getKey()));
            if (content != null) {
                ContentDTO dto = buildContentDTO(content, commentMap, contentSumMap, gameDBMap);
                if (dto == null) {
                    continue;
                }
                list.add(dto);
            }
        }
        rows.setPage(contentIds.getPage());
        rows.setRows(list);
        return rows;
    }


    /**
     * 搜索solr 游戏
     *
     * @param text
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<GameDTO> searchGameByText(String text, Pagination page, Integer version) throws ServiceException {
        PageRows<GameDTO> rows = new PageRows<GameDTO>();


        PageRows<FiledValue> gameIds = searchService.searchByType(WikiappSearchType.GAME, page.getCurPage(), page.getPageSize(), text);
        if (CollectionUtil.isEmpty(gameIds.getRows())) {
            return rows;
        }
        Set<Long> gameIdSet = new HashSet<Long>();
        for (FiledValue filedValue : gameIds.getRows()) {
            gameIdSet.add(Long.valueOf(filedValue.getKey()));
        }


        Map<Long, WikiGameres> wikiGameresMap = AskServiceSngl.get().queryWikiGameresByIds(gameIdSet);
        List<GameDTO> list = this.buildGameDTOByMap(wikiGameresMap, version);

        rows.setPage(gameIds.getPage());
        rows.setRows(list);
        return rows;
    }

    public List<CollectDTO> buildCollect(List<UserCollect> list) throws ServiceException {
        List<CollectDTO> returnList = new ArrayList<CollectDTO>();
        Set<Long> contentIds = new HashSet<Long>();
        Set<Long> gameIds = new HashSet<Long>();
        if (CollectionUtil.isEmpty(list)) {
            return returnList;
        }
        for (UserCollect userCollect : list) {
            contentIds.add(userCollect.getContentId());
        }

        Map<Long, Content> contentMap = AskServiceSngl.get().queryContentByUserCollect(contentIds);
        for (Long contentId : contentMap.keySet()) {
            Content content = contentMap.get(contentId);
            if (!content.getSource().equals(ContentSource.WIKI)) {
                gameIds.add(Long.parseLong(content.getGameId()));
            }
        }
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIds);

        for (UserCollect userCollect : list) {
            Content content = contentMap.get(userCollect.getContentId());
            if (content == null) {
                continue;
            }
            CollectDTO collectDTO = new CollectDTO();
            collectDTO.setCtype(String.valueOf(content.getSource().getCode()));
            collectDTO.setTime(userCollect.getCreateDate().getTime());
            collectDTO.setGamename(content.getTitle());
            collectDTO.setId(userCollect.getId());
            collectDTO.setJt("-2");//wap页为负2
            collectDTO.setJi(content.getWebUrl());
            if (ContentSource.WIKI.equals(content.getSource())) {
                collectDTO.setDiscussion(content.getDescription());
            } else {
                collectDTO.setDiscussion(gameDBMap.get(Long.parseLong(content.getGameId())).getGameName());
            }
            returnList.add(collectDTO);
        }

        return returnList;
    }


    public List<ApiwikiTagDTO> queryContentTagList(ContentTagLine tagLine) throws ServiceException {
        List<ApiwikiTagDTO> returnList = new ArrayList<ApiwikiTagDTO>();
        List<ContentTag> contentTagList = AskServiceSngl.get().queryContentTagByTagType(tagLine);
        if (CollectionUtil.isEmpty(contentTagList)) {
            return returnList;
        }

        for (ContentTag tag : contentTagList) {
            ApiwikiTagDTO dto = new ApiwikiTagDTO();
            dto.setTagname(tag.getName());
            dto.setJt(tag.getTagType().getCode());
            if (tag.getTagType().getCode() == ContentTagType.ARCHIVE.getCode()) {
                dto.setJi(String.valueOf(tag.getId()));
            } else {
                dto.setJi(tag.getTarget());
            }
            returnList.add(dto);
        }
        return returnList;
    }
}
