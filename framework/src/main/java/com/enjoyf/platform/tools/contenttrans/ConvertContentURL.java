package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.shorturl.ShortUrlHandler;
import com.enjoyf.platform.db.viewline.ViewLineHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlField;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.ViewLineItemField;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-23
 * Time: 下午3:02
 * To change this template use File | Settings | File Templates.
 */
public class ConvertContentURL {

    private static Logger logger = LoggerFactory.getLogger(ConvertContentURL.class);


    private static Pattern pattern = Pattern.compile("/home/[^/]+/([0-9a-zA-Z\\-_]+)");

    private static PageRows<ViewLineItem> queryLineHandler(ViewLineHandler viewLineHandler, Pagination pagination) throws DbException {
        PageRows<ViewLineItem> itemList = viewLineHandler.queryLineItems(new QueryExpress().add(QueryCriterions
                .eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode())), pagination);

        return itemList;
    }

    private static void convertViewLine(ViewLineHandler viewLineHandler) throws DbException {
        int size = 0;
        int curPageNo = 0;
        Pagination pagination = new Pagination(300, curPageNo, 300);
        do {
            curPageNo = curPageNo + 1;
            pagination.setCurPage(curPageNo);

            PageRows<ViewLineItem> itemList = queryLineHandler(viewLineHandler, pagination);

            size += itemList.getRows().size();

            pagination = itemList.getPage();

            for (ViewLineItem item : itemList.getRows()) {
                ViewLineItemDisplayInfo info = item.getDisplayInfo();
                if (info == null || StringUtil.isEmpty(info.getLinkUrl())) {
                    continue;
                }

                StringBuffer resutlStringBuffer = new StringBuffer();
                Matcher matcher = pattern.matcher(info.getLinkUrl());
                if (matcher.find()) {
                    String contentId = matcher.group(1);
                    matcher.appendReplacement(resutlStringBuffer, "/note/" + contentId);
                    matcher.appendTail(resutlStringBuffer);

                    logger.info(resutlStringBuffer.toString());
                    item.getDisplayInfo().setLinkUrl(resutlStringBuffer.toString());

                    viewLineHandler.updateLineItem(new UpdateExpress().set(ViewLineItemField.DISPLAYINFO, item.getDisplayInfo().toJson()),
                            new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.ITEMID, item.getItemId())));
                }
            }

        } while (pagination.hasNextPage());

        logger.info("finish  convert viewline content url size is:" + size);
    }

    //todo 导完出书删掉
    private static void convertShortUrl(ShortUrlHandler shortUrlHandler) throws DbException {
        int size = 0;

        for (int i = 0; i < 100; i++) {
            List<ShortUrl> shortUrlList = shortUrlHandler.queryShortUrlByTableNo(i);

            for (ShortUrl url : shortUrlList) {
                if (StringUtil.isEmpty(url.getUrl())) {
                    continue;
                }

                StringBuffer resutlStringBuffer = new StringBuffer();
                Matcher matcher = pattern.matcher(url.getUrl());
                if (matcher.find()) {
                    String contentId = matcher.group(1);
                    matcher.appendReplacement(resutlStringBuffer, "/note/" + contentId);
                    matcher.appendTail(resutlStringBuffer);

                    logger.info(resutlStringBuffer.toString());

                    Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
                    map.put(ShortUrlField.URL, resutlStringBuffer.toString());
                    shortUrlHandler.updateShortUrl(url.getUrlKey(), map);
                }
            }

            size += shortUrlList.size();

        }


        logger.info("finish  convert short content url size is:" + size);
    }

    public static void main(String[] args) throws DbException {
        FiveProps fiveProps = Props.instance().getServProps();
        ViewLineHandler viewLineHandler = new ViewLineHandler("viewline", fiveProps);
        ShortUrlHandler shortUrlHandler = new ShortUrlHandler("shorturl", fiveProps);

        long now = System.currentTimeMillis();

        logger.info("====================begin convert url==================== " + now);
        convertViewLine(viewLineHandler);

        convertShortUrl(shortUrlHandler);
        logger.info("====================finished all data====================" + ((System.currentTimeMillis() - now) / 1000));
    }
}
