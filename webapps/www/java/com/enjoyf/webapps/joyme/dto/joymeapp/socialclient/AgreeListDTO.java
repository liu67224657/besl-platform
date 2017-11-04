package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.util.NextPagination;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-15
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
public class AgreeListDTO {

    private NextPagination page;
    private List<ProfileDTO> rows = new ArrayList<ProfileDTO>();
    private int agreenum;

    public NextPagination getPage() {
        return page;
    }

    public void setPage(NextPagination page) {
        this.page = page;
    }

    public List<ProfileDTO> getRows() {
        return rows;
    }

    public void setRows(List<ProfileDTO> rows) {
        this.rows = rows;
    }

    public int getAgreenum() {
        return agreenum;
    }

    public void setAgreenum(int agreenum) {
        this.agreenum = agreenum;
    }
}
