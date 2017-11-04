/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public interface ExampleService {
    //create Example
    public Example create(Example entry) throws ServiceException;

    //get Example
    public Example get(QueryExpress queryExpress) throws ServiceException;

    //query Example
    public PageRows<Example> query(QueryExpress queryExpress, Pagination page) throws ServiceException;

    public RangeRows<Example> query(QueryExpress queryExpress, Rangination range) throws ServiceException;

    //update Example
    public boolean modify(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

}
