package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareBaseInfoField;
import com.enjoyf.platform.service.sync.ShareType;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/goods")
public class GoodsController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {
            PageRows<Goods> pageRows = PointServiceSngl.get().queryGoodsByPage(new QueryExpress().add(QuerySort.add(GoodsField.DISPLAYORDER, QuerySortOrder.ASC)), pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());

            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("baseInfoList", baseInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/point/goodslist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.GIFTMARKET.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("baseInfoList", baseInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        String gTemplate = templateConfig.getExchangedGTemplate();
        String vTemplate = templateConfig.getExchangedVTemplate();

        mapMessage.put("gTemplate", gTemplate);
        mapMessage.put("vTemplate", vTemplate);


        mapMessage.put("goodstypecollection", GoodsType.getAll());

        mapMessage.put("consumetimestypecollection", ConsumeTimesType.getAll());
        return new ModelAndView("/point/creategoods", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "shardid", required = true) Long shareId,
                               @RequestParam(value = "goodsname", required = false) String goodsName,
                               @RequestParam(value = "goodsdesc", required = false) String goodsDesc,
                               @RequestParam(value = "goodspic", required = false) String goodsPic,
                               @RequestParam(value = "goodstype", required = false) Integer goodsType,
                               //@RequestParam(value = "goodsexpiredate", required = false) Date goodsExpireDate,
                               @RequestParam(value = "goodsamount", required = false) Integer goodsAmount,
                               @RequestParam(value = "goodsconsumepoint", required = false) Integer goodsConsumePoint,
                               @RequestParam(value = "consumetimestype", required = false) Integer consumeTimesType,
                               @RequestParam(value = "isnew", required = false) boolean isNew,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "noticebody", required = false) String noticeBody,
                               @RequestParam(value = "detailurl", required = false) String detailUrl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<Goods> list = PointServiceSngl.get().queryGoods(new QueryExpress().add(QueryCriterions.eq(GoodsField.GOODSNAME, goodsName)));
            if (!CollectionUtil.isEmpty(list)) {
                mapMessage.put("nameExist", "goods.name.has.exist");
                return new ModelAndView("forward:/point/goods/createpage", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        Goods goods = new Goods();

        goods.setShareId(shareId == null ? 0 : shareId);
        goods.setNoticeBody(noticeBody);
        goods.setDetailUrl(detailUrl);

        goods.setGoodsName(goodsName);
        goods.setGoodsDesc(goodsDesc);
        goods.setGoodsPic(goodsPic);
        goods.setGoodsType(GoodsType.getByCode(goodsType));
        /*if (goodsExpireDate!=null) {
            goods.setGoodsExpireDate(goodsExpireDate);
        }*/
        goods.setGoodsConsumePoint(goodsConsumePoint);
        goods.setGoodsAmount(goodsAmount);
        goods.setConsumeTimesType(ConsumeTimesType.getByCode(consumeTimesType));
        goods.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        goods.setIsNew(isNew);
        goods.setIsHot(isHot);
        goods.setValidStatus(ValidStatus.VALID);
        if (GoodsType.GOODS.getCode() == goodsType) {
            goods.setGoodsResetAmount(goodsAmount);
        } else {
        }
        goods.setCreateDate(new Date());
        goods.setCreateIp(getIp());
        goods.setCreateUserId(getCurrentUser().getUserid());
        try {
            PointServiceSngl.get().addGoods(goods);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/point/goods/list", mapMessage);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "goodsid", required = true) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.GIFTMARKET.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("baseInfoList", baseInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        String gTemplate = templateConfig.getExchangedGTemplate();
        String vTemplate = templateConfig.getExchangedVTemplate();

        mapMessage.put("gTemplate", gTemplate);
        mapMessage.put("vTemplate", vTemplate);

        mapMessage.put("goodstypecollection", GoodsType.getAll());

        mapMessage.put("consumetimestypecollection", ConsumeTimesType.getAll());

        mapMessage.put("statuslist", ValidStatus.getAll());

        try {
            Goods goods = PointServiceSngl.get().getGoodsById(goodsId);
            if (goods == null) {
                mapMessage = putErrorMessage(mapMessage, "goods.goodsId.empty");
                return new ModelAndView("forward:/point/goods/list", mapMessage);
            }
            mapMessage.put("goods", goods);
            mapMessage.put("goodsid", goodsId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/point/goods/list", mapMessage);
        }
        return new ModelAndView("/point/modifygoods", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "shardid", required = true) Long shareId,
                               @RequestParam(value = "goodsid", required = true) Long goodsId,
                               @RequestParam(value = "goodsname", required = false) String goodsName,
                               @RequestParam(value = "goodsdesc", required = false) String goodsDesc,
                               @RequestParam(value = "goodspic", required = false) String goodsPic,
                               @RequestParam(value = "goodstype", required = false) Integer goodsType,
                               @RequestParam(value = "goodsamount", required = false) Integer goodsAmount,
                               @RequestParam(value = "goodsrestamount", required = false) Integer goodsRestAmount,
                               @RequestParam(value = "goodsconsumepoint", required = false) Integer goodsConsumePoint,
                               @RequestParam(value = "consumetimestype", required = false) Integer consumeTimesType,
                               @RequestParam(value = "isnew", required = false) boolean isNew,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "noticebody", required = false) String noticeBody,
                               @RequestParam(value = "detailurl", required = false) String detailUrl,
                               @RequestParam(value = "status", required = false) String status) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<Goods> list = PointServiceSngl.get().queryGoods(new QueryExpress().add(QueryCriterions.eq(GoodsField.GOODSNAME, goodsName)));
            if (!CollectionUtil.isEmpty(list) && list.get(0).getGoodsId() != goodsId) {
                mapMessage.put("nameExist", "goods.name.has.exist");
                return new ModelAndView("forward:/point/goods/modifypage", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        try {
            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(GoodsField.SHAREID, shareId);
            updateExpress.set(GoodsField.NOTICEBODY, noticeBody);
            updateExpress.set(GoodsField.DETAIL_URL, detailUrl);

            updateExpress.set(GoodsField.GOODSNAME, goodsName);
            updateExpress.set(GoodsField.GOODSDESC, goodsDesc);
            updateExpress.set(GoodsField.GOODSPIC, goodsPic);
            updateExpress.set(GoodsField.GOODSTYPE, goodsType);
            updateExpress.set(GoodsField.GOODSAMOUNT, goodsAmount);
            updateExpress.set(GoodsField.GOODSRESETAMOUNT, goodsRestAmount);
            updateExpress.set(GoodsField.GOODSCONSUMEPOINT, goodsConsumePoint);
            updateExpress.set(GoodsField.CONSUMETIMESTYPE, consumeTimesType);
            updateExpress.set(GoodsField.ISNEW, isNew);
            updateExpress.set(GoodsField.ISHOT, isHot);
            updateExpress.set(GoodsField.VALIDSTATUS, status);

            boolean bool = PointServiceSngl.get().modifyGoodsById(updateExpress, goodsId);

            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_GOODS);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("goods id:" + goodsId);

                addLog(log);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/goods/list");
    }

    //
    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "goodsid", required = true) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GoodsField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
        try {
            PointServiceSngl.get().modifyGoodsById(updateExpress, goodsId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/goods/list");
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "goodsid", required = true) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GoodsField.VALIDSTATUS, ValidStatus.VALID.getCode());
        try {
            PointServiceSngl.get().modifyGoodsById(updateExpress, goodsId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/goods/list");
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView recover(@PathVariable(value = "sort") String sort,
                                @RequestParam(value = "goodsid", required = true) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        try {
            //step1 get goodsId,get display order
            Goods goods = PointServiceSngl.get().getGoodsById(goodsId);
            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(GoodsField.DISPLAYORDER, goods.getDisplayOrder()));
                queryExpress.add(QuerySort.add(GoodsField.DISPLAYORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(GoodsField.DISPLAYORDER, goods.getDisplayOrder()));
                queryExpress.add(QuerySort.add(GoodsField.DISPLAYORDER, QuerySortOrder.ASC));
            }

            PageRows<Goods> goodsRows = PointServiceSngl.get().queryGoodsByPage(queryExpress, new Pagination(1, 1, 1));
            if (goodsRows != null && !CollectionUtil.isEmpty(goodsRows.getRows())) {
                updateExpress1.set(GoodsField.DISPLAYORDER, goods.getDisplayOrder());
                PointServiceSngl.get().modifyGoodsById(updateExpress1, goodsRows.getRows().get(0).getGoodsId());

                updateExpress2.set(GoodsField.DISPLAYORDER, goodsRows.getRows().get(0).getDisplayOrder());
                PointServiceSngl.get().modifyGoodsById(updateExpress2, goods.getGoodsId());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/goods/list");
    }

}
