package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-12
 * Time: 下午1:44
 * To change this template use File | Settings | File Templates.
 */
public interface PointAwardStragy {

    public PointResultMsg arwardPoint(PointActionHistory history) throws ServiceException;
}
