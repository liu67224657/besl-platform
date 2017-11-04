package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
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
@RequestMapping(value = "/point/consumelog")
public class UserConsumeLogController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "goodsactiontype", required = false) String goodsActionType,
                             @RequestParam(value = "validstatus", required = false) String validStatus,
                             @RequestParam(value = "startdate", required = false) Date startDate,
                             @RequestParam(value = "enddate", required = false) Date endDate) {
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("shopTypes", GoodsActionType.getAll());
            GoodsActionType goodsAction = GoodsActionType.MIDOU;

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_TYPE, GoodsType.GOODS.getCode()));
            if (!StringUtil.isEmpty(goodsActionType)) {
                mapMessage.put("goodsActionType", goodsActionType);
                queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ACTION_TYPE, Integer.parseInt(goodsActionType)));
            }
            if (!StringUtil.isEmpty(validStatus)) {
                mapMessage.put("status", validStatus);
                queryExpress.add(QueryCriterions.eq(UserConsumeLogField.VALID_STATUS, validStatus));
            }
            queryExpress.add(QuerySort.add(UserConsumeLogField.USER_CONSUME_LOG_ID, QuerySortOrder.DESC));

            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            mapMessage.put("applist", appList);
            PageRows<UserConsumeLog> userConsumeLogPageRows = PointServiceSngl.get().queryConsumeLogByPage(queryExpress, pagination, goodsAction);
            if (userConsumeLogPageRows != null) {
                mapMessage.put("page", userConsumeLogPageRows.getPage());
                mapMessage.put("rows", userConsumeLogPageRows.getRows());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/consumeloglist", mapMessage);
    }
}