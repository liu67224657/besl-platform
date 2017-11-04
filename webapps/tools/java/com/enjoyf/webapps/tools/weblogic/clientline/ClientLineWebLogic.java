package com.enjoyf.webapps.tools.weblogic.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineItemDTO;
import com.enjoyf.platform.service.joymeapp.clientline.ClientLineWebDataProcessorFactory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-11
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "clientLineWebLogic")
public class ClientLineWebLogic {


    public PageRows<ClientLineItemDTO> queryClientLineItemByPage(QueryExpress queryExpress, Pagination pagination) throws Exception {
        PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
        if (itemPageRows == null || CollectionUtil.isEmpty(itemPageRows.getRows())) {
            return null;
        }
        List<ClientLineItemDTO> dtoList = new LinkedList<ClientLineItemDTO>();
        for (ClientLineItem item : itemPageRows.getRows()) {
            ClientLineItemDTO dto = ClientLineWebDataProcessorFactory.get().factory(item.getItemDomain()).buildViewLineItemDTOs(item);
            if (dto == null) {
                continue;
            }
            dtoList.add(dto);
        }
        PageRows<ClientLineItemDTO> dtoPageRows = new PageRows<ClientLineItemDTO>();
        dtoPageRows.setPage(itemPageRows.getPage());
        dtoPageRows.setRows(dtoList);
        return dtoPageRows;
    }

    public static Long sort(String sort, Long lineId, Long itemId) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
        ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
        if (clientLineItem == null) {
            return null;
        }
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, new Pagination(1, 1, 1));
        if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
            updateExpress1.set(ClientLineItemField.DISPLAY_ORDER, clientLineItem.getDisplayOrder());
            updateExpress1.set(ClientLineItemField.ITEM_CREATE_DATE, clientLineItem.getItemCreateDate());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress1, pageRows.getRows().get(0).getItemId());


            updateExpress2.set(ClientLineItemField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
            updateExpress2.set(ClientLineItemField.ITEM_CREATE_DATE, pageRows.getRows().get(0).getItemCreateDate());
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress2, clientLineItem.getItemId());

        }
        return pageRows.getRows().get(0).getItemId();
    }

    public static Long clientLineSort(String sort, Long lineId, ClientLineType clientLineType, String platform) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId)));
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, clientLineType.getCode()));
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.parseInt(platform)));
            queryExpress.add(QueryCriterions.lt(ClientLineField.DISPLAY_ORDER, clientLine.getDisplay_order()));
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, Integer.parseInt(platform)));
            queryExpress.add(QueryCriterions.gt(ClientLineField.DISPLAY_ORDER, clientLine.getDisplay_order()));
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        PageRows<ClientLine> pageRows = JoymeAppServiceSngl.get().queryClientLineByPage(queryExpress, new Pagination(1, 1, 1));
        if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
            updateExpress1.set(ClientLineField.DISPLAY_ORDER, clientLine.getDisplay_order());
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress1, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, pageRows.getRows().get(0).getLineId())));


            updateExpress2.set(ClientLineField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplay_order());
            JoymeAppServiceSngl.get().modifyClientLine(updateExpress2, new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, clientLine.getLineId())));
            return pageRows.getRows().get(0).getLineId();
        }
        return null;
    }
}
