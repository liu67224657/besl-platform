package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.service.event.ActivityAwardLog;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * This interface receives packets from the remote clients. It translates them
 * into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any handlers
 * must be thread-safe.
 */
class EventPacketDecoder extends PacketDecoder {

    private static final Logger logger = LoggerFactory.getLogger(EventPacketDecoder.class);

    private EventLogic logic;
    private ActivityLogic activityLogic;

    /**
     * Constructor takes the UserLogic object that we're going to use to
     * logicProcess the packets.
     *
     * @param l our logical friend
     */
    EventPacketDecoder(EventLogic l, ActivityLogic activityLogic) {
        logic = l;
        this.activityLogic = activityLogic;

        setTransContainer(EventConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will just
     * forward the call to the logic object which will take care of actually
     * decoding the packet.
     *
     * @throws IOException
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            // event relation APIs
            case EventConstants.EVENT_WRITE:
                wp.writeBooleanNx(logic.reportEvent((Event) rPacket.readSerializable(), rPacket.readBooleanNx()));
                break;
            // player event relation APIs
            case EventConstants.PAGEVIEW_LOCATION_GET:
                wp.writeSerializable(logic.getPageViewLocationById((Integer) rPacket.readSerializable()));
                break;
            case EventConstants.PAGEVIEW_LOCATION_QUERY:
                wp.writeSerializable((Serializable) logic.queryAllPageViewLocations());
                break;
            //
            case EventConstants.PAGEVIEW_STATS_PAGEVIEW:
                wp.writeLongNx(logic.statsPageView((Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            case EventConstants.PAGEVIEW_STATS_UNIQUEUSER:
                wp.writeLongNx(logic.statsUniqueUsers((Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            ////////////////
            case EventConstants.ACTIVITY_CREATE:
                wp.writeSerializable(activityLogic.crateActivity((Activity) rPacket.readSerializable()));
                break;

            case EventConstants.ACTIVITY_MODIFY:
                wp.writeBooleanNx(activityLogic.modifyActivity((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
                break;

            case EventConstants.ACTIVITY_GET_BY_ID:
                wp.writeSerializable(activityLogic.getActivity(rPacket.readLongNx()));
                break;

            case EventConstants.ACTIVITY_QUERY_BY_PAGE:
                wp.writeSerializable(activityLogic.queryByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            case EventConstants.ACTIVITYLOG_CREATE:
                wp.writeSerializable(activityLogic.createAwardLog((ActivityAwardLog) rPacket.readSerializable()));
                break;

            case EventConstants.ACTIVITYLOG_QUERY:
                wp.writeSerializable((Serializable) activityLogic.queryAwardLog((QueryExpress) rPacket.readSerializable()));
                break;
            //
            case EventConstants.CREATE_TASK:
                wp.writeSerializable(logic.createTask((Task) rPacket.readSerializable()));
                break;
            case EventConstants.GET_TASK:
                wp.writeSerializable(logic.getTask(rPacket.readStringUTF()));
                break;
            case EventConstants.QUERY_TASK:
                wp.writeSerializable((Serializable) logic.queryTask((QueryExpress) rPacket.readSerializable()));
                break;
            case EventConstants.QUERY_TASK_BY_PAGE:
                wp.writeSerializable(logic.queryTaskByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case EventConstants.MODIFY_TASK:
                wp.writeBooleanNx(logic.modifyTask((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case EventConstants.CREATE_TASK_LOG:
                wp.writeSerializable(logic.createTaskLog((TaskLog) rPacket.readSerializable()));
                break;
            case EventConstants.GET_TASK_LOG:
                wp.writeSerializable(logic.getTaskLog(rPacket.readStringUTF()));
                break;
            case EventConstants.QUERY_TASK_LOG:
                wp.writeSerializable((Serializable) logic.queryTaskLog((QueryExpress) rPacket.readSerializable()));
                break;
            case EventConstants.QUERY_TASK_LOG_BY_PAGE:
                wp.writeSerializable(logic.queryTaskLogByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case EventConstants.MODIFY_TASK_LOG:
                wp.writeBooleanNx(logic.modifyTaskLog((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case EventConstants.CREATE_TASK_GROUP:
                wp.writeSerializable(logic.createTaskGroup((com.enjoyf.platform.service.event.task.TaskGroup) rPacket.readSerializable()));
                break;
            case EventConstants.GET_TASK_GROUP:
                wp.writeSerializable(logic.getTaskGroup(rPacket.readStringUTF()));
                break;
            case EventConstants.QUERY_TASK_GROUP:
                wp.writeSerializable((Serializable) logic.queryTaskGroup((QueryExpress) rPacket.readSerializable()));
                break;
            case EventConstants.MODIFY_TASK_GROUP:
                wp.writeSerializable(logic.modifyTaskGroup((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case EventConstants.QUERY_TASK_BY_GROUPIDS:
                wp.writeSerializable((Serializable) logic.queryTaskByGroupIds(rPacket.readStringUTF(), (AppPlatform) rPacket.readSerializable(), (TaskGroupType) rPacket.readSerializable()));
                break;

            case EventConstants.QUERY_TASK_BY_GROUPID_PROFILEID:
                wp.writeSerializable((Serializable) logic.queryTaskByGroupIdProfileId(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case EventConstants.GET_TASKLOG_BY_GROUPID_PROFILEID:
                wp.writeSerializable(logic.getTaskLogByGroupIdProfileId(rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable()));
                break;

            case EventConstants.CHECK_COMPLETE_TASK:
                wp.writeSerializable((Serializable) logic.checkCompleteTask(rPacket.readStringUTF(), rPacket.readStringUTF(), (AppPlatform) rPacket.readSerializable(), (Date) rPacket.readSerializable()));
                break;
            case EventConstants.GET_TASK_AWARD:
                wp.writeSerializable(logic.getAward(rPacket.readStringUTF(), rPacket.readStringUTF(), (Date) rPacket.readSerializable(), rPacket.readStringUTF(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;

            case EventConstants.QUERY_TASKLOG_SIGN_SUM_BY_PROFILEID_GROUPID:
                wp.writeIntNx(logic.querySignSumByProfileIdGroupId(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case EventConstants.QUERY_COMPLETE_GROUP_BY_PROFILEID_GROUPID:
                wp.writeSerializable((Serializable) logic.queryCompleteGroupByProfileIdGroupId(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case EventConstants.SET_COMPLETE_GROUP_BY_PROFILEID_GROUPID:
                wp.writeSerializable((Serializable) logic.setCompleteGroupByProfileIdGroupId(rPacket.readStringUTF(), rPacket.readStringUTF(), (TaskGroupShowType) rPacket.readSerializable()));
                break;

            case EventConstants.QUERY_TASK_LIST_BY_GROUPID:
                wp.writeSerializable((Serializable) logic.getTaskGroupList(rPacket.readStringUTF()));
                break;
            default:
                GAlerter.lab("EventPacketDecoder.logicProcess: Unrecognized type: ", String.valueOf(type));
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
