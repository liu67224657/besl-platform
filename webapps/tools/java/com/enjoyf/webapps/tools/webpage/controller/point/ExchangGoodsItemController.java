package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-8-21
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/exchangegoodsitem/")
public class ExchangGoodsItemController extends ToolsBaseController {
    private static final String OPTION_TYPE = "2";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "goodsid", required = false) Long goodsId,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "subject", required = false) String subject) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            QueryExpress queryExpress2 = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_TYPE, GoodsType.VIRTUAL.getCode()));
            if (!StringUtil.isEmpty(subject)) {
                queryExpress2.add(QueryCriterions.like(ActivityGoodsField.ACTIVITY_SUBJECT, "%" + subject + "%"));
            }

            List<ActivityGoods> goodsList = PointServiceSngl.get().listActivityGoods(queryExpress2);
            mapMessage.put("goodsList", goodsList);
            mapMessage.put("goodsId", goodsId);
            mapMessage.put("subject", subject);
            if (goodsId == null) {
                return new ModelAndView("/point/exchangegoodsitemlist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ID, goodsId));
            if (!StringUtil.isEmpty(status)) {
                queryExpress.add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_STATUS, status));
                mapMessage.put("status", status);
            }
            PageRows<ExchangeGoodsItem> pageRows = PointServiceSngl.get().queryExchangeGoodsItemByPage(queryExpress, pagination);
            Map<String, Profile> mapProfile = null;
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                mapProfile = new HashMap<String, Profile>();
                List<ExchangeGoodsItem> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getProfileId())) {
                        unos.add(list.get(i).getProfileId());
                    }
                }

                mapProfile = UserCenterServiceSngl.get().queryProfiles(unos);
            }


            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("mapProfile", mapProfile);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/point/exchangegoodsitemlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "goodsid", required = false) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ActivityGoods goods = PointServiceSngl.get().getActivityGoods(goodsId);
            mapMessage.put("goods", goods);
            mapMessage.put("goodsId", goodsId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/point/exchangegoodsitem/list?goodsid=" + goodsId, mapMessage);
        }

        return new ModelAndView("/point/batchcreateexchangegoodsitem", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "goodsid", required = true) Long goodsId,
                               @RequestParam(value = "snname1", required = true) String snName1,
                               @RequestParam(value = "snvalue1", required = true) String snValue1,
                               @RequestParam(value = "snname2", required = false) String snName2,
                               @RequestParam(value = "snvalue2", required = false) String snValue2,
                               @RequestParam(value = "optiontype", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ActivityGoods goods = PointServiceSngl.get().getActivityGoods(goodsId);

            if (goods == null) {
                mapMessage.put("goodsName", goods.getActivitySubject());
                mapMessage.put("goodsId", goods.getActivityGoodsId());
                return new ModelAndView("redirect:/point/exchangegoodsitem/list?goodsid=" + goodsId, mapMessage);
            }

            String[] snValue1Array = snValue1.split("\n");
            String[] snValue2Array = null;
            if (!StringUtil.isEmpty(snValue2)) {
                snValue2Array = snValue2.split("\n");
                if (snValue1Array.length != snValue2Array.length) {
                    mapMessage.put("errorMsg", "value1.length.noteq.value2");
                    return new ModelAndView("/point/batchcreateexchangegoodsitem", mapMessage);
                }

                if (StringUtil.isEmpty(snName2)) {
                    mapMessage.put("errorMsg", "value2.name.empty");
                    return new ModelAndView("/point/batchcreateexchangegoodsitem", mapMessage);
                }
            }

            List<ExchangeGoodsItem> goodsItemList = new ArrayList<ExchangeGoodsItem>();
            for (int i = 0; i < snValue1Array.length; i++) {
                ExchangeGoodsItem goodsItem = new ExchangeGoodsItem();

                goodsItem.setGoodsId(goodsId);
                goodsItem.setSnName1(snName1);
                goodsItem.setSnValue1(snValue1Array[i].replace("\r", "").trim());

                if (!CollectionUtil.isEmpty(snValue2Array)) {
                    goodsItem.setSnName2(snName2);
                    goodsItem.setSnValue2(snValue2Array[i].replace("\r", "").trim());
                }

                goodsItem.setExchangeStatus(ActStatus.UNACT);
                goodsItem.setCreateTime(new Date());
                goodsItemList.add(goodsItem);
            }

            int result = PointServiceSngl.get().addExchangeGoodsItem(goodsItemList);
            UpdateExpress updateExpress = new UpdateExpress().set(ActivityGoodsField.GOODS_RESETAMOUNT, goods.getGoodsResetAmount() + result);
            if (type == OPTION_TYPE || OPTION_TYPE.equals(type)) {
                updateExpress.set(ActivityGoodsField.GOODS_AMOUNT, goods.getGoodsAmount() + result);
            }

            PointServiceSngl.get().modifyActivityGoods(goodsId, updateExpress, null);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/point/exchangegoodsitem/list?goodsid=" + goodsId);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "itemid[]", required = false) Long[] goodsItemId,
                         @RequestParam(value = "goodsid", required = false) Long goodsId) {
        JsonBinder binder = JsonBinder.buildNormalBinder();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            PageRows<ExchangeGoodsItem> pageRows = PointServiceSngl.get().queryExchangeGoodsItemByPage(new QueryExpress().add(QueryCriterions.in(ExchangeGoodsItemField.EXCHANGE_GOODS_ITEM_ID, goodsItemId)), new Pagination(40, 1, 40));
            List<ExchangeGoodsItem> list = pageRows.getRows();
            Set<Long> goodsIdSet = new HashSet<Long>();
            for (ExchangeGoodsItem exchangeGoodsItem : list) {
                if (exchangeGoodsItem.getExchangeStatus().equals(ActStatus.UNACT)) {
                    goodsIdSet.add(exchangeGoodsItem.getGoodsItemId());
                }
            }
            if (!CollectionUtil.isEmpty(goodsIdSet)) {
                boolean bool = PointServiceSngl.get().modifyExchangeItemGoods(new UpdateExpress()
                        .set(ExchangeGoodsItemField.EXCHANGE_STATUS, ActStatus.REJECTED.getCode())
                        .set(ExchangeGoodsItemField.EXCHANGE_TIME, new Date()).set(ExchangeGoodsItemField.OWN_USER_NO, getCurrentUser().getUsername()),
                        new QueryExpress().add(QueryCriterions.in(ExchangeGoodsItemField.EXCHANGE_GOODS_ITEM_ID, goodsIdSet.toArray())));
                if (bool) {
                    PointServiceSngl.get().modifyActivityGoods(goodsId,
                            new UpdateExpress().increase(ActivityGoodsField.GOODS_RESETAMOUNT, -goodsIdSet.size()), null);
                }
            }
        } catch (ServiceException e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return binder.toJson(resultObjectMsg);
        }

        return binder.toJson(resultObjectMsg);

    }
}
