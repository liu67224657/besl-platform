package com.enjoyf.webapps.joyme.weblogic.joymeapp.gameclient;

import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.*;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.gameclient.dto.GameClientPicDTO;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCoverFieldJson;
import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDisplyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.dto.gamedb.GameDBHotPageDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientContentDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientObjectDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientTagDetailDTO;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/25
 * Description:
 */
@Service(value = "gameClientWebLogic")
public class GameClientWebLogic {


    //todo add param version
    public List<GameClientObjectDTO> buildGameClientDtO(List<ClientLineItem> clientLineItems, String version) {

        //保证轮播图的顺序
        Collections.sort(clientLineItems, new Comparator<ClientLineItem>() {
            @Override
            public int compare(ClientLineItem o1, ClientLineItem o2) {
                int jt1 = o1.getDisplayOrder();
                int jt2 = o2.getDisplayOrder();
                return jt1 > jt2 ? 1 : (o1 == o2 ? 0 : -1);
            }
        });


        List<GameClientObjectDTO> list = new ArrayList<GameClientObjectDTO>();
        for (ClientLineItem item : clientLineItems) {
            GameClientObjectDTO dto = new GameClientObjectDTO();

            dto.setPicurl(item.getPicUrl() == null ? "" : URLUtils.getJoymeDnUrl(item.getPicUrl()));
            dto.setIconurl(item.getCategory() == null ? "" : URLUtils.getJoymeDnUrl(item.getCategory()));
            dto.setDesc(item.getDesc());
            dto.setTitle(item.getTitle());
            dto.setColor(item.getCategoryColor());

            //轮播图webview链接增加appstrore
            if (item.getRedirectType().equals(AppRedirectType.REDIRECT_DOWNLOAD) || item.getRedirectType().equals(AppRedirectType.DOWNLOAD)) {
                dto.setJt(String.valueOf(AppRedirectType.REDIRECT_DOWNLOAD.getCode()));
                //CMS
            } else if (item.getRedirectType().equals(AppRedirectType.CMSARTICLE) || item.getRedirectType().equals(AppRedirectType.REDIRECT_WEBVIEW)) {
                dto.setJt(String.valueOf(AppRedirectType.REDIRECT_WEBVIEW.getCode()));
            } else {
                dto.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
            }

            dto.setJi(getArticleJiByVersion(AppUtil.getVersionInt(version), item.getUrl()));
            dto.setNum(item.getRate());

            dto.setNumcolor(item.getAuthor() == null ? "" : item.getAuthor());
            list.add(dto);
        }

        return list;
    }

    //玩霸2.0.3热门页今日推荐的dto   ,用于今日推荐和游戏分类
    public List<GameDBHotPageDTO> buildHotPageDTO(List<ClientLineItem> clientLineItems) throws Exception {
        Set<Long> directIdSet = new HashSet<Long>();

        for (int i = 0; i < clientLineItems.size(); i++) {
            Long longTemp = Long.valueOf(clientLineItems.get(i).getDirectId());
            directIdSet.add(longTemp);
        }

        //根据set查询mongodb的game库 的game_db表
        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(directIdSet);

        //添充GameDBSmallDTO 形成的List
        List<GameDBHotPageDTO> resultList = new ArrayList<GameDBHotPageDTO>();
        for (int i = 0; i < clientLineItems.size(); i++) {
            Long longTemp = Long.valueOf(clientLineItems.get(i).getDirectId());
            GameDB gameDB = gameDBMap.get(longTemp);
            if (gameDB != null) {
                GameDBHotPageDTO temp = new GameDBHotPageDTO();
                JSONObject jsonObject = JSONObject.fromObject(clientLineItems.get(i).getDesc());
                Map<String, String> map = new HashMap<String, String>();
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    String key = (String) it.next();
                    map.put(key, (String) jsonObject.get(key));
                }

                temp.setName(map.get("gameName"));
                temp.setType(map.get("gameTypeDesc"));
                temp.setJt(map.get("jt"));
                temp.setJi(map.get("ji"));
                temp.setShowtype(map.get("showType"));
                temp.setReferral(map.get("downloadRecommend"));

                if (gameDB.getModifyTime() != null && !StringUtil.isEmpty(gameDB.getModifyTime().getRedMessageText())) {
                    GameDBModifyTimeFieldJson modifyTime = gameDB.getModifyTime();
                    long timeLong = modifyTime.getRedMessageTime();
                    if (timeLong != 0) {
                        Date dateTemp = new Date(timeLong);
                        if (DateUtil.isToday(dateTemp)) {    //是今天的才更新 那个 礼活攻新 之类
                            temp.setFlag(gameDB.getModifyTime().getRedMessageText());
                        }
                    }
                } else {
                    temp.setFlag("");
                }

                temp.setTag(map.get("tag"));

                temp.setGameid(String.valueOf(gameDB.getGameDbId()));
                temp.setLikes(gameDB.getGameDBCover().getCoverAgreeNum());
                temp.setIcon(URLUtils.getJoymeDnUrl(gameDB.getGameIcon()));
                temp.setRate(average(gameDB));
                temp.setCreatetime(clientLineItems.get(i).getItemCreateDate().getTime());
                // temp.setItemid(clientLineItemMap.get(longTemp).getItemId());
                //  temp.setDisplayOrder(clientLineItemMap.get(longTemp).getDisplayOrder());
                resultList.add(temp);
            }
        }

        return resultList;
    }

    //热门页自定义楼层
    public List<JSONObject> buildHotFloorDTO(List<ClientLineItem> clientLineItems) throws Exception {

        List<JSONObject> resultList = new ArrayList<JSONObject>();
        for (int i = 0; i < clientLineItems.size(); i++) {

            JSONObject temp = new JSONObject();

            JSONObject jsonObject = JSONObject.fromObject(clientLineItems.get(i).getDesc());
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                map.put(key, (String) jsonObject.get(key));
            }
            temp.put("icon", URLUtils.getJoymeDnUrl(map.get("floorIcon")));
            temp.put("name", map.get("floorName"));
            temp.put("more", map.get("moreLink"));
            temp.put("jt", map.get("jt"));
            temp.put("ji", map.get("ji"));

            List<JSONObject> array = new ArrayList<JSONObject>();

            JSONObject pic1 = new JSONObject();
            pic1.put("pic", map.get("pic1st"));
            pic1.put("jt", map.get("jt1"));
            pic1.put("ji", map.get("ji1"));
            array.add(pic1);

            JSONObject pic2 = new JSONObject();
            pic2.put("pic", map.get("pic2nd"));
            pic2.put("jt", map.get("jt2"));
            pic2.put("ji", map.get("ji2"));
            array.add(pic2);

            JSONObject pic3 = new JSONObject();
            pic3.put("pic", map.get("pic3rd"));
            pic3.put("jt", map.get("jt3"));
            pic3.put("ji", map.get("ji3"));
            array.add(pic3);

            temp.put("body", array);
            resultList.add(temp);
        }
        return resultList;
    }


    public List<GameClientTagDetailDTO> buildTagDedearchives(Long tagid, List<TagDedearchives> tagDedearchivesList, String appkey, Pagination pagination, String channelid, Integer platform, String version, HttpServletRequest request) throws ServiceException {
        //查询广告集合
        Map<Integer, List<GameClientContentDTO>> adMap = queryAdList(appkey, AppAdvertisePublishType.GAME_CLIENT, channelid, platform);

        //版本判断
        Integer verionInt = 0;
        if (!StringUtil.isEmpty(version)) {
            verionInt = AppUtil.getVersionInt(version);
        }

        //查询tag信息
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));
        Map<Long, AnimeTag> tagMap = new HashMap<Long, AnimeTag>();
        List<AnimeTag> tagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
        for (AnimeTag tag : tagList) {
            tagMap.put(tag.getTag_id(), tag);
        }


        List<GameClientTagDetailDTO> list = new ArrayList<GameClientTagDetailDTO>();


        //阅读数、赞百分比
        Set<Long> dedeIds = new HashSet<Long>();
        //帖子id集合
        Set<String> commentIds = new HashSet<String>();
        for (TagDedearchives dedearchives : tagDedearchivesList) {
            if (ArchiveContentType.MIYOU_COMMENT.getCode() == dedearchives.getArchiveContentType().getCode()) {
                commentIds.add(dedearchives.getDede_archives_id());
            } else {
                dedeIds.add(Long.valueOf(dedearchives.getDede_archives_id()));
            }
        }

        //查询帖子的comment信息
        Map<String, CommentBean> commentBeanMap = null;
        if (verionInt >= 210) {
            commentBeanMap = CommentServiceSngl.get().queryCommentBeanByIds(commentIds);
        }

        Map<Long, TagDedearchiveCheat> cheatMap = JoymeAppServiceSngl.get().getMapTagDedearchiveCheat(dedeIds);

        int start = pagination.getStartRowIdx();
        for (int i = 0; i < tagDedearchivesList.size(); i++) {
            GameClientTagDetailDTO tag = new GameClientTagDetailDTO();

            //插入广告
            List<GameClientContentDTO> adList = adMap.get(start + i);
            if (!CollectionUtil.isEmpty(adList)) {
                GameClientTagDetailDTO adtag = new GameClientTagDetailDTO();
                adtag.setType(String.valueOf(TagDisplyType.ADVERTISING.getCode()));
                adtag.setContent(adList);
                list.add(adtag);
            }

            TagDedearchives archive = tagDedearchivesList.get(i);

            //IOS 解决2.0.1崩溃问题
            if (verionInt <= 201 && (archive.getTagDisplyType().equals(TagDisplyType.BIG_PIC_NO_TITLE) || archive.getTagDisplyType().equals(TagDisplyType.BIG_PIC_TITLE))) {
                continue;
            }

            //如果版本低于2.1.0 ，迷友帖子不显示
            if (verionInt < 210 && ArchiveContentType.MIYOU_COMMENT.equals(archive.getArchiveContentType())) {
                continue;
            }

            //迷友圈的帖子
            if (ArchiveContentType.MIYOU_COMMENT.equals(archive.getArchiveContentType()) && commentBeanMap != null) {

                CommentBean bean = commentBeanMap.get(archive.getDede_archives_id());
                if (bean == null) {
                    continue;
                }
                tag.setType(String.valueOf(TagDisplyType.SMALL_PIC.getCode()));
                tag.setArchiveid(archive.getDede_archives_id());
                GameClientContentDTO content = new GameClientContentDTO();
                content.setTitle("迷友圈：" + bean.getDescription());
                content.setPicurl(bean.getPic());
                content.setIconurl(bean.getPic());
                content.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
                content.setJi(WebappConfig.get().getUrlWww() + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + bean.getCommentId());
                content.setDesc(bean.getDescription());
                content.setAgreenum(String.valueOf(bean.getScoreCommentSum()));
                content.setPublishtime(String.valueOf(bean.getCreateTime().getTime()));
                content.setReadnum(conversionNum(bean.getLongCommentSum()));
                List<GameClientContentDTO> contentList = new ArrayList<GameClientContentDTO>();
                contentList.add(content);
                tag.setContent(contentList);
                //private String tagname;
            } else {
                tag.setType(archive.getTagDisplyType().getCode() + "");
                tag.setArchiveid(archive.getDede_archives_id());

                GameClientContentDTO content = new GameClientContentDTO();
                content.setTitle(archive.getDede_archives_title());
                content.setDesc(archive.getDede_archives_description());
                //TODO 临时替换
                if (WebappConfig.get().getDomain().contains("joyme.dev") || WebappConfig.get().getDomain().contains("joyme.test")) {
                    if (archive.getTagDisplyType().equals(TagDisplyType.SMALL_PIC)) {
                        content.setPicurl(archive.getDede_archives_litpic().replace(".joyme.com", ".enjoyf.com"));
                    } else {
                        content.setPicurl(archive.getDede_archives_htlistimg().replace(".joyme.com", ".enjoyf.com"));
                    }
                } else {
                    if (archive.getTagDisplyType().equals(TagDisplyType.SMALL_PIC)) {
                        content.setPicurl(archive.getDede_archives_litpic());
                    } else {
                        content.setPicurl(archive.getDede_archives_htlistimg());
                    }
                }
                AnimeTag animeTag = tagMap.get(tagid);
                String tagName = "";
                //String tagPic = "";
                if (tagid == -1) {
                    animeTag = tagMap.get(archive.getDisplay_tag());
                }
                if (animeTag != null) {
                    tagName = StringUtil.isEmpty(animeTag.getTag_name()) ? "" : animeTag.getTag_name();
                }

                //新版本不显示tagname
                if (HTTPUtil.isNewVersion(request) && tagid != -1) {
                    content.setTagname("");
                } else {
                    content.setTagname(tagName);
                }

                //content.setIconurl(URLUtils.getQiniuUrl(tagPic));
                content.setIconurl("http://joymepic.qiniudn.com/qiniu/original/2015/02/66/09d94c63000e5045380b67600f8d9b86f94e.png");
                if (cheatMap.get(Long.valueOf(archive.getDede_archives_id())) != null) {
                    content.setAgreenum(cheatMap.get(Long.valueOf(archive.getDede_archives_id())).getAgree_num() + "");
                    content.setReadnum(conversionNum(cheatMap.get(Long.valueOf(archive.getDede_archives_id())).getRead_num()));
                }
                if (archive.getDede_redirect_type().equals(AppRedirectType.DEFAULT_WEBVIEW)) {
                    content.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
                    content.setJi(StringUtil.isEmpty(archive.getDede_redirect_url()) ? "" : archive.getDede_redirect_url());
                } else {
                    content.setJt(String.valueOf(AppRedirectType.REDIRECT_WEBVIEW.getCode()));
                    content.setJi(getArticleJiByVersion(verionInt, archive.getDede_archives_url()));
                }

                //TODO
                if (content.getJi().contains("http://itunes.apple.com/cn/app/apple-store/id955396648")) {
                    content.setJt("-4");
                }

                content.setPublishtime(String.valueOf(archive.getDede_archives_pubdate()));
                List<GameClientContentDTO> contentList = new ArrayList<GameClientContentDTO>();
                contentList.add(content);

                tag.setContent(contentList);
            }

            list.add(tag);
        }
        return list;
    }

    private String getArticleJiByVersion(int version, String url) {
        String ji = "";
        if (version >= 210) {
            ji = StringUtil.isEmpty(url) ? "" : url.replace("syhb4", "wanba210");
        } else {
            ji = StringUtil.isEmpty(url) ? "" : url;
        }
        return ji;
    }


    private Map<Integer, List<GameClientContentDTO>> queryAdList(String appId, AppAdvertisePublishType publishType, String channelid, Integer platform) throws ServiceException {
        Map<Integer, List<GameClientContentDTO>> returnMap = new HashMap<Integer, List<GameClientContentDTO>>();
        List<AppAdvertisePublish> publishList = AdvertiseServiceSngl.get().queryAppAdvertisePublishByCache(appId, publishType, StringUtil.isEmpty(channelid) ? "" : channelid);
        if (CollectionUtil.isEmpty(publishList)) {
            return returnMap;
        }
        Set<Long> advertiseIdSet = new HashSet<Long>();
        for (AppAdvertisePublish publish : publishList) {
            advertiseIdSet.add(publish.getAdvertiseId());
        }
        Map<Long, AppAdvertise> advertiseMap = AdvertiseServiceSngl.get().queryAppAdvertiseByIdSet(advertiseIdSet, platform);

        for (AppAdvertisePublish publish : publishList) {
            AppAdvertise advertise = advertiseMap.get(publish.getAdvertiseId());
            if (advertise == null) {
                continue;
            }
            List<GameClientContentDTO> list = buildAppAdvertiseDTO(advertise, publish);
            returnMap.put(publish.getPublishParam().getNumberParam(), list);

        }
        return returnMap;
    }

    private List<GameClientContentDTO> buildAppAdvertiseDTO(AppAdvertise advertise, AppAdvertisePublish publish) {

        GameExistJson existJson = GameExistJson.parse(advertise.getExtstring());

        List<GameClientContentDTO> returnList = new ArrayList<GameClientContentDTO>();
        GameClientContentDTO dto1 = new GameClientContentDTO();
        dto1.setTitle(existJson.getTitle1());
        dto1.setDesc(existJson.getDesc1());
        dto1.setPicurl(URLUtils.getJoymeDnUrl(existJson.getPicurl1()));
        dto1.setTagname("");
        dto1.setAgreenum("");
        if (existJson.getType1().equals(String.valueOf(AppAdvertiseRedirectType.APPSTORE.getCode()))) {
            dto1.setJt(String.valueOf(AppRedirectType.REDIRECT_DOWNLOAD.getCode()));
        } else {
            dto1.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
        }
        dto1.setJi(existJson.getUrl1());
        dto1.setPublishtime("");
        returnList.add(dto1);

        GameClientContentDTO dto2 = new GameClientContentDTO();
        dto2.setTitle(existJson.getTitle2());
        dto2.setDesc(existJson.getDesc2());
        dto2.setPicurl(URLUtils.getJoymeDnUrl(existJson.getPicurl2()));
        dto2.setTagname("");
        dto2.setAgreenum("");
        if (existJson.getType2().equals(String.valueOf(AppAdvertiseRedirectType.APPSTORE.getCode()))) {
            dto2.setJt(String.valueOf(AppRedirectType.REDIRECT_DOWNLOAD.getCode()));
        } else {
            dto2.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
        }
        dto2.setJi(existJson.getUrl2());
        dto2.setPublishtime("");
        returnList.add(dto2);

        GameClientContentDTO dto3 = new GameClientContentDTO();
        dto3.setTitle(existJson.getTitle3());
        dto3.setDesc(existJson.getDesc3());
        dto3.setPicurl(URLUtils.getJoymeDnUrl(existJson.getPicurl3()));
        dto3.setTagname("");
        dto3.setAgreenum("");
        if (existJson.getType3().equals(String.valueOf(AppAdvertiseRedirectType.APPSTORE.getCode()))) {
            dto3.setJt(String.valueOf(AppRedirectType.REDIRECT_DOWNLOAD.getCode()));
        } else {
            dto3.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
        }
        dto3.setJi(existJson.getUrl3());
        dto3.setPublishtime("");
        returnList.add(dto3);

        GameClientContentDTO dto4 = new GameClientContentDTO();
        dto4.setTitle(existJson.getTitle4());
        dto4.setDesc(existJson.getDesc4());
        dto4.setPicurl(URLUtils.getJoymeDnUrl(existJson.getPicurl4()));
        dto4.setTagname("");
        dto4.setAgreenum("");
        if (existJson.getType4().equals(String.valueOf(AppAdvertiseRedirectType.APPSTORE.getCode()))) {
            dto4.setJt(String.valueOf(AppRedirectType.REDIRECT_DOWNLOAD.getCode()));
        } else {
            dto4.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
        }
        dto4.setJi(existJson.getUrl4());
        dto4.setPublishtime("");
        returnList.add(dto4);


        return returnList;
    }

    //修改tag的总数
    public void updateAnimeTagTotalSum(Long tagid, int num) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.increase(AnimeTagField.TOTAL_SUM, num);
        try {
            JoymeAppServiceSngl.get().modifyAnimeTag(tagid, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid)), updateExpress);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public List<GameClientPicDTO> queryGameClientPicDTO(String profileId) throws ServiceException {
        int page = (int) (Math.random() * 10);
        List<GameClientPicDTO> commentBeanList = CommentServiceSngl.get().queryCommentBeanByCache(page + 1);
        if (CollectionUtil.isEmpty(commentBeanList)) {
            return null;
        }
        Collections.shuffle(commentBeanList);
        List<GameClientPicDTO> returnList = new ArrayList<GameClientPicDTO>();
        for (GameClientPicDTO gameClientPicDTO : commentBeanList) {
            if (returnList.size() >= 10) {
                break;
            }
            String otherId = gameClientPicDTO.getProfiledto().getProfileid();//profileid  不能出现自己的照片
            if (StringUtil.isEmpty(otherId) || otherId.equals(profileId)) {
                continue;
            }

            returnList.add(gameClientPicDTO);
        }
        return returnList;
    }


    //保留2位小数
    private static String get2DoubleToString(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }


    public String getArticleUrl(DedeArctype dedeArctype, long time, int archiveId) {
        String typedir = dedeArctype.getTypedir().replaceAll("\\{cmspath\\}/", "");
        String namerule = dedeArctype.getNamerule();
        // {typedir}/{Y}{M}{D}/{aid}.html
        namerule = namerule.replaceAll("\\{typedir\\}", typedir);
        String date = DateUtil.convert2String(time, DateUtil.YYYYMMDD_FORMAT);
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        namerule = namerule.replaceAll("\\{Y\\}", year);
        namerule = namerule.replaceAll("\\{M\\}", month);
        namerule = namerule.replaceAll("\\{D\\}", day);
        namerule = namerule.replaceAll("\\{aid\\}", archiveId + "");

        int position = namerule.lastIndexOf("/");
        String[] paths = new String[2];
        if (position > 0) {
            paths[0] = namerule.substring(0, position);
            paths[1] = namerule.substring(position + 1, namerule.length());
        }
        String returnURL = "";
        String domain = WebappConfig.get().getDomain();
        if (domain.contains("joyme.dev") || domain.contains("joyme.test")) {
            returnURL = "http://marticle.joyme.dev/syhb4/" + paths[0] + "/" + paths[1];
            ;
        } else {
            returnURL = "http://marticle." + domain + "/syhb4/" + paths[0] + "/" + paths[1];
        }
        return returnURL;
    }


    public String conversionNum(Integer agreeNum) {
        DecimalFormat df = new DecimalFormat("######0.0");
        String returnStr = "";
        if (agreeNum < 10000) {
            returnStr = agreeNum + "";
        } else {
            returnStr = df.format(((double) agreeNum / 10000)).replace(".0", "") + "万";
        }
        return returnStr;
    }

    public void sendEventTask(TaskAction taskAction, String ip, String platform, String appkey, Profile profile, String directId, String clientId) {
        TaskAwardEvent taskEvent = new TaskAwardEvent();
//        taskEvent.setTaskId(taskid);
        taskEvent.setTaskAction(taskAction);
        taskEvent.setDoTaskDate(new Date());
        taskEvent.setProfileId(profile.getProfileId());
        taskEvent.setTaskIp(ip);
        taskEvent.setUno(profile.getUno());
        taskEvent.setAppkey(AppUtil.getAppKey(appkey));
        taskEvent.setProfileKey(profile.getProfileKey());
        taskEvent.setAppPlatform(AppPlatform.getByCode(Integer.valueOf(platform)));
        taskEvent.setClientId(clientId);
        taskEvent.setDirectId(directId);
        try {
            //GAlerter.lab("sendEventTask:taskid=" + taskid + ",ip=" + ip + ",platform=" + platform + ",appkey=" + AppUtil.getAppKey(appkey) + ",profile=" + profile.toString());
            EventDispatchServiceSngl.get().dispatch(taskEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //根据游戏资料库的5个评分，求算术平均值
    public static String average(GameDB gameDB) {
        String returnObj = "";
        GameDBCoverFieldJson fieldJson = gameDB.getGameDBCoverFieldJson();
        if (fieldJson != null) {
            double sum = Double.valueOf(fieldJson.getValue1()) + Double.valueOf(fieldJson.getValue2()) + Double.valueOf(fieldJson.getValue3()) + Double.valueOf(fieldJson.getValue4()) + Double.valueOf(fieldJson.getValue5());
            DecimalFormat df = new DecimalFormat("0.0");
            returnObj = df.format(sum / 5.0);
        }
        return returnObj;
    }

}
