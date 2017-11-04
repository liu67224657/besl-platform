package com.enjoyf.webapps.joyme.weblogic.example;

import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.example.ExampleField;
import com.enjoyf.platform.service.example.ExampleServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 */
@Service(value = "exampleWebLogic")
public class ExampleWebLogic {

    //
    public boolean clickExample(long exampleId) throws ServiceException {
        //fullfill the query express.
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ExampleField.EXAMPLEID, exampleId));

        //fullfill the update express
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ExampleField.LASTCLICKDATE, new Date());
        updateExpress.increase(ExampleField.CLICKTIMES, 1);

        //modify the click times
        return ExampleServiceSngl.get().modify(updateExpress, queryExpress);
    }

    //
    public boolean modifyExampleName(String exampleName, long exampleId) throws ServiceException {
        //fullfill the query express.
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ExampleField.EXAMPLEID, exampleId));

        //fullfill the update express
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ExampleField.EXAMPLENAME, exampleName);

        //modify the name
        return ExampleServiceSngl.get().modify(updateExpress, queryExpress);
    }

    //
    public Example getExampleById(long exampleId) throws ServiceException {
        //fullfill the query express.
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ExampleField.EXAMPLEID, exampleId));

        //modify the name
        return ExampleServiceSngl.get().get(queryExpress);
    }
}
