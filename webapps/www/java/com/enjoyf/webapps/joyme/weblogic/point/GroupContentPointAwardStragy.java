package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.PointHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
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
public class GroupContentPointAwardStragy implements PointAwardStragy {

    public PointResultMsg arwardPoint(PointActionHistory pointHistory) throws ServiceException {
        PointProps pointProps = HotdeployConfigFactory.get().getConfig(PointHotdeployConfig.class).getPropsMap().get(PointActionType.CONTENT_ADMIN_ADJUST_POINT);

        Date now = new Date();
        List<String> superUnos = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getGameSuperPrivacyUnoList();
        if (!superUnos.contains(pointHistory.getUserNo())) {
            if (pointProps.getLimit() > 0) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(UserDayPointField.USERNO, pointHistory.getUserNo()));
                queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTDATE, now));
                queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, pointHistory.getActionType().getCode()));
                UserDayPoint userDayPoint = PointServiceSngl.get().getUserDayPoint(queryExpress, now);

                int absPoint = userDayPoint == null ? 0 : Math.abs(userDayPoint.getPointValue());

                //分享类型的day limit超过
                if (userDayPoint != null && absPoint >= pointProps.getLimit()) {
                    return PointResultMsg.OUT_OF_POINT_LIMIT;
                }
            }
        }


        boolean bVal = PointServiceSngl.get().increasePointActionHistory(pointHistory, PointKeyType.WWW);
        if (bVal) {
            PointActionHistory authorPointHistroy = new PointActionHistory();
            authorPointHistroy.setUserNo(pointHistory.getDestUno());
            authorPointHistroy.setCreateDate(now);
            authorPointHistroy.setActionDate(now);
            authorPointHistroy.setActionDescription(pointHistory.getActionDescription());
            authorPointHistroy.setActionType(PointActionType.CONTENT_ADJUST_POINT);
            authorPointHistroy.setPointValue(Math.abs(pointHistory.getPointValue()));
            authorPointHistroy.setDestId(pointHistory.getDestId());
            authorPointHistroy.setDestUno(pointHistory.getUserNo());

            PointServiceSngl.get().increasePointActionHistory(authorPointHistroy, PointKeyType.WWW);
        }


        return PointResultMsg.SUCCESS;
    }
}
