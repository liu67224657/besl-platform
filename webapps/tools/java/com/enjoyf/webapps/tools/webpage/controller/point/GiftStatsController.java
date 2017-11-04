package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@RequestMapping(value = "/gift/stats")
public class GiftStatsController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") Integer pageSize,
                             @RequestParam(value = "exchangedomain", required = false) String exchangeDomain,
                             @RequestParam(value = "startdate", required = false) String startDate,
                             @RequestParam(value = "enddate", required = false) String endDate) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
                return new ModelAndView("/point/giftstats", mapMessage);
            }
            QueryExpress queryExpress = new QueryExpress();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtil.isEmpty(exchangeDomain)) {
                queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_DOMAIN, exchangeDomain));
                mapMessage.put("exchangedomain", exchangeDomain);
            }
            if (!StringUtil.isEmpty(startDate)) {
                Date date = sdf.parse(startDate);
                queryExpress.add(QueryCriterions.geq(UserExchangeLogField.EXCHANGE_TIME, date));
                mapMessage.put("startdate", date);
            }
            if (!StringUtil.isEmpty(endDate)) {
                Date date = sdf.parse(endDate);
                queryExpress.add(QueryCriterions.leq(UserExchangeLogField.EXCHANGE_TIME, date));
                mapMessage.put("enddate", date);
            }

            queryExpress.add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC));
//            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            int listExchangeLog = PointServiceSngl.get().queryUserExchangeByDate(queryExpress);
            mapMessage.put("rn", listExchangeLog);

            List<ExchangeGoods> listExchangeGoods = PointServiceSngl.get().listExchangeGoods(new QueryExpress());
            if (CollectionUtil.isEmpty(listExchangeGoods)) {
                mapMessage.put("cn", 0);
                mapMessage.put("sn", 0);
            } else {
                int cn = 0;
                int sn = 0;
                for (ExchangeGoods exchanegGoods : listExchangeGoods) {
                    cn += exchanegGoods.getGoodsAmount();
                    sn += exchanegGoods.getGoodsResetAmount();
                }
                mapMessage.put("cn", cn);
                mapMessage.put("sn", sn);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/giftstats", mapMessage);
    }
}