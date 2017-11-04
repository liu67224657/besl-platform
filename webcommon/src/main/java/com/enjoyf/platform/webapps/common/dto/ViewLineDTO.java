/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.util.Pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-8 下午1:14
 * Description:
 */
public class ViewLineDTO implements Serializable {
    //the line object
    private ViewLine line;

    //
    private Pagination page;

    //the line items;
    private List<ViewLineItemDTO> lineItems = new ArrayList<ViewLineItemDTO>();


    public ViewLine getLine() {
        return line;
    }

    public void setLine(ViewLine line) {
        this.line = line;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public List<ViewLineItemDTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<ViewLineItemDTO> lineItems) {
        this.lineItems = lineItems;
    }
}