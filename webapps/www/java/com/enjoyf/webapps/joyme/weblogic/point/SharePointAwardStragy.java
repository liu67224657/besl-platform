package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.PointHotdeployConfig;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-12
 * Time: 下午1:44
 * To change this template use File | Settings | File Templates.
 */
public class SharePointAwardStragy implements PointAwardStragy {

    public PointResultMsg arwardPoint(PointActionHistory pointHistory) throws ServiceException {
        PointProps pointProps = HotdeployConfigFactory.get().getConfig(PointHotdeployConfig.class).getPropsMap().get(PointActionType.SHARE);

        Date now = new Date();
        if (pointProps.getLimit() > 0) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(UserDayPointField.USERNO, pointHistory.getUserNo()));
            queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTDATE, now));
            queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, pointHistory.getActionType().getCode()));
            UserDayPoint userDayPoint = PointServiceSngl.get().getUserDayPoint(queryExpress, now);

            //分享类型的day limit超过
            if (userDayPoint != null && userDayPoint.getPointValue() >= pointProps.getLimit()) {
                return PointResultMsg.OUT_OF_POINT_LIMIT;
            }
        }

        if (pointProps.getTimes() > 0) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PointActionHistoryField.USERNO, pointHistory.getUserNo()));
            queryExpress.add(QueryCriterions.eq(PointActionHistoryField.DESTID, pointHistory.getDestId()));
            queryExpress.add(QueryCriterions.eq(PointActionHistoryField.ACTIONDATE, now));
            List<PointActionHistory> pointActionHistoryList = PointServiceSngl.get().queryPointActionHistory(queryExpress);

            //分享类型的day limit超过
            if (pointActionHistoryList.size() >= pointProps.getTimes()) {
                return PointResultMsg.OUT_OF_POINT_TIMES;
            }
        }

        PointServiceSngl.get().increasePointActionHistory(pointHistory,PointKeyType.WWW);
        return PointResultMsg.SUCCESS;
    }
}
