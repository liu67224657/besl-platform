/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseEventType;
import org.apache.tools.ant.types.resources.selectors.Date;

import java.sql.Connection;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:56
 * Description:
 */
public interface AdvertiseEventStatAccessor {
    //count publish times
    public long countTimes(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public long countUniqueUsers(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public long countUsers(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public long countSessions(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public long sum(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    //group count publish times
    public Map<String, Long> groupTimes(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public Map<String, Long> groupUniqueUsers(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public Map<String, Long> groupUsers(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public Map<String, Long> groupSessions(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    public Map<String, Long> groupSum(AdvertiseEventType eventType, String publishId, Date from, Date to, Connection conn) throws DbException;

    //count location times
    public long countTimes(AdvertiseEventType eventType, String publishId, String locationId, Date from, Date to, Connection conn) throws DbException;

    public long countUniqueUsers(AdvertiseEventType eventType, String publishId, String locationId, Date from, Date to, Connection conn) throws DbException;

    public long countUsers(AdvertiseEventType eventType, String publishId, String locationId, Date from, Date to, Connection conn) throws DbException;

    public long countSessions(AdvertiseEventType eventType, String publishId, String locationId, Date from, Date to, Connection conn) throws DbException;

    public long sum(AdvertiseEventType eventType, String publishId, String locationId, Date from, Date to, Connection conn) throws DbException;

}
