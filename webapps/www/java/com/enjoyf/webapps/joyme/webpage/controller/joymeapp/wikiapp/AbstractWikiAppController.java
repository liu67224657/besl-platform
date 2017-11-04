package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhitaoshi on 2015/4/7.
 */
public abstract class AbstractWikiAppController extends BaseRestSpringController {


    protected Pagination getPaginationbyRequest(HttpServletRequest request) {
        String pageNo = request.getParameter("pnum");
        String pageCount = request.getParameter("pcount");

        int pagNum = 1;
        try {
            pagNum = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
        }
        int pageSize = 24;
        try {
            pageSize = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
        }

        return new Pagination(pagNum * pageSize, pagNum, pageSize);
    }

}
