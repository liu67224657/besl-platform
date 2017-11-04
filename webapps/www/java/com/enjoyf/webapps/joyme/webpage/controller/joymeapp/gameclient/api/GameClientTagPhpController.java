package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientTagDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli
 * Date: 2014/12/22
 * Time: 10:34
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/tagphp")
public class GameClientTagPhpController extends AbstractGameClientBaseController {
    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;

    //获取文章所选标签
    @ResponseBody
    @RequestMapping(value = "/getarticle")
    public String getarticle(HttpServletRequest request,
                             @RequestParam(value = "archivesid", required = false) String archivesid) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archivesid));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.TAG_RELATION.getCode()));
            //queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<TagDedearchives> tagDedearchivesList = JoymeAppServiceSngl.get().queryTagDedearchives(queryExpress);
            List<Long> checked = new ArrayList<Long>();
            for (int i = 0; i < tagDedearchivesList.size(); i++) {
                TagDedearchives tag = tagDedearchivesList.get(i);
                checked.add(tag.getTagid());
            }

            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.eq(TagDedearchivesFiled.ID, MD5Util.Md5("-1" + archivesid)));
            TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(qu);

            msg.setMsg("success");
            msg.setRs(ResultObjectMsg.CODE_S);
            HashMap map = new HashMap();
            map.put("checkedtag", checked);
            map.put("displaytag", tagDedearchives == null ? "" : tagDedearchives.getDisplay_tag());


            //标签显示
            QueryExpress queryTag = new QueryExpress();
            queryTag.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            //queryTag.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            queryTag.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()));

            //全部标签
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryTag);
            map.put("rows", buildGameClientTagDTO(animeTagList));

            msg.setResult(map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return "getarticlecallback([" + JsonBinder.buildNormalBinder().toJson(msg) + "])";
    }


    //上报文章所选标签
    @ResponseBody
    @RequestMapping(value = "/updatearticle")
    public String updatearticle(HttpServletRequest request,
                                @RequestParam(value = "archivesid", required = false) String archivesid,
                                @RequestParam(value = "tags", required = false) String tags,
                                @RequestParam(value = "displaytag", required = false) String displaytag) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.valueOf(archivesid));
            if (archive != null && (archive.getTypeid() == 367 || archive.getTypeid() == 368) && archive.getArcrank() == 0) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archivesid));
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.TAG_RELATION.getCode()));
                //queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
                List<TagDedearchives> tagDedearchivesList = JoymeAppServiceSngl.get().queryTagDedearchives(queryExpress);

                Map<Long, TagDedearchives> checkedTag = new HashMap<Long, TagDedearchives>();
                Map<Long, TagDedearchives> defaultTag = new HashMap<Long, TagDedearchives>();
                for (int i = 0; i < tagDedearchivesList.size(); i++) {
                    TagDedearchives tag = tagDedearchivesList.get(i);
                    checkedTag.put(tag.getTagid(), tag);
                    if (tag.getDisplay_tag() != 0) {
                        defaultTag.put(tag.getTagid(), tag);
                    }
                }


                //TODO 测试环境下图片地址替换
//                if (!StringUtil.isEmpty(archive.getHtlistimg()) && WebappConfig.get().getUrlWww().contains("test") || WebappConfig.get().getUrlWww().contains("dev") || WebappConfig.get().getUrlWww().contains("alpha")) {
//                    archive.setHtlistimg(archive.getHtlistimg().replace("joyme", "enjoyf"));
//                }
                long currTimeLong = new Date().getTime();
                String tagArr[] = tags.split(",");
                for (int i = 0; i < tagArr.length; i++) {
                    Long tag = Long.valueOf(tagArr[i]);
                    if (checkedTag.containsKey(tag)) {
                        checkedTag.remove(tag);
                        QueryExpress q = new QueryExpress();
                        q.add(QueryCriterions.eq(TagDedearchivesFiled.ID, MD5Util.Md5("" + tag + archive.getArchiveId())));
                        UpdateExpress u = new UpdateExpress();
                        // u.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.INVALID.getCode());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, archive.getTitle());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_DESCRIPTION, archive.getDesc());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_LITPIC, archive.getIcon());
                        //u.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, archive.getCreateTime().getTime());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, archive.getShowios());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, archive.getShowandroid());
                        u.set(TagDedearchivesFiled.DISPLAY_TAG, StringUtil.isEmpty(displaytag) ? 0L : Long.valueOf(displaytag));

                        //通栏图片
                        if (!StringUtil.isEmpty(archive.getHtlistimg())) {
                            u.set(TagDedearchivesFiled.DEDE_ARCHIVES_HTLISTIMG, archive.getHtlistimg());
                        }


                        //跳转类型
                        if (!StringUtil.isEmpty(archive.getWebviewUrl())) {
                            u.set(TagDedearchivesFiled.DEDE_ARCHIVES_URL, archive.getWebviewUrl());
                            u.set(TagDedearchivesFiled.DEDE_REDIRECT_URL, archive.getWebviewUrl());
                            u.set(TagDedearchivesFiled.DEDE_REDIRECT_TYPE, AppRedirectType.DEFAULT_WEBVIEW.getCode());
                        }
                        JoymeAppServiceSngl.get().modifyTagDedearchives(tag, String.valueOf(archive.getArchiveId()), q, u, ArchiveRelationType.TAG_RELATION);
                    } else {
                        TagDedearchives inserTag = new TagDedearchives();
                        inserTag.setId(MD5Util.Md5("" + tag + archive.getArchiveId()));
                        inserTag.setDede_archives_title(archive.getTitle());
                        inserTag.setDede_archives_description(archive.getDesc());
                        inserTag.setDede_archives_litpic(archive.getIcon());
                        inserTag.setDisplay_order(System.currentTimeMillis());
                        inserTag.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                        inserTag.setTagid(tag);
                        inserTag.setDisplay_tag(StringUtil.isEmpty(displaytag) ? 0L : Long.valueOf(displaytag));
                        inserTag.setDede_archives_pubdate(currTimeLong);
                        inserTag.setDede_archives_showios(archive.getShowios());
                        inserTag.setDede_archives_showandroid(archive.getShowandroid());
                        if (!StringUtil.isEmpty(archive.getHtlistimg())) {
                            inserTag.setDede_archives_htlistimg(archive.getHtlistimg());
                        }
                        inserTag.setRemove_status(ValidStatus.INVALID);
                        DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                        if (arctype != null) {
                            String dedearchiv_url = gameClientWebLogic.getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId());
                            inserTag.setDede_archives_url(dedearchiv_url);
                        }

                        //跳转类型
                        if (!StringUtil.isEmpty(archive.getWebviewUrl())) {
                            inserTag.setDede_redirect_url(archive.getWebviewUrl());
                            inserTag.setDede_archives_url(archive.getWebviewUrl());
                            inserTag.setDede_redirect_type(AppRedirectType.DEFAULT_WEBVIEW);
                        }

                        JoymeAppServiceSngl.get().createTagDedearchives(inserTag);


                        //阅读数赞的百分比
                        TagDedearchiveCheat tagDedearchiveCheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(Long.valueOf(archive.getArchiveId()));
                        if (tagDedearchiveCheat == null) {
                            TagDedearchiveCheat cheat = new TagDedearchiveCheat();
                            int readNum = (int) (Math.random() * 2000) + 2000;
                            cheat.setRead_num(readNum);
                            cheat.setAgree_num(readNum / 100);
                            cheat.setAgree_percent(0.0);
                            cheat.setCheating_time(new Date());
                            cheat.setDede_archives_id(Long.valueOf(archive.getArchiveId()));
                            JoymeAppServiceSngl.get().createTagDedearchiveCheat(cheat);
                        }

                        //修改tag的总数
                        //gameClientWebLogic.updateAnimeTagTotalSum(tag, 1);
                    }


                }


                //最新标签
                QueryExpress qu = new QueryExpress();
                qu.add(QueryCriterions.eq(TagDedearchivesFiled.ID, MD5Util.Md5("-1" + archivesid)));
                TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(qu);
                if (tagDedearchives == null && !StringUtil.isEmpty(displaytag) && checkedTag.containsKey("-1")) {
                    TagDedearchives inserTag = new TagDedearchives();
                    inserTag.setId(MD5Util.Md5("-1" + archive.getArchiveId()));
                    inserTag.setDede_archives_title(archive.getTitle());
                    inserTag.setDede_archives_description(archive.getDesc());
                    inserTag.setDede_archives_litpic(archive.getIcon());
                    inserTag.setDisplay_order(System.currentTimeMillis());
                    inserTag.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                    inserTag.setTagid(-1L);
                    inserTag.setDisplay_tag(StringUtil.isEmpty(displaytag) ? 0L : Long.valueOf(displaytag));
                    inserTag.setDede_archives_pubdate(currTimeLong);
                    inserTag.setDede_archives_showios(archive.getShowios());
                    inserTag.setDede_archives_showandroid(archive.getShowandroid());
                    if (!StringUtil.isEmpty(archive.getHtlistimg())) {
                        inserTag.setDede_archives_htlistimg(archive.getHtlistimg());
                    }
                    inserTag.setRemove_status(ValidStatus.INVALID);
                    DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                    if (archive != null) {
                        String dedearchiv_url = gameClientWebLogic.getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId());
                        inserTag.setDede_archives_url(dedearchiv_url);
                    }

                    //跳转类型
                    if (!StringUtil.isEmpty(archive.getWebviewUrl())) {
                        inserTag.setDede_redirect_url(archive.getWebviewUrl());
                        inserTag.setDede_archives_url(archive.getWebviewUrl());
                        inserTag.setDede_redirect_type(AppRedirectType.DEFAULT_WEBVIEW);
                    }
                    JoymeAppServiceSngl.get().createTagDedearchives(inserTag);

                    //阅读数赞的百分比
                    TagDedearchiveCheat tagDedearchiveCheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(Long.valueOf(archive.getArchiveId()));
                    if (tagDedearchiveCheat == null) {
                        TagDedearchiveCheat cheat = new TagDedearchiveCheat();
                        int readNum = (int) (Math.random() * 2000) + 2000;
                        cheat.setRead_num(readNum);
                        cheat.setAgree_num(readNum / 100);
                        cheat.setAgree_percent(0.0);
                        cheat.setCheating_time(new Date());
                        cheat.setDede_archives_id(Long.valueOf(archive.getArchiveId()));
                        JoymeAppServiceSngl.get().createTagDedearchiveCheat(cheat);
                    }

                    //修改tag的总数
                    //gameClientWebLogic.updateAnimeTagTotalSum(-1L, 1);
                } else if (tagDedearchives != null) {
                    UpdateExpress u = new UpdateExpress();
                    //u.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, archive.getTitle());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_DESCRIPTION, archive.getDesc());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_LITPIC, archive.getIcon());
                    //u.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, archive.getCreateTime().getTime());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWIOS, archive.getShowios());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_SHOWANDROID, archive.getShowandroid());
                    u.set(TagDedearchivesFiled.DISPLAY_TAG, StringUtil.isEmpty(displaytag) ? 0L : Long.valueOf(displaytag));
                    if (!StringUtil.isEmpty(archive.getHtlistimg())) {
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_HTLISTIMG, archive.getHtlistimg());
                    }
                    //跳转类型
                    if (!StringUtil.isEmpty(archive.getWebviewUrl())) {
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_URL, archive.getWebviewUrl());
                        u.set(TagDedearchivesFiled.DEDE_REDIRECT_URL, archive.getWebviewUrl());
                        u.set(TagDedearchivesFiled.DEDE_REDIRECT_TYPE, AppRedirectType.DEFAULT_WEBVIEW.getCode());
                    }

                    JoymeAppServiceSngl.get().modifyTagDedearchives(-1L, String.valueOf(archive.getArchiveId()), qu, u, ArchiveRelationType.GAME_RELATION);

                }


                if (!CollectionUtil.isEmpty(checkedTag)) {
                    for (Long tag : checkedTag.keySet()) {
                        if (defaultTag.get(tag).getRemove_status().equals(ValidStatus.REMOVED)) {
                            continue;
                        }
                        QueryExpress q = new QueryExpress();
                        q.add(QueryCriterions.eq(TagDedearchivesFiled.ID, MD5Util.Md5("" + tag + archive.getArchiveId())));
                        UpdateExpress u = new UpdateExpress();
                        u.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
                        JoymeAppServiceSngl.get().modifyTagDedearchives(tag, String.valueOf(archive.getArchiveId()), q, u, ArchiveRelationType.GAME_RELATION);
                        //修改tag的总数
                        gameClientWebLogic.updateAnimeTagTotalSum(tag, -1);
                    }
                }
                msg.setRs(ResultObjectMsg.CODE_S);
                msg.setMsg("success");
                msg.setResult("");
            } else {
                msg.setRs(ResultObjectMsg.CODE_S);
                msg.setMsg("arichtype.is.367.368");
                msg.setResult("");
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return "updatearticlecallback([" + JsonBinder.buildNormalBinder().toJson(msg) + "])";

    }

    private List<GameClientTagDTO> buildGameClientTagDTO(List<AnimeTag> animeTagList) {
        List<GameClientTagDTO> used = new ArrayList<GameClientTagDTO>();
        if (!CollectionUtil.isEmpty(animeTagList)) {
            for (int i = 0; i < animeTagList.size(); i++) {
                AnimeTag animeTag = animeTagList.get(i);
                GameClientTagDTO dto = new GameClientTagDTO();
                dto.setTagid(animeTag.getTag_id() + "");
                dto.setTagname(animeTag.getTag_name());
                // dto.setSelected("");
                dto.setIconurl("");
                used.add(dto);
            }
        }
        return used;
    }

}
