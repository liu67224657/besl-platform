/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface MessageAccessor{
    //insert message
    public Message insert(Message message, Connection conn) throws DbException;

    //get by id
    public Message getMessage(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<Message> query(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<Message> query(String ownUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int updateMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //query message topics
    public List<Long> queryTopicIds(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException;

    //query message sender unos
    public List<String> querySenderUnos(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException;

    //query messages
    public List<Message> query(String ownUno, String senderUno, MessageType type, Pagination page, Connection conn) throws DbException;

    //query messages
    public List<Message> query(String ownUno, Long topicId, Pagination page, Connection conn) throws DbException;

    //get the lastest message with sender
    public Message getLastest(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException;

    public Message getLastest(String ownUno, Long topicId, Connection conn) throws DbException;

    //get message topic size
    public int getTopicSize(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException;

    //remove message
    public boolean remove(String ownUno, Long msgId, Connection conn) throws DbException;

    //remove message
    public boolean removeTopicMessages(String ownUno, Long topicId, Connection conn) throws DbException;

    public boolean removeSenderMessages(String ownUno, String senderUno, Connection conn) throws DbException;

}
