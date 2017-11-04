package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCoverFieldJson;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.ShortUrlUtils;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.mobilegame.MobileGameTopDTO;
import com.enjoyf.webapps.joyme.weblogic.gamedb.GameDbWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import com.google.gdata.util.common.base.StringUtil;
import com.opensymphony.xwork2.util.URLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-1-21
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 * 手游排行榜Controller
 */
@Controller
@RequestMapping(value = "/joymeapp/gameclient/webview/game/top")
public class GameClientGameTopWebViewController extends AbstractGameClientBaseController {

    @Resource(name = "gameDbWebLogic")
    private GameDbWebLogic gameDbWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String platform = HTTPUtil.getParam(request, "platform");
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.parseInt(platform)))
                    .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.MobileGame.getCode()))
                    .add(QueryCriterions.in(ClientLineField.ITEM_TYPE, new Object[]{ClientItemType.GAMETOP.getCode(), ClientItemType.ADVERT.getCode()}))
                    .add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<ClientLine> list = JoymeAppServiceSngl.get().queryClientLineList(queryExpress);

            mapMessage.put("list", list);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/gameclient/webview/gametop/gametoplist", mapMessage);
    }

    @RequestMapping(value = "/itemlist")
    public ModelAndView gameTopDetail(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "id", required = false) String id) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, Long.parseLong(id))));
            mapMessage.put("clientLine", clientLine);
            if (clientLine == null) {
                mapMessage.put("lists", "");
                return new ModelAndView("/views/jsp/gameclient/webview/gametop/itemlist", mapMessage);
            }
            QueryExpress queryExpressItem = new QueryExpress();
            queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpressItem.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<ClientLineItem> list = JoymeAppServiceSngl.get().queryClientLineItemByQueryExpress(queryExpressItem);
            if (!CollectionUtil.isEmpty(list)) {
                Set<Long> setLong = new TreeSet<Long>();
                for (ClientLineItem clientLineItem : list) {
                    setLong.add(Long.parseLong(clientLineItem.getDirectId()));
                }
                Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(setLong);
                if (map != null) {
                    List<MobileGameTopDTO> mobileGameTopDTOList = new ArrayList<MobileGameTopDTO>();
                    for (GameDB gameDB : map.values()) {
                        MobileGameTopDTO mobileGameTopDTO = new MobileGameTopDTO();
                        mobileGameTopDTO.setGamePic(URLUtils.getJoymeDnUrl(gameDB.getGameIcon()));
                        mobileGameTopDTO.setLineId(clientLine.getLineId());
                        mobileGameTopDTO.setTitle(gameDB.getGameName());
                        mobileGameTopDTO.setReason(gameDB.getDownloadRecommend());
                        mobileGameTopDTO.setRate(StringUtil.isEmpty(gameDbWebLogic.average(gameDB)) ? 0.0 : Double.parseDouble(gameDbWebLogic.average(gameDB)));
                        mobileGameTopDTO.setGamedbId(gameDB.getGameDbId());
                        mobileGameTopDTOList.add(mobileGameTopDTO);
                    }

                    Collections.sort(mobileGameTopDTOList, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            Double d1 = ((MobileGameTopDTO) o1).getRate();
                            Double d2 = ((MobileGameTopDTO) o2).getRate();
                            return d2.compareTo(d1);
                        }
                    });


                    mapMessage.put("lists", mobileGameTopDTOList);

                }
            }
            mapMessage.put("share_url", ShortUrlUtils.getSinaURL(WebappConfig.get().URL_WWW + "/joymeapp/gameclient/webview/game/top/share?code=" + clientLine.getCode()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/gameclient/webview/gametop/itemlist", mapMessage);
    }

    @RequestMapping(value = "/share")
    public ModelAndView share(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "code", required = false) String code) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, code)));
            if (clientLine != null) {
                clientLine.setSmallpic(URLUtils.getJoymeDnUrl(clientLine.getSmallpic()));
            }
            mapMessage.put("clientLine", clientLine);
            QueryExpress queryExpressItem = new QueryExpress();
            queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()));
            queryExpressItem.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpressItem.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<ClientLineItem> list = JoymeAppServiceSngl.get().queryClientLineItemByQueryExpress(queryExpressItem);
            if (!CollectionUtil.isEmpty(list)) {
                Set<Long> setLong = new TreeSet<Long>();
                for (ClientLineItem clientLineItem : list) {
                    setLong.add(Long.parseLong(clientLineItem.getDirectId()));
                }
                Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(setLong);
                if (map != null) {
                    List<MobileGameTopDTO> mobileGameTopDTOList = new ArrayList<MobileGameTopDTO>();
                    for (GameDB gameDB : map.values()) {
                        MobileGameTopDTO mobileGameTopDTO = new MobileGameTopDTO();
                        mobileGameTopDTO.setGamePic(URLUtils.getJoymeDnUrl(gameDB.getGameIcon()));
                        mobileGameTopDTO.setLineId(clientLine.getLineId());
                        mobileGameTopDTO.setTitle(gameDB.getGameName());
                        mobileGameTopDTO.setReason(gameDB.getDownloadRecommend());
                        mobileGameTopDTO.setRate(StringUtil.isEmpty(gameDbWebLogic.average(gameDB)) ? 0.0 : Double.parseDouble(gameDbWebLogic.average(gameDB)));
                        mobileGameTopDTO.setGamedbId(gameDB.getGameDbId());
                        mobileGameTopDTOList.add(mobileGameTopDTO);
                    }
                    Collections.sort(mobileGameTopDTOList, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            Double d1 = ((MobileGameTopDTO) o1).getRate();
                            Double d2 = ((MobileGameTopDTO) o2).getRate();
                            return d2.compareTo(d1);
                        }
                    });
                    mapMessage.put("lists", mobileGameTopDTOList);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/gameclient/webview/gametop/share", mapMessage);
    }

}
