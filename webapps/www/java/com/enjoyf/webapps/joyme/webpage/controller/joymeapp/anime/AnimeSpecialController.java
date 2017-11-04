package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.anime;


import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.JoymeAppHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTVField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.*;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeSpecialDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeSpecialItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/anime/special")
public class AnimeSpecialController extends AbstractAnimeBaseController {

    private JoymeAppHotdeployConfig config = HotdeployConfigFactory.get().getConfig(JoymeAppHotdeployConfig.class);

    String replyNum[] = {"-1", "0", "1"};

    @ResponseBody
    @RequestMapping(value = "/list")
    public String index(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                        @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        ResultObjectPageMsg msg = new ResultObjectPageMsg(ResultObjectPageMsg.CODE_S);
        try {
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);
            String appkey = joymeAppClientConstant.getAppkey();
            int platform = joymeAppClientConstant.getPlatform();
            String flag = getFlag();
            if (StringUtil.isEmpty(appkey)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("appkey.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            Pagination pagination = new Pagination(count * page, page, count);
            QueryExpress queryExpress = new QueryExpress();
            if (platform == 0) {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.PLATFORM, 0));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.PLATFORM, 1));
            }
            if ("true".equals(flag)) {
                queryExpress.add(QueryCriterions.ne(AnimeSpecialField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            }

            queryExpress.add(QuerySort.add(AnimeSpecialField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpress.add(QueryCriterions.eq(AnimeSpecialField.APPKEY, appkey));
            PageRows<AnimeSpecial> pageRows = JoymeAppServiceSngl.get().queryAnimeSpecialByPage(queryExpress, pagination);
            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("anime.special.list.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            List<AnimeSpecialDTO> animeSpecialDTOs = new ArrayList<AnimeSpecialDTO>();
            Random random = new Random();
            for (AnimeSpecial animeSpecial : pageRows.getRows()) {

                AnimeSpecialDTO animeSpecialDTO = new AnimeSpecialDTO();
                animeSpecialDTO.setTitle(StringUtil.isEmpty(animeSpecial.getSpecialName()) ? "" : animeSpecial.getSpecialName());
                animeSpecialDTO.setPicurl(StringUtil.isEmpty(animeSpecial.getCoverPic()) ? "" : URLUtils.getJoymeDnUrl(animeSpecial.getCoverPic()));
                animeSpecialDTO.setTips(animeSpecial.getSpecialType().getValue() + "");
                animeSpecialDTO.setTipscolor(StringUtil.isEmpty(animeSpecial.getSpecialTypeBgColor()) ? "" : animeSpecial.getSpecialTypeBgColor());
                animeSpecialDTO.setJt(animeSpecial.getAnimeRedirectType().getCode() + "");
                if (animeSpecial.getAnimeRedirectType().equals(AnimeRedirectType.DIRECT)) {
                    animeSpecialDTO.setJi(StringUtil.isEmpty(animeSpecial.getLinkUrl()) ? "" : animeSpecial.getLinkUrl());
                } else {
                    animeSpecialDTO.setJi(animeSpecial.getSpecialId().toString());
                }
                animeSpecialDTO.setDesc(StringUtil.isEmpty(animeSpecial.getSpecialDesc()) ? "" : animeSpecial.getSpecialDesc());

                //阅读数、图片的展示类型
                animeSpecialDTO.setReadnum(animeSpecial.getRead_num() + "");
                animeSpecialDTO.setDisplaytype(animeSpecial.getDisplay_type().getCode() + "");

                //TODO 评论数显占个位置
                //animeSpecialDTO.setReplynum(replyNum[random.nextInt(3)]);
                animeSpecialDTO.setReplynum("-1");

                animeSpecialDTOs.add(animeSpecialDTO);
            }
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("rows", animeSpecialDTOs);
            msg.setPage(new JsonPagination(pageRows.getPage()));
            msg.setMsg("success");
            msg.setResult(returnMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @ResponseBody
    @RequestMapping(value = "/details")
    public String details(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                          @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                          @RequestParam(value = "specialid", required = false) String specialId) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(specialId)) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("special.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AnimeSpecial animeSpecial = JoymeAppServiceSngl.get().getAnimeSpecial(new QueryExpress().add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, Long.parseLong(specialId))));
            if (animeSpecial == null) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("special.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AnimeSpecialDTO specialDTO = new AnimeSpecialDTO();
            specialDTO.setPicurl(StringUtil.isEmpty(animeSpecial.getSpecialPic()) ? "" : URLUtils.getJoymeDnUrl(animeSpecial.getSpecialPic()));
            specialDTO.setTitle(StringUtil.isEmpty(animeSpecial.getSpecialTtile()) ? "" : animeSpecial.getSpecialTtile());
            specialDTO.setDesc(StringUtil.isEmpty(animeSpecial.getSpecialDesc()) ? "" : animeSpecial.getSpecialDesc());
            specialDTO.setTipscolor("");
            specialDTO.setTips("");
            specialDTO.setJi("");
            specialDTO.setJt("");
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("headinfo", specialDTO);
            getJoymeAppClientConstant(request);
            String flag = getFlag();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.SPECIAL_ID, Long.parseLong(specialId)));
            if ("true".equals(flag)) {
                queryExpress.add(QueryCriterions.ne(AnimeSpecialItemField.REMOVE_STATUS, ValidStatus.REMOVED.getCode()));
            } else {
                queryExpress.add(QueryCriterions.eq(AnimeSpecialItemField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            }
            queryExpress.add(QuerySort.add(AnimeSpecialItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<AnimeSpecialItem> specialItems = JoymeAppServiceSngl.get().queryAnimeSpecialItem(queryExpress);
            if (CollectionUtil.isEmpty(specialItems)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("anime.special.item.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            List<AnimeSpecialItemDTO> lists = new ArrayList<AnimeSpecialItemDTO>();
            if (animeSpecial.getAnimeRedirectType().equals(AnimeRedirectType.DIVERSITY)) {
                List<Long> tvIdList = new ArrayList<Long>();
                for (AnimeSpecialItem animeSpecialItem : specialItems) {
                    tvIdList.add(animeSpecialItem.getTvId());
                }
                List<AnimeTV> animeTVs = JoymeAppServiceSngl.get().queryAnimeTV(new QueryExpress().add(QueryCriterions.in(AnimeTVField.TV_ID, tvIdList.toArray())), null, null);

                //查询出来之后需要重新排序
                Map<Long, AnimeTV> animeTVMap = new HashMap<Long, AnimeTV>();
                for (AnimeTV animeTV : animeTVs) {
                    animeTVMap.put(animeTV.getTv_id(), animeTV);
                }
                List<AnimeTV> animeTVsSort = new ArrayList<AnimeTV>();
                for (AnimeSpecialItem animeSpecialItem : specialItems) {
                    AnimeTV animeTV = animeTVMap.get(animeSpecialItem.getTvId());
                    if (animeTV != null) {
                        animeTVsSort.add(animeTV);
                    }
                }


                for (AnimeTV animeTV : animeTVsSort) {
                    AnimeSpecialItemDTO animeSpecialItemDTO = new AnimeSpecialItemDTO();
                    animeSpecialItemDTO.setPicurl(StringUtil.isEmpty(animeTV.getTv_pic()) ? "" : URLUtils.getJoymeDnUrl(animeTV.getTv_pic()));
                    animeSpecialItemDTO.setTitle(StringUtil.isEmpty(animeTV.getTv_name()) ? "" : animeTV.getTv_name());
                    animeSpecialItemDTO.setUrl(StringUtil.isEmpty(animeTV.getUrl()) ? "" : animeTV.getUrl());
                    animeSpecialItemDTO.setDomain(StringUtil.isEmpty(animeTV.getDomain().getValue()) ? "" : animeTV.getDomain().getValue());
                    animeSpecialItemDTO.setM3u8(StringUtil.isEmpty(animeTV.getM3u8()) ? "" : animeTV.getM3u8());
                    animeSpecialItemDTO.setTvid(animeTV.getTv_id() + "");
                    animeSpecialItemDTO.setTvnumber(animeTV.getTv_number() + "");
                    animeSpecialItemDTO.setDesc("");
                    animeSpecialItemDTO.setReplynum("");
                    lists.add(animeSpecialItemDTO);
                }
            } else {
                for (AnimeSpecialItem animeSpecialItem : specialItems) {
                    AnimeSpecialItemDTO animeSpecialItemDTO = new AnimeSpecialItemDTO();
                    animeSpecialItemDTO.setTitle(StringUtil.isEmpty(animeSpecialItem.getTitle()) ? "" : animeSpecialItem.getTitle());
                    animeSpecialItemDTO.setUrl(StringUtil.isEmpty(animeSpecialItem.getLinkUrl()) ? "" : animeSpecialItem.getLinkUrl());
                    animeSpecialItemDTO.setPicurl(StringUtil.isEmpty(animeSpecialItem.getPic()) ? "" : URLUtils.getJoymeDnUrl(animeSpecialItem.getPic()));
                    animeSpecialItemDTO.setDesc(StringUtil.isEmpty(animeSpecialItem.getDesc()) ? "" : animeSpecialItem.getDesc());
                    animeSpecialItemDTO.setTvid("");
                    animeSpecialItemDTO.setTvnumber("");
                    animeSpecialItemDTO.setDomain("");
                    animeSpecialItemDTO.setM3u8("");

                    String uri = animeSpecialItem.getLinkUrl();
                    if (uri.indexOf("http://") >= 0 && uri.indexOf(".joyme.com/wiki/") > 0) {
                        String key = uri.substring("http://".length(), uri.indexOf(".joyme.com/"));
                        String uniKey = key + "|" + animeSpecialItem.getTitle();
                        CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(MD5Util.Md5(uniKey + CommentDomain.UGCWIKI_COMMENT.getCode()));
                        if (commentBean != null) {
                            animeSpecialItemDTO.setReplynum(commentBean.getCommentSum() + "");
                        } else {
                            animeSpecialItemDTO.setReplynum(0 + "");
                        }

                    } else {
                        animeSpecialItemDTO.setReplynum(0 + "");
                    }
                    lists.add(animeSpecialItemDTO);
                }
            }
            returnMap.put("rows", lists);
            msg.setMsg("success");
            msg.setResult(returnMap);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }

        return JsonBinder.buildNormalBinder().toJson(msg);
    }

//    public static void main(String[] args) {
//        String uri = "http://op.joyme.com/wiki/蒙奇·D·路飞";
//        String key = uri.substring(7, uri.indexOf("."));
//        System.out.println(key);
//
//    }


    //上报阅读数 12:100,13:2
    @ResponseBody
    @RequestMapping(value = "/upread")
    public String upm3u8(@RequestParam(value = "specialid", required = false) String specialid
    ) {
        ResultObjectMsg msg = new ResultObjectMsg();
        try {
            if (StringUtil.isEmpty(specialid)) {
                msg.setRs(ResultCodeConstants.SPECIALID_NOT_EXISTS.getCode());
                msg.setMsg(ResultCodeConstants.SPECIALID_NOT_EXISTS.getMsg());
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            String arr[] = specialid.split(",");
            for (String cid : arr) {
                String cids[] = cid.split(":");
                if (cids.length != 2) {
                    continue;
                }
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AnimeSpecialField.SPECIAL_ID, Long.valueOf(cids[0])));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AnimeSpecialField.READ_NUM, Integer.valueOf(cids[1]));
                JoymeAppServiceSngl.get().modifyAnimeSpecial(queryExpress, updateExpress);
            }

            msg.setRs(ResultCodeConstants.SUCCESS.getCode());
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
}
