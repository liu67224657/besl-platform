/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.example.test;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.example.ExampleField;
import com.enjoyf.platform.service.example.ExampleServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        Example entry = new Example();

        entry.setExampleName("example name");
        entry.setExampleDiscription("example description");

        try {
            //insert
            entry = ExampleServiceSngl.get().create(entry);
            System.out.println(entry);

            //
            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(ExampleField.EXAMPLEID, entry.getExampleId()));

            System.out.println(ExampleServiceSngl.get().get(getExpress));

            //
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ExampleField.EXAMPLENAME, "example name"));
            queryExpress.add(QuerySort.add(ExampleField.EXAMPLEID));

            System.out.println(ExampleServiceSngl.get().query(queryExpress, new Pagination()));

            //
            UpdateExpress updateExpress = new UpdateExpress();

            updateExpress.set(ExampleField.LASTCLICKDATE, new Date());
            updateExpress.set(ExampleField.EXAMPLEDISCRIPTION, "example description updated");
            updateExpress.set(ExampleField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
            updateExpress.increase(ExampleField.CLICKTIMES, 1);

            System.out.println(ExampleServiceSngl.get().modify(updateExpress, queryExpress));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        Utility.sleep(10000);
    }
}
