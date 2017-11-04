package com.enjoyf.platform.webapps.common;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-7-31 上午11:23
 * Description:
 */
public class JsonPagination {

    public static final int DEFAULT_PAGE_SIZE = 16;

    private int totalRows;
    private int curPage;
    private int pageSize;

    private int maxPage;
    private int startRowIdx;
    private int endRowIdx;

    public JsonPagination() {
        this(0, 1, DEFAULT_PAGE_SIZE);
    }

    public JsonPagination(int total) {
        this(total, 1, DEFAULT_PAGE_SIZE);
    }

    public JsonPagination(int total, int page) {
        this(total, page, DEFAULT_PAGE_SIZE);
    }

    public JsonPagination(int total, int page, int size) {
        totalRows = total;
        curPage = page;
        pageSize = size;

        calculate();
    }

    public JsonPagination(Pagination pagination) {
        totalRows = pagination.getTotalRows();
        curPage = pagination.getCurPage();
        pageSize = pagination.getPageSize();
        calculate();
    }

    public void setTotalRows(int total) {
        totalRows = total;
        calculate();
    }

    public void setCurPage(int page) {
        curPage = page;
        calculate();
    }

    public void setPageSize(int size) {
        pageSize = size;
        calculate();
    }


    public int getEndRowIdx() {
        return endRowIdx;
    }

    public int getStartRowIdx() {
        return startRowIdx;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public boolean hasNextPage() {
        return curPage < maxPage;
    }

    public boolean isFirstPage() {
        return curPage <= 1;
    }

    public boolean isLastPage() {
        return curPage >= maxPage;
    }

    private void calculate() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        //cal the max page
        maxPage = totalRows / pageSize;
        if (totalRows % pageSize > 0) {
            maxPage++;
        }

        //cal the current page num
        if (curPage > maxPage) {
            curPage = maxPage;
        }
        if (curPage < 1) {
            curPage = 1;
        }

        //cal the start and end row idx
        startRowIdx = pageSize * (curPage - 1);
        endRowIdx = pageSize * curPage - 1;
        if (endRowIdx > totalRows - 1) {
            endRowIdx = totalRows - 1;
        }
        if (endRowIdx < 0) {
            endRowIdx = 0;
        }
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
