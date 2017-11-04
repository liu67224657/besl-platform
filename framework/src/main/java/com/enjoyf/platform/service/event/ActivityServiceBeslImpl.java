package com.enjoyf.platform.service.event;

import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
public class ActivityServiceBeslImpl implements ActivityService {

    private ReqProcessor reqProcessor = null;
    private QueueThreadN eventQueueThreadN = null;

    public ActivityServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ActivityServiceBeslImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public Activity crateActivity(Activity activity) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(activity);

        Request request = new Request(EventConstants.ACTIVITY_CREATE, wp);
        return (Activity) reqProcessor.process(request).readSerializable();
    }

    @Override
    public boolean modifyActivity(UpdateExpress updateExpress, long activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(activityId);

        Request request = new Request(EventConstants.ACTIVITY_MODIFY, wp);
        return reqProcessor.process(request).readBooleanNx();
    }

    @Override
    public PageRows<Activity> queryByPage(QueryExpress queryExpress,Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request request = new Request(EventConstants.ACTIVITY_MODIFY, wp);
        return (PageRows<Activity>) reqProcessor.process(request).readSerializable();
    }

    @Override
    public Activity getActivity(long activityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(activityId);

        Request request = new Request(EventConstants.ACTIVITY_GET_BY_ID, wp);
        return (Activity) reqProcessor.process(request).readSerializable();
    }

    @Override
    public ActivityAwardLog createAwardLog(ActivityAwardLog log) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(log);

        Request request = new Request(EventConstants.ACTIVITYLOG_CREATE, wp);
        return (ActivityAwardLog) reqProcessor.process(request).readSerializable();
    }

    @Override
    public List<ActivityAwardLog> queryAwardLog(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(EventConstants.ACTIVITYLOG_QUERY, wp);
        return (List<ActivityAwardLog>) reqProcessor.process(request).readSerializable();
    }
}
