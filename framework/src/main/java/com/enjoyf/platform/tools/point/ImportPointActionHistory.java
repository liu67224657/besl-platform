package com.enjoyf.platform.tools.point;

import com.enjoyf.platform.serv.point.PointConfig;
import com.enjoyf.platform.serv.point.PointLogic;
import com.enjoyf.platform.serv.point.PointRedis;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionHistoryField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class ImportPointActionHistory {


    public static void main(String[] args) {

        try {
            FiveProps fiveProps = Props.instance().getServProps();
            PointConfig pointConfig = new PointConfig(fiveProps);
            PointLogic pointLogic = new PointLogic(pointConfig);
            PointRedis pointRedis = new PointRedis(fiveProps);
            System.out.println("start=================================================");
            Pagination page = null;
            int cp = 0;
            do {
                cp += 1;
                page = new Pagination(2000 * cp, cp, 2000);
                PageRows<PointActionHistory> pointActionHistoryPageRows = pointLogic.queryPointActionHistoryByPage(new QueryExpress().add(QueryCriterions.eq(PointActionHistoryField.POINTKEY, "newsyhb")), page);
                if (pointActionHistoryPageRows != null && !CollectionUtil.isEmpty(pointActionHistoryPageRows.getRows())) {
                    for (PointActionHistory pointActionHistory : pointActionHistoryPageRows.getRows()) {
                        pointRedis.putMyPoint(pointActionHistory);
                    }
                }
                page = pointActionHistoryPageRows.getPage();
            } while (!page.isLastPage());
            System.out.println("success=================================================");
        } catch (ServiceException e) {
            System.out.println("error=================================================");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
