package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeShareDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeTagDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeTvDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/anime")
public class AnimeTagController extends AbstractAnimeBaseController {

    //追番首页
    @ResponseBody
    @RequestMapping(value = "/tag")
    public String tag(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "tagid", required = false) String tagid
    ) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            if (StringUtil.isEmpty(tagid)) {
                msg.setRs(ResultCodeConstants.TAGID_NOT_EXISTS.getCode());
                msg.setMsg(ResultCodeConstants.TAGID_NOT_EXISTS.getMsg());
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }


            //公共参数
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);

            //分页
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeTagField.PARENT_TAG_ID, Long.valueOf(tagid)));

            //等于true为预发布环境
            if (!StringUtil.isEmpty(getFlag()) && getFlag().equals("true")) {
                queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            }
            queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);


            Map<String, Object> map = buildAnimeTagDTO(animeTagList, joymeAppClientConstant);


            msg.setRs(ResultObjectMsg.CODE_S);
            msg.setMsg("success");
            msg.setResult(map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }


    ////追番详情页
    @ResponseBody
    @RequestMapping(value = "/tagdetail")
    public String tagdetail(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "tagid", required = false) String tagid
    ) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            //公共参数
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);


            if (StringUtil.isEmpty(tagid)) {
                msg.setRs(ResultCodeConstants.TAGID_NOT_EXISTS.getCode());
                msg.setMsg(ResultCodeConstants.TAGID_NOT_EXISTS.getMsg());
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QuerySort.add(AnimeTVField.DISPLAY_ORDER, QuerySortOrder.DESC));
            queryExpress.add(QueryCriterions.like(AnimeTVField.TAGS, "%," + tagid + ",%"));
            //等于true为预发布环境
            if (!StringUtil.isEmpty(getFlag()) && getFlag().equals("true")) {
                queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            }
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(Long.valueOf(tagid), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, Long.valueOf(tagid))));
            if (animeTag == null) {
                msg.setRs(0);
                msg.setMsg("animetag.is.null");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            List<AnimeTV> animeTVList = JoymeAppServiceSngl.get().queryAnimeTV(queryExpress, Long.valueOf(tagid), getFlag());

            Map<String, Object> map = buildAnimeTvDTO(animeTVList, animeTag);

            msg.setRs(ResultObjectMsg.CODE_S);
            msg.setMsg("success");
            msg.setResult(map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    //上报m3u8
    @ResponseBody
    @RequestMapping(value = "/upm3u8")
    public String upm3u8(@RequestParam(value = "tvid", required = false) String tv_id,
                         @RequestParam(value = "m3u8", required = false) String m3u8
    ) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            if (StringUtil.isEmpty(tv_id)) {
                msg.setRs(ResultCodeConstants.TVID_NOT_EXISTS.getCode());
                msg.setMsg(ResultCodeConstants.TVID_NOT_EXISTS.getMsg());
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            if (StringUtil.isEmpty(m3u8)) {
                msg.setRs(ResultCodeConstants.M3U8_NOT_EXISTS.getCode());
                msg.setMsg(ResultCodeConstants.M3U8_NOT_EXISTS.getMsg());
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(AnimeTVField.TV_ID, Long.valueOf(tv_id)));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AnimeTVField.M3U8, m3u8);
            JoymeAppServiceSngl.get().modifyAnimeTV(queryExpress, updateExpress, null);

            msg.setRs(ResultObjectMsg.CODE_S);
            msg.setMsg("success");
            msg.setResult("");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    //上报m3u8
    @ResponseBody
    @RequestMapping(value = "/getshare")
    public String getshare(HttpServletRequest request,
                           @RequestParam(value = "sharetype", required = false) String sharetype,
                           @RequestParam(value = "id", required = false) String id) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            //公共参数
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);

            String appkey = joymeAppClientConstant.getAppkey();
            int platform = joymeAppClientConstant.getPlatform();

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialShareField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(SocialShareField.APPKEY, appkey));
            queryExpress.add(QueryCriterions.eq(SocialShareField.PLATFORM, platform));

            List<Long> activityids = new ArrayList<Long>();
            activityids.add(-1L);

            int sharetypeInt = Integer.valueOf(sharetype);
            long idLong = StringUtil.isEmpty(id) ? -1L : Long.valueOf(id);
            //专题
            if (sharetypeInt == SocialShareType.ANIME_SPECIAL_TYPE.getCode()) {
                activityids.add(Long.valueOf(id));
                queryExpress.add(QueryCriterions.in(SocialShareField.ACTIVITYID, activityids.toArray()));
                queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, SocialShareType.ANIME_SPECIAL_TYPE.getCode()));
            } else {
                //视频
                idLong = -1L;
                queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, sharetypeInt));
            }


            List<SocialShare> list = JoymeAppServiceSngl.get().querySocialShare(queryExpress, appkey,
                    platform, SocialShareType.getByCode(sharetypeInt), idLong);


            List<AnimeShareDTO> returnDTO = buildAnimeShareDTO(list);
            msg.setMsg("success");
            msg.setRs(ResultObjectMsg.CODE_S);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", returnDTO == null ? "" : returnDTO);
            msg.setResult(map);
//			for (SocialShare socialShare : list) {
//				System.out.println(socialShare);
//			}
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }


    private Map<String, Object> buildAnimeTvDTO(List<AnimeTV> animeTVList, AnimeTag animeTag) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<AnimeTvDTO> returnDtoList = new ArrayList<AnimeTvDTO>();
        for (AnimeTV animeTV : animeTVList) {
            AnimeTvDTO animeTvDTO = new AnimeTvDTO();
            animeTvDTO.setTvid(animeTV.getTv_id() + "");
            animeTvDTO.setReserved(StringUtil.isEmpty(animeTag.getReserved()) ? "" : animeTag.getReserved());
            animeTvDTO.setTitle(StringUtil.isEmpty(animeTV.getTv_name()) ? "" : animeTV.getTv_name());
            animeTvDTO.setTvnumber(animeTV.getTv_number() + "");
            animeTvDTO.setPicurl(StringUtil.isEmpty(animeTV.getTv_pic()) ? "" : animeTV.getTv_pic());
            animeTvDTO.setUrl(StringUtil.isEmpty(animeTV.getUrl()) ? "" : animeTV.getUrl().replace("http://pad.tv.sohu.com", "http://m.tv.sohu.com"));
            animeTvDTO.setM3u8(StringUtil.isEmpty(animeTV.getM3u8()) ? "" : animeTV.getM3u8());
            animeTvDTO.setDomain(animeTV.getDomain() == null ? "其他" : AnimeTVDomain.mapValue.get(Integer.valueOf(animeTV.getDomain().getCode())));
            animeTvDTO.setLatest(animeTV.getAnimeTvIsNewType().getCode() + "");
            returnDtoList.add(animeTvDTO);
        }
        map.put("rows", returnDtoList);
        return map;
    }

    private Map<String, Object> buildAnimeTagDTO(List<AnimeTag> animeTagList, JoymeAppClientConstant joymeAppClientConstant) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<AnimeTagDTO> returnDtoList = new ArrayList<AnimeTagDTO>();
        List<AnimeTagDTO> theatreDtoList = new ArrayList<AnimeTagDTO>();
        if (!CollectionUtil.isEmpty(animeTagList)) {

            for (int i = 0; i < animeTagList.size(); i++) {
                AnimeTag animeTag = animeTagList.get(i);
                AnimeTagDTO dto = new AnimeTagDTO();
                dto.setTagid(animeTag.getTag_id() + "");
                dto.setTagname(StringUtil.isEmpty(animeTag.getTag_name()) ? "" : animeTag.getTag_name());
                dto.setReserved(StringUtil.isEmpty(animeTag.getReserved()) ? "" : animeTag.getReserved());
                if (joymeAppClientConstant.getPlatform() == AppPlatform.IOS.getCode()) {
                    dto.setPicurl(StringUtil.isEmpty(animeTag.getPicjson().getIos()) ? "" : animeTag.getPicjson().getIos());
                } else {
                    dto.setPicurl(StringUtil.isEmpty(animeTag.getPicjson().getAndroid()) ? "" : animeTag.getPicjson().getAndroid());
                }

                //第一个为剧场版
                if (i == 0) {
                    theatreDtoList.add(dto);
                } else {
                    returnDtoList.add(dto);
                }

            }
        }
        map.put("rows", returnDtoList);
        map.put("theatre", theatreDtoList);
        return map;
    }

    private List<AnimeShareDTO> buildAnimeShareDTO(List<SocialShare> list) {
        List<AnimeShareDTO> returnObj = new ArrayList<AnimeShareDTO>();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        for (SocialShare socialShare : list) {
            AnimeShareDTO animeShareDTO = new AnimeShareDTO();
            animeShareDTO.setSharedomain(socialShare.getSharedomain() == null ? "" : socialShare.getSharedomain().getCode());
            animeShareDTO.setTitle(com.enjoyf.util.StringUtil.isEmpty(socialShare.getTitle()) ? "" : socialShare.getTitle());
            animeShareDTO.setDesc(com.enjoyf.util.StringUtil.isEmpty(socialShare.getBody()) ? "" : socialShare.getBody());
            animeShareDTO.setPicurl(com.enjoyf.util.StringUtil.isEmpty(socialShare.getPic_url()) ? "" : socialShare.getPic_url());
            animeShareDTO.setUrl(com.enjoyf.util.StringUtil.isEmpty(socialShare.getUrl()) ? "" : socialShare.getUrl());
            if (socialShare.getActivityid() == -1) {
                animeShareDTO.setIsdefault("true");
            } else {
                animeShareDTO.setIsdefault("false");
            }
            returnObj.add(animeShareDTO);
        }
        return returnObj;
    }
}
