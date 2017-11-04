package com.enjoyf.webapps.tools.webpage.controller.viewline;

import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;
import com.enjoyf.platform.service.viewline.ViewLineItemField;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.util.sql.QueryCriterionRelation;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.HttpUpload;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.tools.weblogic.viewline.ViewLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-6-27
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/json/viewline")
public class JsonViewLineController {
    private static final String PREV = "prev";
    private static final String NEXT = "next";
    private static final String TOP = "top";
    private static final String NOTOP = "notop";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "viewLineWebLogic")
    private ViewLineWebLogic viewLineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //@ResponseBody表示该方法返回的结果直接输入到ResponseBody中去
    @ResponseBody
    @RequestMapping("/paixu")
    public String paiXu(@RequestParam(value = "lineItemId", required = false) String lineItemId,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "top", required = false) String top) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp : lineItemId=" + lineItemId + ", type= " + type);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        try {
            if (!StringUtil.isEmpty(lineItemId) && !StringUtil.isEmpty(type)) {

                //
                ViewLineItem thisItem = viewLineWebLogic.getLineItemByItemId(Long.parseLong(lineItemId));
                if (thisItem != null) {
                    //
                    QueryExpress queryExpress = new QueryExpress();
                    queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, thisItem.getLineId()));

                    if (TOP.equals(top)) {

                        queryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE,
                                QueryCriterionRelation.EQ, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));
                    } else if (NOTOP.equals(top)) {
                        queryExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE,
                                QueryCriterionRelation.LT, ViewLineItemDisplayType.TOP, ViewLineItemDisplayType.TOP));
                    }


                    if (type.equals(PREV)) {
                        queryExpress.add(QueryCriterions.lt(ViewLineItemField.DISPLAYORDER, thisItem.getDisplayOrder()))
                                .add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.DESC));

                    } else if (type.equals(NEXT)) {
                        queryExpress.add(QueryCriterions.gt(ViewLineItemField.DISPLAYORDER, thisItem.getDisplayOrder()))
                                .add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC));
                    }

                    //
                    RangeRows<ViewLineItem> rangeRows = viewLineWebLogic.queryLineItems(queryExpress, new Rangination(0, 1));
                    ViewLineItem viewLineItem = null;
                    boolean result = false;
                    if (rangeRows.getRows().size() > 0) {
                        viewLineItem = rangeRows.getRows().get(0);

                        //
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(ViewLineItemField.DISPLAYORDER, viewLineItem.getDisplayOrder());
                        QueryExpress thisQuery = new QueryExpress();
                        thisQuery.add(QueryCriterions.eq(ViewLineItemField.ITEMID, Long.parseLong(lineItemId)));

                        boolean thisSuccess = viewLineWebLogic.modifyLineItem(updateExpress, thisQuery);

                        //
                        UpdateExpress updateExpress2 = new UpdateExpress();
                        updateExpress2.set(ViewLineItemField.DISPLAYORDER, thisItem.getDisplayOrder());
                        QueryExpress upOrDownQuery = new QueryExpress();
                        upOrDownQuery.add(QueryCriterions.eq(ViewLineItemField.ITEMID, viewLineItem.getItemId()));

                        boolean upOrDownSuccess = viewLineWebLogic.modifyLineItem(updateExpress2, upOrDownQuery);

                        result = thisSuccess && upOrDownSuccess;
                    }


                    if (result) {
                        //todo 准备log对象
                        ToolsLog log = new ToolsLog();

                        resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                    }
                }


//                resultMsg.setMsg("");
            }

        } catch (ServiceException e) {
            GAlerter.lab(" caught an Exception:", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


}
