package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.EncodeUtils;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhitaoshi on 2015/7/24.
 */
@Controller
@RequestMapping(value = "/point/goodsseckill")
public class GoodsSeckillController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "50") int pageSize,
                             @RequestParam(value = "qtitle", required = false) String qTitle,
                             @RequestParam(value = "qgid", required = false) String qGid,
                             @RequestParam(value = "qgat", required = false) Integer qGat,
                             @RequestParam(value = "qstart", required = false) String qStart,
                             @RequestParam(value = "qend", required = false) String qEnd,
                             @RequestParam(value = "qstatus", required = false) Integer qStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qTitle", qTitle);
        map.put("qGid", qGid);
        map.put("qGat", qGat);
        map.put("qStart", qStart);
        map.put("qEnd", qEnd);
        map.put("qStatus", qStatus);

        map.put("actionTypes", GoodsActionType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(qTitle)) {
            queryExpress.add(QueryCriterions.like(GoodsSeckillField.SECKILL_TITLE, "%" + qTitle + "%"));
        }
        if (!StringUtil.isEmpty(qGid)) {
            queryExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ID, qGid));
        }
        if (qGat != null) {
            queryExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, qGat));
        }
        if (!StringUtil.isEmpty(qStart) || !StringUtil.isEmpty(qEnd)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtil.isEmpty(qStart)) {
                try {
                    queryExpress.add(QueryCriterions.geq(GoodsSeckillField.START_TIME, df.parse(qStart + " 00:00:00")));
                } catch (ParseException e) {
                    GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
                    map = putErrorMessage(map, "system.error");
                }
            }
            if (!StringUtil.isEmpty(qEnd)) {
                try {
                    queryExpress.add(QueryCriterions.leq(GoodsSeckillField.END_TIME, df.parse(qEnd + " 23:59:59")));
                } catch (ParseException e) {
                    GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
                    map = putErrorMessage(map, "system.error");
                }
            }
        } else {
            queryExpress.add(QueryCriterions.geq(GoodsSeckillField.END_TIME, new Date()));
        }
        if (qStatus != null) {
            queryExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, qStatus));
        }
        queryExpress.add(QuerySort.add(GoodsSeckillField.START_TIME, QuerySortOrder.ASC));
        try {
            PageRows<GoodsSeckill> pageRows = PointServiceSngl.get().queryGoodsSeckillByPage(queryExpress, pagination);
            map.put("list", pageRows == null ? null : pageRows.getRows());
            map.put("page", pageRows == null ? null : pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
        }
        return new ModelAndView("/point/seckill/seckilllist", map);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "qtitle", required = false) String qTitle,
                                   @RequestParam(value = "qgid", required = false) String qGid,
                                   @RequestParam(value = "qgat", required = false) Integer qGat,
                                   @RequestParam(value = "qstart", required = false) String qStart,
                                   @RequestParam(value = "qend", required = false) String qEnd,
                                   @RequestParam(value = "qstatus", required = false) Integer qStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qTitle", qTitle);
        map.put("qGid", qGid);
        map.put("qGat", qGat);
        map.put("qStart", qStart);
        map.put("qEnd", qEnd);
        map.put("qStatus", qStatus);

        map.put("actionTypes", GoodsActionType.getAll());
        return new ModelAndView("/point/seckill/createseckill", map);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "qtitle", required = false) String qTitle,
                               @RequestParam(value = "qgid", required = false) String qGid,
                               @RequestParam(value = "qgat", required = false) Integer qGat,
                               @RequestParam(value = "qstart", required = false) String qStart,
                               @RequestParam(value = "qend", required = false) String qEnd,
                               @RequestParam(value = "qstatus", required = false) Integer qStatus,

                               @RequestParam(value = "goodsid", required = false) String goodsId,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               @RequestParam(value = "total", required = false) Integer total,
                               @RequestParam(value = "beforetips", required = false) String beforeTips,
                               @RequestParam(value = "intips", required = false) String inTips,
                               @RequestParam(value = "aftertips", required = false) String afterTips,
                               @RequestParam(value = "actiontype", required = false) Integer actionType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qTitle", qTitle);
        map.put("qGid", qGid);
        map.put("qGat", qGat);
        map.put("qStart", qStart);
        map.put("qEnd", qEnd);
        map.put("qStatus", qStatus);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GoodsSeckill goodsSeckill = new GoodsSeckill();
        goodsSeckill.setGoodsId(goodsId);
        goodsSeckill.setSeckillTitle(title);
        goodsSeckill.setSeckillDesc(desc);
        goodsSeckill.setSeckillPic(pic);
        goodsSeckill.setSeckillTotal(total);
        goodsSeckill.setSeckillSum(total);

        SeckillTips tips = new SeckillTips();
        tips.setBeforeTips(beforeTips);
        tips.setInTips(inTips);
        tips.setAfterTips(afterTips);
        goodsSeckill.setSeckillTips(tips);
        try {
            goodsSeckill.setStartTime(df.parse(startTime));
            goodsSeckill.setEndTime(df.parse(endTime));
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
            map.put("goodsId", goodsId);
            map.put("title", title);
            map.put("desc", desc);
            map.put("pic", pic);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("total", total);
            map.put("beforeTips", beforeTips);
            map.put("inTips", inTips);
            map.put("afterTips", afterTips);
            map.put("actionType", actionType);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/createseckill", map);
        }
        if(total <= 0){
            map = putErrorMessage(map, "goods.seckill.total.more.0");
            map.put("goodsId", goodsId);
            map.put("title", title);
            map.put("desc", desc);
            map.put("pic", pic);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("total", total);
            map.put("beforeTips", beforeTips);
            map.put("inTips", inTips);
            map.put("afterTips", afterTips);
            map.put("actionType", actionType);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/createseckill", map);
        }
        try {
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(Long.valueOf(goodsId));
            if (activityGoods == null) {
                map = putErrorMessage(map, "activity.goods.null");
                map.put("goodsId", goodsId);
                map.put("title", title);
                map.put("desc", desc);
                map.put("pic", pic);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
                map.put("total", total);
                map.put("beforeTips", beforeTips);
                map.put("inTips", inTips);
                map.put("afterTips", afterTips);
                map.put("actionType", actionType);
                map.put("actionTypes", GoodsActionType.getAll());
                return new ModelAndView("/point/seckill/createseckill", map);
            } else {
                goodsSeckill.setGoodsActionType(activityGoods.getGoodsActionType());
            }
            //没结束的批次
            QueryExpress seckillExpress = new QueryExpress();
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ID, goodsId));
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, activityGoods.getGoodsActionType().getCode()));
            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.END_TIME, new Date()));
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode()));
            List<GoodsSeckill> list = PointServiceSngl.get().queryGoodsSeckill(seckillExpress);
            int hasTotal = 0;
            for (GoodsSeckill seckill : list) {
                hasTotal += seckill.getSeckillSum();
            }
            //剩余库存
            //实物
            if (activityGoods.getActivitygoodsType().equals(GoodsType.GOODS)) {
                if ((activityGoods.getGoodsResetAmount() - hasTotal) < total) {
                    map = putErrorMessage(map, "goods.item.not.enough");
                    map.put("restsum", activityGoods.getGoodsResetAmount() - hasTotal);
                    map.put("goodsId", goodsId);
                    map.put("title", title);
                    map.put("desc", desc);
                    map.put("pic", pic);
                    map.put("startTime", startTime);
                    map.put("endTime", endTime);
                    map.put("total", total);
                    map.put("beforeTips", beforeTips);
                    map.put("inTips", inTips);
                    map.put("afterTips", afterTips);
                    map.put("actionType", actionType);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/createseckill", map);
                }
            } else {
                List<GoodsItem> items = PointServiceSngl.get().queryGoodsItem(new QueryExpress()
                        .add(QueryCriterions.eq(GoodsItemField.GOODSID, Long.valueOf(goodsId)))
                        .add(QueryCriterions.eq(GoodsItemField.EXCHANGE_STATUS, ActStatus.UNACT.getCode())));
                if (CollectionUtil.isEmpty(items)) {
                    map = putErrorMessage(map, "goods.item.null");
                    map.put("goodsId", goodsId);
                    map.put("title", title);
                    map.put("desc", desc);
                    map.put("pic", pic);
                    map.put("startTime", startTime);
                    map.put("endTime", endTime);
                    map.put("total", total);
                    map.put("beforeTips", beforeTips);
                    map.put("inTips", inTips);
                    map.put("afterTips", afterTips);
                    map.put("actionType", actionType);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/createseckill", map);
                }
                if ((items.size() - hasTotal) < total) {
                    map = putErrorMessage(map, "goods.item.not.enough");
                    map.put("restsum", items.size() - hasTotal);
                    map.put("goodsId", goodsId);
                    map.put("title", title);
                    map.put("desc", desc);
                    map.put("pic", pic);
                    map.put("startTime", startTime);
                    map.put("endTime", endTime);
                    map.put("total", total);
                    map.put("beforeTips", beforeTips);
                    map.put("inTips", inTips);
                    map.put("afterTips", afterTips);
                    map.put("actionType", actionType);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/createseckill", map);
                }
            }
            goodsSeckill.setRemoveStatus(IntRemoveStatus.USED);
            goodsSeckill.setCreateDate(new Date());
            goodsSeckill.setCreateUser(getCurrentUser().getUserid());
            PointServiceSngl.get().createGoodsSeckill(goodsSeckill);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
            map.put("goodsId", goodsId);
            map.put("title", title);
            map.put("desc", desc);
            map.put("pic", pic);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("total", total);
            map.put("beforeTips", beforeTips);
            map.put("inTips", inTips);
            map.put("afterTips", afterTips);
            map.put("actionType", actionType);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/createseckill", map);
        }
        return new ModelAndView("redirect:/point/goodsseckill/list?qtitle=" + EncodeUtils.urlEncode(qTitle) + "&qgid=" + qGid + (qGat == null ? "" : "&qgat=" + qGat) + "&qstart=" + qStart + "&qend=" + qEnd + (qStatus == null ? "" : "&qstatus=" + qStatus));
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "seckillid", required = false) Long seckillId,

                                   @RequestParam(value = "qtitle", required = false) String qTitle,
                                   @RequestParam(value = "qgid", required = false) String qGid,
                                   @RequestParam(value = "qgat", required = false) Integer qGat,
                                   @RequestParam(value = "qstart", required = false) String qStart,
                                   @RequestParam(value = "qend", required = false) String qEnd,
                                   @RequestParam(value = "qstatus", required = false) Integer qStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qTitle", qTitle);
        map.put("qGid", qGid);
        map.put("qGat", qGat);
        map.put("qStart", qStart);
        map.put("qEnd", qEnd);
        map.put("qStatus", qStatus);

        map.put("actionTypes", GoodsActionType.getAll());
        try {
            GoodsSeckill goodsSeckill = PointServiceSngl.get().getGoodsSeckill(new QueryExpress().add(QueryCriterions.eq(GoodsSeckillField.SECKILL_ID, seckillId)));
            map.put("goodsSeckill", goodsSeckill);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
        }
        return new ModelAndView("/point/seckill/modifyseckill", map);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "qtitle", required = false) String qTitle,
                               @RequestParam(value = "qgid", required = false) String qGid,
                               @RequestParam(value = "qgat", required = false) Integer qGat,
                               @RequestParam(value = "qstart", required = false) String qStart,
                               @RequestParam(value = "qend", required = false) String qEnd,
                               @RequestParam(value = "qstatus", required = false) Integer qStatus,

                               @RequestParam(value = "seckillid", required = false) Long seckillId,
                               @RequestParam(value = "goodsid", required = false) String goodsId,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               @RequestParam(value = "total", required = false) Integer total,
                               @RequestParam(value = "seckillsum", required = false) Integer seckillSum,
                               @RequestParam(value = "beforetips", required = false) String beforeTips,
                               @RequestParam(value = "intips", required = false) String inTips,
                               @RequestParam(value = "aftertips", required = false) String afterTips,
                               @RequestParam(value = "incrtotal", required = false,defaultValue = "0") Integer incrTotal) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qTitle", qTitle);
        map.put("qGid", qGid);
        map.put("qGat", qGat);
        map.put("qStart", qStart);
        map.put("qEnd", qEnd);
        map.put("qStatus", qStatus);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GoodsSeckillField.GOODS_ID, goodsId);
        updateExpress.set(GoodsSeckillField.SECKILL_TITLE, title);
        updateExpress.set(GoodsSeckillField.SECKILL_DESC, desc);
        updateExpress.set(GoodsSeckillField.SECKILL_PIC, pic);
        if (incrTotal != null) {
            updateExpress.increase(GoodsSeckillField.SECKILL_TOTAL, incrTotal);
            updateExpress.increase(GoodsSeckillField.SECKILL_SUM, incrTotal);
        }

        SeckillTips tips = new SeckillTips();
        tips.setBeforeTips(beforeTips);
        tips.setInTips(inTips);
        tips.setAfterTips(afterTips);
        updateExpress.set(GoodsSeckillField.SECKILL_TIPS, tips.toJson());
        try {
            start = df.parse(startTime);
            end = df.parse(endTime);
            updateExpress.set(GoodsSeckillField.START_TIME, start);
            updateExpress.set(GoodsSeckillField.END_TIME, end);
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
            GoodsSeckill goodsSeckill = new GoodsSeckill();
            goodsSeckill.setSeckillId(seckillId);
            goodsSeckill.setGoodsId(goodsId);
            goodsSeckill.setSeckillTitle(title);
            goodsSeckill.setSeckillDesc(desc);
            goodsSeckill.setSeckillPic(pic);
            goodsSeckill.setSeckillTotal(total);
            goodsSeckill.setSeckillSum(seckillSum);
            goodsSeckill.setSeckillTips(tips);
            goodsSeckill.setStartTime(start);
            goodsSeckill.setEndTime(end);
            map.put("goodsSeckill", goodsSeckill);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/modifyseckill", map);
        }
        if(incrTotal != null && (total + incrTotal) <= 0){
            map = putErrorMessage(map, "goods.seckill.total.more.0");
            GoodsSeckill goodsSeckill = new GoodsSeckill();
            goodsSeckill.setSeckillId(seckillId);
            goodsSeckill.setGoodsId(goodsId);
            goodsSeckill.setSeckillTitle(title);
            goodsSeckill.setSeckillDesc(desc);
            goodsSeckill.setSeckillPic(pic);
            goodsSeckill.setSeckillTotal(total);
            goodsSeckill.setSeckillSum(seckillSum);
            goodsSeckill.setSeckillTips(tips);
            goodsSeckill.setStartTime(start);
            goodsSeckill.setEndTime(end);
            map.put("goodsSeckill", goodsSeckill);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/modifyseckill", map);
        }
        if(incrTotal != null && (seckillSum + incrTotal) < 0){
            map = putErrorMessage(map, "goods.seckill.secsum.more.0");
            GoodsSeckill goodsSeckill = new GoodsSeckill();
            goodsSeckill.setSeckillId(seckillId);
            goodsSeckill.setGoodsId(goodsId);
            goodsSeckill.setSeckillTitle(title);
            goodsSeckill.setSeckillDesc(desc);
            goodsSeckill.setSeckillPic(pic);
            goodsSeckill.setSeckillTotal(total);
            goodsSeckill.setSeckillSum(seckillSum);
            goodsSeckill.setSeckillTips(tips);
            goodsSeckill.setStartTime(start);
            goodsSeckill.setEndTime(end);
            map.put("goodsSeckill", goodsSeckill);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/modifyseckill", map);
        }
        try {
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(Long.valueOf(goodsId));
            if (activityGoods == null) {
                map = putErrorMessage(map, "activity.goods.null");
                GoodsSeckill goodsSeckill = new GoodsSeckill();
                goodsSeckill.setSeckillId(seckillId);
                goodsSeckill.setGoodsId(goodsId);
                goodsSeckill.setSeckillTitle(title);
                goodsSeckill.setSeckillDesc(desc);
                goodsSeckill.setSeckillPic(pic);
                goodsSeckill.setSeckillTotal(total);
                goodsSeckill.setSeckillSum(seckillSum);
                goodsSeckill.setSeckillTips(tips);
                goodsSeckill.setStartTime(start);
                goodsSeckill.setEndTime(end);
                map.put("goodsSeckill", goodsSeckill);
                map.put("actionTypes", GoodsActionType.getAll());
                return new ModelAndView("/point/seckill/modifyseckill", map);
            } else {
                updateExpress.set(GoodsSeckillField.GOODS_ACTION_TYPE, activityGoods.getGoodsActionType().getCode());
            }
            //没结束的批次
            QueryExpress seckillExpress = new QueryExpress();
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ID, goodsId));
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, activityGoods.getGoodsActionType().getCode()));
            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.END_TIME, new Date()));
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode()));
            List<GoodsSeckill> list = PointServiceSngl.get().queryGoodsSeckill(seckillExpress);
            int hasTotal = 0;
            for (GoodsSeckill seckill : list) {
                hasTotal += seckill.getSeckillSum();
            }
            //剩余库存
            //实物
            if (activityGoods.getActivitygoodsType().equals(GoodsType.GOODS)) {
                if (incrTotal != null && (activityGoods.getGoodsResetAmount() - hasTotal) <  incrTotal) {
                    map = putErrorMessage(map, "goods.item.not.enough");
                    map.put("restsum", activityGoods.getGoodsResetAmount() - hasTotal);
                    GoodsSeckill goodsSeckill = new GoodsSeckill();
                    goodsSeckill.setSeckillId(seckillId);
                    goodsSeckill.setGoodsId(goodsId);
                    goodsSeckill.setSeckillTitle(title);
                    goodsSeckill.setSeckillDesc(desc);
                    goodsSeckill.setSeckillPic(pic);
                    goodsSeckill.setSeckillTotal(total);
                    goodsSeckill.setSeckillSum(seckillSum);
                    goodsSeckill.setSeckillTips(tips);
                    goodsSeckill.setStartTime(start);
                    goodsSeckill.setEndTime(end);
                    map.put("goodsSeckill", goodsSeckill);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/modifyseckill", map);
                }
            } else {
                List<GoodsItem> items = PointServiceSngl.get().queryGoodsItem(new QueryExpress()
                        .add(QueryCriterions.eq(GoodsItemField.GOODSID, Long.valueOf(goodsId)))
                        .add(QueryCriterions.eq(GoodsItemField.EXCHANGE_STATUS, ActStatus.UNACT.getCode())));
                if (CollectionUtil.isEmpty(items)) {
                    map = putErrorMessage(map, "goods.item.null");
                    GoodsSeckill goodsSeckill = new GoodsSeckill();
                    goodsSeckill.setSeckillId(seckillId);
                    goodsSeckill.setGoodsId(goodsId);
                    goodsSeckill.setSeckillTitle(title);
                    goodsSeckill.setSeckillDesc(desc);
                    goodsSeckill.setSeckillPic(pic);
                    goodsSeckill.setSeckillTotal(total);
                    goodsSeckill.setSeckillSum(seckillSum);
                    goodsSeckill.setSeckillTips(tips);
                    goodsSeckill.setStartTime(start);
                    goodsSeckill.setEndTime(end);
                    map.put("goodsSeckill", goodsSeckill);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/modifyseckill", map);
                }
                if (incrTotal != null && (items.size() - hasTotal) < incrTotal) {
                    map = putErrorMessage(map, "goods.item.not.enough");
                    map.put("restsum", items.size() - hasTotal);
                    GoodsSeckill goodsSeckill = new GoodsSeckill();
                    goodsSeckill.setSeckillId(seckillId);
                    goodsSeckill.setGoodsId(goodsId);
                    goodsSeckill.setSeckillTitle(title);
                    goodsSeckill.setSeckillDesc(desc);
                    goodsSeckill.setSeckillPic(pic);
                    goodsSeckill.setSeckillTotal(total);
                    goodsSeckill.setSeckillSum(seckillSum);
                    goodsSeckill.setSeckillTips(tips);
                    goodsSeckill.setStartTime(start);
                    goodsSeckill.setEndTime(end);
                    map.put("goodsSeckill", goodsSeckill);
                    map.put("actionTypes", GoodsActionType.getAll());
                    return new ModelAndView("/point/seckill/modifyseckill", map);
                }
            }

            PointServiceSngl.get().modifyGoodsSeckill(seckillId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            map = putErrorMessage(map, "system.error");
            GoodsSeckill goodsSeckill = new GoodsSeckill();
            goodsSeckill.setSeckillId(seckillId);
            goodsSeckill.setGoodsId(goodsId);
            goodsSeckill.setSeckillTitle(title);
            goodsSeckill.setSeckillDesc(desc);
            goodsSeckill.setSeckillPic(pic);
            goodsSeckill.setSeckillTotal(total);
            goodsSeckill.setSeckillSum(seckillSum);
            goodsSeckill.setSeckillTips(tips);
            goodsSeckill.setStartTime(start);
            goodsSeckill.setEndTime(end);
            map.put("goodsSeckill", goodsSeckill);
            map.put("actionTypes", GoodsActionType.getAll());
            return new ModelAndView("/point/seckill/modifyseckill", map);
        }
        return new ModelAndView("redirect:/point/goodsseckill/list?qtitle=" + EncodeUtils.urlEncode(qTitle) + "&qgid=" + qGid + (qGat == null ? "" : "&qgat=" + qGat) + "&qstart=" + qStart + "&qend=" + qEnd + (qStatus == null ? "" : "&qstatus=" + qStatus));
    }

    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "seckillid", required = false) Long seckillId,

                               @RequestParam(value = "qtitle", required = false) String qTitle,
                               @RequestParam(value = "qgid", required = false) String qGid,
                               @RequestParam(value = "qgat", required = false) Integer qGat,
                               @RequestParam(value = "qstart", required = false) String qStart,
                               @RequestParam(value = "qend", required = false) String qEnd,
                               @RequestParam(value = "qstatus", required = false) Integer qStatus) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.REMOVE.getCode());
        updateExpress.set(GoodsSeckillField.MODIFY_USER, getCurrentUser().getUserid());
        updateExpress.set(GoodsSeckillField.MODIFY_DATE, new Date());
        try {
            PointServiceSngl.get().modifyGoodsSeckill(seckillId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
        return new ModelAndView("redirect:/point/goodsseckill/list?qtitle=" + EncodeUtils.urlEncode(qTitle) + "&qgid=" + qGid + (qGat == null ? "" : "&qgat=" + qGat) + "&qstart=" + qStart + "&qend=" + qEnd + (qStatus == null ? "" : "&qstatus=" + qStatus));
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "seckillid", required = false) Long seckillId,

                                @RequestParam(value = "qtitle", required = false) String qTitle,
                                @RequestParam(value = "qgid", required = false) String qGid,
                                @RequestParam(value = "qgat", required = false) Integer qGat,
                                @RequestParam(value = "qstart", required = false) String qStart,
                                @RequestParam(value = "qend", required = false) String qEnd,
                                @RequestParam(value = "qstatus", required = false) Integer qStatus) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode());
        updateExpress.set(GoodsSeckillField.MODIFY_USER, getCurrentUser().getUserid());
        updateExpress.set(GoodsSeckillField.MODIFY_DATE, new Date());
        try {
            PointServiceSngl.get().modifyGoodsSeckill(seckillId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
        return new ModelAndView("redirect:/point/goodsseckill/list?qtitle=" + EncodeUtils.urlEncode(qTitle) + "&qgid=" + qGid + (qGat == null ? "" : "&qgat=" + qGat) + "&qstart=" + qStart + "&qend=" + qEnd + (qStatus == null ? "" : "&qstatus=" + qStatus));
    }

    @ResponseBody
    @RequestMapping(value = "/getgoods")
    public String getGoods(@RequestParam(value = "goodsname", required = false) String goodsName,
                           @RequestParam(value = "actiontype", required = false) Integer actionType) {
        try {
            PageRows<ActivityGoods> list = PointServiceSngl.get().queryActivityGoodsByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, ChooseType.YES.getCode()))       //秒杀
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))  //商品
                    .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, actionType))                //商城
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.UNACT.getCode()))      //可用
                    .add(QueryCriterions.like(ActivityGoodsField.ACTIVITY_SUBJECT, "%" + goodsName + "%")), null);
            JSONObject jsonObject = new JSONObject();
            if (list != null) {
                jsonObject.put("result", list.getRows());
            }
            jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

}
