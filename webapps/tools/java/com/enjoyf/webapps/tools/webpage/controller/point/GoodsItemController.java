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
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-31
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/goodsitem")
public class GoodsItemController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "goodsid", required = false) Long goodsId,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "subject", required = false) String subject) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            QueryExpress queryExpress2 = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_TYPE, GoodsType.VIRTUAL.getCode()));
            if (!StringUtil.isEmpty(subject)) {
                queryExpress2.add(QueryCriterions.like(ActivityGoodsField.ACTIVITY_SUBJECT, "%" + subject + "%"));
            }
            List<ActivityGoods> goodsList = PointServiceSngl.get().listActivityGoods(queryExpress2);
            mapMessage.put("goodsList", goodsList);
            mapMessage.put("goodsId", goodsId);
            mapMessage.put("subject", subject);
            mapMessage.put("status", status);

            if (goodsId == null) {
                return new ModelAndView("/point/goodsitemlist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GoodsItemField.GOODSID, goodsId));
            if(!StringUtil.isEmpty(status)){
                queryExpress.add(QueryCriterions.eq(GoodsItemField.EXCHANGE_STATUS, status));
            }
            PageRows<GoodsItem> pageRows = PointServiceSngl.get().queryGoodsItemByPage(queryExpress, pagination);

            Set<String> unos = new HashSet<String>();
            for (GoodsItem goodsItem : pageRows.getRows()) {
                unos.add(goodsItem.getProfileId());
            }
            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(unos);
            mapMessage.put("profileMap", profileMap == null ? null : profileMap.values());


            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/point/goodsitemlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "goodsid", required = false) Long goodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ActivityGoods goods = PointServiceSngl.get().getActivityGoods(goodsId);
            mapMessage.put("goods", goods);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/point/goodsitem/list?goodsid=" + goodsId, mapMessage);
        }

        return new ModelAndView("/point/batchcreategoodsitem", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "goodsid", required = true) Long goodsId,
                               @RequestParam(value = "snname1", required = true) String snName1,
                               @RequestParam(value = "snvalue1", required = true) String snValue1,
                               @RequestParam(value = "snname2", required = false) String snName2,
                               @RequestParam(value = "snvalue2", required = false) String snValue2) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            ActivityGoods goods = PointServiceSngl.get().getActivityGoods(goodsId);

            if (goods == null) {
                mapMessage.put("goodsName", goods.getActivitySubject());
                mapMessage.put("goodsId", goods.getActivityGoodsId());
                return new ModelAndView("redirect:/point/goodsitem/list?goodsid=" + goodsId, mapMessage);
            }

            int allowCreateNum = goods.getGoodsAmount() - goods.getGoodsResetAmount();

            String[] snValue1Array = snValue1.split("\n");
            if (snValue1Array.length > allowCreateNum) {
                return new ModelAndView("redirect:/point/goodsitem/createpage");
            }
            String[] snValue2Array = null;
            if (!StringUtil.isEmpty(snValue2)) {
                snValue2Array = snValue2.split("\n");
                if (snValue1Array.length != snValue2Array.length) {
                    mapMessage.put("errorMsg", "value1.length.noteq.value2");
                    return new ModelAndView("/point/batchcreategoodsitem", mapMessage);
                }

                if (StringUtil.isEmpty(snName2)) {
                    mapMessage.put("errorMsg", "value2.name.empty");
                    return new ModelAndView("/point/batchcreategoodsitem", mapMessage);
                }
            }

            List<GoodsItem> goodsItemList = new ArrayList<GoodsItem>();
            for (int i = 0; i < snValue1Array.length; i++) {
                GoodsItem goodsItem = new GoodsItem();

                goodsItem.setGoodsId(goodsId);
                goodsItem.setSnName1(snName1);
                goodsItem.setSnValue1(snValue1Array[i].replace("\r", "").trim());

                if (!CollectionUtil.isEmpty(snValue2Array)) {
                    goodsItem.setSnName2(snName2);
                    goodsItem.setSnValue2(snValue2Array[i].replace("\r", "").trim());
                }

                goodsItem.setExchangeStatus(ActStatus.UNACT);
                goodsItem.setCreateDate(new Date());
                goodsItemList.add(goodsItem);
            }

            int result = PointServiceSngl.get().addGoodsItem(goodsItemList);

            PointServiceSngl.get().modifyActivityGoods(goodsId, new UpdateExpress().increase(ActivityGoodsField.GOODS_RESETAMOUNT, result), null);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/point/goodsitem/list?goodsid=" + goodsId);
    }
}
