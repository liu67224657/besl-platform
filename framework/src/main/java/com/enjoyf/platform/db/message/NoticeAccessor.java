/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface NoticeAccessor extends SequenceAccessor {

    //insert notice
    public Notice insert(Notice notice, Connection conn) throws DbException;

    //update notice
    public boolean update(Notice notice, Connection conn) throws DbException;

    //query notices
    public List<Notice> query(String ownUno, Connection conn) throws DbException;

    //get notice
    public Notice get(String ownUno, NoticeType type, Connection conn) throws DbException;

    //reset notice
    public boolean reset(String ownUno, NoticeType type, Connection conn) throws DbException;
    public boolean reset(String ownUno, Connection conn) throws DbException;
    public boolean resetExcludeNC(String ownUno, Connection conn) throws DbException;

    //update notice use field
    public boolean updateField(String ownUno, NoticeType type,Map<ObjectField, Object> keyValues, Connection conn) throws DbException;
}
