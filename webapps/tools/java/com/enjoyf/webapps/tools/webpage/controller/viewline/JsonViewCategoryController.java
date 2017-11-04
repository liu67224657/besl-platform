package com.enjoyf.webapps.tools.webpage.controller.viewline;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.viewline.ViewLineWebLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-11-19
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/json/viewcategory")
public class JsonViewCategoryController {
    private static final String PREV = "prev";
    private static final String NEXT = "next";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @Resource(name = "viewLineWebLogic")
    private ViewLineWebLogic viewLineWebLogic;

    //    @ResponseBody
    @RequestMapping("/sort")
    public ModelAndView paiXu(
            HttpServletRequest request,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "aspectCode", required = false) String aspectCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "items", required = false, defaultValue = "0") int items,                   //总记录数
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "1") int maxPageItems) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp : categoryId=" + categoryId + ", type= " + type);
        }
//        Map<String, Object> mapMsg = new HashMap<String, Object>();
//        mapMsg.put("aspectCode", aspectCode);
//        mapMsg.put("categoryName", categoryName);
//        mapMsg.put("items", items);
//        mapMsg.put("pager.offset", pagerOffset);
//        mapMsg.put("maxPageItems", maxPageItems);
        pagerOffset = (pagerOffset - 1) * maxPageItems;

        String encode = categoryName;
        try {
            encode = URLEncoder.encode(categoryName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            GAlerter.lab("encoding occured UnsupportedEncodingException.e:", e);
        }

        try {
            if (!StringUtil.isEmpty(categoryId) && !StringUtil.isEmpty(type)) {

                //

                ViewCategory sourceCategory = viewLineWebLogic.getCategoryById(Integer.parseInt(categoryId));
                int fooId = sourceCategory.getParentCategoryId();
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, fooId))
                        .add(QuerySort.add(ViewCategoryField.DISPLAYORDER, QuerySortOrder.ASC));
                List<ViewCategory> categoryList = ViewLineServiceSngl.get().queryCategory(queryExpress, new Pagination(Integer.MAX_VALUE)).getRows();

                ViewCategory targetCategory = null;

                for (int i = 0; i < categoryList.size(); i++) {
                    if (Integer.parseInt(categoryId) == categoryList.get(i).getCategoryId()) {
                        //当前向上移动
                        if (type.equals(PREV) && i != 0) {
                            targetCategory = categoryList.get(i - 1);

                        } else if (type.equals(NEXT) && i != categoryList.size() - 1) {
                            //当前向下移动
                            targetCategory = categoryList.get(i + 1);

                        }
                    }
                }
//        mapMsg.put("aspectCode", aspectCode);
//        mapMsg.put("categoryName", categoryName);
//        mapMsg.put("items", items);
//        mapMsg.put("pager.offset", pagerOffset);
//        mapMsg.put("maxPageItems", maxPageItems);
                if (targetCategory == null) {
                    return new ModelAndView("redirect:/viewline/categorylist?aspectCode=" + aspectCode + "&categoryName=" + encode + "&items=" + items
                            + "&pager.offset=" + pagerOffset + "&maxPageItems=" + maxPageItems);
                }

                //交换排序值
                int tempDisplayOrderValue = sourceCategory.getDisplayOrder();
                sourceCategory.setDisplayOrder(targetCategory.getDisplayOrder());
                targetCategory.setDisplayOrder(tempDisplayOrderValue);

                //保存数据库
                boolean success1 = viewLineWebLogic.modifyCategory(new UpdateExpress().set(ViewCategoryField.DISPLAYORDER, sourceCategory.getDisplayOrder()),
                        new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, sourceCategory.getCategoryId())));
                if (success1) {
                    boolean success2 = viewLineWebLogic.modifyCategory(new UpdateExpress().set(ViewCategoryField.DISPLAYORDER, targetCategory.getDisplayOrder()),
                            new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, targetCategory.getCategoryId())));

                    if (success2) {
                        //todo 准备log对象
                        ToolsLog log = new ToolsLog();

                    } else {
                        viewLineWebLogic.modifyCategory(new UpdateExpress().set(ViewCategoryField.DISPLAYORDER, targetCategory.getDisplayOrder()),
                                new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, sourceCategory.getCategoryId())));
                    }
                }

            }
        } catch (ServiceException e) {
            GAlerter.lab(" caught an Exception:", e);
        }
        return new ModelAndView("redirect:/viewline/categorylist?aspectCode=" + aspectCode + "&categoryName=" + encode + "&items=" + items
                + "&pager.offset=" + pagerOffset + "&maxPageItems=" + maxPageItems);
    }
}
