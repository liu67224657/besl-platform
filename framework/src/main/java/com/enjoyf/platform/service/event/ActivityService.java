package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityService {


    public Activity crateActivity(Activity activity)throws ServiceException;


    public boolean modifyActivity(UpdateExpress updateExpress,long activityId)throws ServiceException;


    public PageRows<Activity> queryByPage(QueryExpress queryExpress,Pagination pagination)throws ServiceException;


    public Activity getActivity(long activityId)throws ServiceException;


    public ActivityAwardLog createAwardLog(ActivityAwardLog log)throws ServiceException;


    public List<ActivityAwardLog> queryAwardLog(QueryExpress queryExpress)throws ServiceException;
}
