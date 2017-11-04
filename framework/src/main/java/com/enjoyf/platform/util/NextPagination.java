package com.enjoyf.platform.util;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NextPagination implements Serializable {

    public static final int DEFAULT_PAGE_SIZE = 16;

    private int pageSize;

    private long startId;
    private long nextId;

    private boolean isNext;

    private boolean isFirst = false;
    private boolean isLast = false;

    private int queryRowsNum;

    private int totalRows;

    private int maxPage;

    public NextPagination(long startId, int size, boolean isNext) {
        this.startId = startId;
        pageSize = size;
        this.isNext = isNext;
        calculate();
    }

    public void setStartId(long startId) {
        this.startId = startId;
    }

    public void setPageSize(int size) {
        pageSize = size;
        calculate();
    }


    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getStartId() {
        return startId;
    }

    public long getNextId() {
        return nextId;
    }

    public boolean isLast() {
        return isLast;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setQueryRowsNum(int queryRowsNum) {
        this.queryRowsNum = queryRowsNum;
        calculate();
    }

    private void calculate() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        //cal the current page num
        if (isNext && queryRowsNum < pageSize) {
            isLast = true;
        } else {
            isLast = false;
        }

        if (startId <= 0l) {
            isFirst = true;
        } else {
            isFirst = false;
        }
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
        maxPage = totalRows / pageSize;
        if (totalRows % pageSize > 0) {
            maxPage++;
        }
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
