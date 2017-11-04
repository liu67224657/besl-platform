package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.QuestionConfig;
import com.enjoyf.platform.service.ask.QuestionConfigType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaActivityDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaShareDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.ask.QuestionConfigDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientTagDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */

@Service(value = "wanbaTagWebLogic")
public class WanbaTagWebLogic extends AbstractWanbaWebLogic {

    public List<GameClientTagDTO> getTagList(HttpServletRequest request, AnimeTagAppType animeTagAppType) {
        List<GameClientTagDTO> returnRows = new ArrayList<GameClientTagDTO>();

        //等于true为预发布环境
        String flag = HTTPUtil.getParam(request, "flag");

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, animeTagAppType.getCode()));
        if (!StringUtil.isEmpty(flag) && flag.equals("true")) {
            queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        } else {
            queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        }

        try {
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
            for (AnimeTag animeTag : animeTagList) {
                GameClientTagDTO dto = new GameClientTagDTO();
                dto.setTagid(String.valueOf(animeTag.getTag_id()));
                dto.setTagname(animeTag.getTag_name());
                String iconurl = animeTag.getPicjson() == null ? "" : animeTag.getPicjson().getIos();
                dto.setTagdesc(animeTag.getTag_desc());
                dto.setIconurl(StringUtil.isEmpty(iconurl) ? "" : URLUtils.getJoymeDnUrl(iconurl));
                //如果是web页面，tagid存放的是url地址
                if (animeTag.getPicjson() != null && GameClientTagType.WEB.getCode().equals(animeTag.getPicjson().getType())) {
                    dto.setTagid(animeTag.getPicjson().getUrl());
                }
                if (animeTag.getPicjson() != null) {
                    dto.setType(animeTag.getPicjson().getType());
                }
                returnRows.add(dto);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }

        return returnRows;
    }

    public PageRows<WanbaActivityDTO> getActivityTagList(HttpServletRequest request, Pagination pagination) {
        PageRows<WanbaActivityDTO> returnRows = new PageRows<WanbaActivityDTO>();

        //等于true为预发布环境
        String flag = HTTPUtil.getParam(request, "flag");

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.ASC));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ACTIVITY.getCode()));
        if (!StringUtil.isEmpty(flag) && flag.equals("true")) {
            queryExpress.add(QueryCriterions.ne(AnimeTagField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
        } else {
            queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        }

        try {

            PageRows<AnimeTag> pageRows = JoymeAppServiceSngl.get().queryAnimeTagByPage(queryExpress, pagination);
            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                return returnRows;
            }

            List<WanbaActivityDTO> returnList = new ArrayList<WanbaActivityDTO>();
            for (AnimeTag animeTag : pageRows.getRows()) {
                WanbaActivityDTO dto = new WanbaActivityDTO();
                dto.setTitle(animeTag.getTag_name());
                dto.setTagid(String.valueOf(animeTag.getTag_id()));

                String ch_name = animeTag.getCh_name();
                if (!StringUtil.isEmpty(ch_name)) {
                    WanbaActivity activity = WanbaActivity.toObject(ch_name);
                    dto.setPic(activity.getPic());
                    dto.setCorner(activity.getCorner());
                }

                returnList.add(dto);
            }
            returnRows.setPage(pageRows.getPage());
            returnRows.setRows(returnList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }

        return returnRows;
    }


    public WanbaActivityDTO getActivityTag(String tagid) {
        WanbaActivityDTO returnDTO = new WanbaActivityDTO();
        try {
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(Long.valueOf(tagid),
                    new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, Long.valueOf(tagid))));
            if (animeTag == null || !animeTag.getRemove_status().equals(ValidStatus.VALID)) {
                return returnDTO;
            }

            Set<String> profileIdSet = new HashSet<String>();

            WanbaShareDTO shareDTO = new WanbaShareDTO();

            String ch_name = animeTag.getCh_name();
            if (!StringUtil.isEmpty(ch_name)) {
                //基本信息
                WanbaActivity activity = WanbaActivity.toObject(ch_name);
                returnDTO.setPic(activity.getPic());
                returnDTO.setCorner(activity.getCorner());
                returnDTO.setTitle(animeTag.getTag_name());
                returnDTO.setTagid(String.valueOf(animeTag.getTag_id()));
                returnDTO.setDesc(animeTag.getTag_desc());

                //share
                shareDTO.setTitle(animeTag.getTag_name());
                shareDTO.setDesc(animeTag.getTag_desc());
                shareDTO.setPic("http://huabao.joyme.com/wapimage/wanba_icon.jpg");
                shareDTO.setUrl("http://api." + WebappConfig.get().getDomain() + "/wanba/webview/activity/listbytag?tagid=" + tagid);
                returnDTO.setShare(shareDTO);


                //组装页面用户
                profileIdSet.add(activity.getAskwho());
                List<String> guestList = activity.getGuestList();
                for (String str : guestList) {
                    profileIdSet.add(str);
                }


                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);


                //嘉宾列表
                List<WanbaProfileDTO> listProfile = new ArrayList<WanbaProfileDTO>();
                for (String str : guestList) {
                    if (StringUtil.isEmpty(str)) {
                        continue;
                    }
                    Profile prof = profileMap.get(str);
                    VerifyProfile wanbaProf = wanbaProfileMap.get(str);
                    if (prof != null && wanbaProf != null) {
                        listProfile.add(wanbaProfileDTO(prof, wanbaProf));
                    }
                }
                returnDTO.setProfiledtos(listProfile);
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }
        return returnDTO;
    }

    public List<QuestionConfigDTO> questionConfigList() {
        List<QuestionConfigDTO> returnConfigDto = new ArrayList<QuestionConfigDTO>();
        List<QuestionConfig> questionConfigList = QuestionConfigType.getAll();
        for (QuestionConfig config : questionConfigList) {
            QuestionConfigDTO dto = new QuestionConfigDTO();
            dto.setTime(String.valueOf(config.getTimeLimit()));
            dto.setScore(String.valueOf(config.getQuestionPoint()));
            dto.setTimestr(config.getTimeStr());
            returnConfigDto.add(dto);
        }
        return returnConfigDto;
    }
}
