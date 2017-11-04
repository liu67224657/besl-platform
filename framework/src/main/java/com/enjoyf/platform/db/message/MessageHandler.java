/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class MessageHandler {
	//
	private DataBaseType dataBaseType;
	private String dataSourceName;

	//
	private MessageAccessor messageAccessor;
	private NoticeAccessor noticeAccessor;
	private PushMsgAccessor pushMsgAccessor;

	private ClientDeviceAccessor clientDeviceAccessor;

	private SocialMessageAccessor socialMessageAccessor;

	private SocialNoticeAccessor socialNoticeAccessor;

	private RedisManager redisManager;

	public MessageHandler(String dsn, FiveProps props, RedisManager redisManager) throws DbException {
		dataSourceName = dsn.toLowerCase();

		//create the catasource
		DataSourceManager.get().append(dataSourceName, props);
		dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

		//
		messageAccessor = MessageAccessorFactory.factoryMessageAccessor(dataBaseType);
		noticeAccessor = MessageAccessorFactory.factoryNoticeAccessor(dataBaseType);
		pushMsgAccessor = MessageAccessorFactory.factoryPushMsgAccessor(dataBaseType);

		clientDeviceAccessor = TableAccessorFactory.get().factoryAccessor(ClientDeviceAccessor.class, dataBaseType);

		socialMessageAccessor = TableAccessorFactory.get().factoryAccessor(SocialMessageAccessor.class, dataBaseType);

		socialNoticeAccessor = TableAccessorFactory.get().factoryAccessor(SocialNoticeAccessor.class, dataBaseType);
		this.redisManager = redisManager;
	}


	public Message postMessage(Message message) throws DbException {
		Message returnValue = null;

		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			if (message.getMsgType().equals(MessageType.PRIVATE) || message.getMsgType().equals(MessageType.SOCIAL_PRIVATE)) {
				//insert to sender table.
				message.setOwnUno(message.getSenderUno());

				returnValue = messageAccessor.insert(message, conn);

				//insert to receiver.
				if (returnValue != null && !(returnValue.getRecieverUno().equals(returnValue.getSenderUno()))) {
					Message receiveMsg = new Message();
					receiveMsg.setOwnUno(returnValue.getRecieverUno());

					receiveMsg.setRecieverUno(returnValue.getRecieverUno());
					receiveMsg.setSenderUno(returnValue.getSenderUno());

					receiveMsg.setTopicId(returnValue.getTopicId());

					receiveMsg.setBody(returnValue.getBody());
					receiveMsg.setMsgType(returnValue.getMsgType());

					receiveMsg.setSendIp(returnValue.getSendIp());
					receiveMsg.setSendDate(returnValue.getSendDate());

					messageAccessor.insert(receiveMsg, conn);
				}


			} else {
				//insert to receiver.
				Message receiveMsg = new Message();

				receiveMsg.setOwnUno(message.getRecieverUno());

				receiveMsg.setRecieverUno(message.getRecieverUno());
				receiveMsg.setSenderUno(message.getSenderUno());

				receiveMsg.setTopicId(message.getTopicId());

				receiveMsg.setBody(message.getBody());
				receiveMsg.setMsgType(message.getMsgType());

				receiveMsg.setSendIp(message.getSendIp());
				receiveMsg.setSendDate(message.getSendDate());

				receiveMsg.setReadStatus(message.getReadStatus());
				receiveMsg.setReadDate(message.getReadDate());

				returnValue = messageAccessor.insert(receiveMsg, conn);
			}

		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public Message getMessage(String ownUno, QueryExpress queryExpress) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return messageAccessor.getMessage(ownUno, queryExpress, conn);

		} finally {
			DataBaseUtil.closeConnection(conn);
		}

	}

	public PageRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Pagination page) throws DbException {
		PageRows<Message> returnValue = new PageRows<Message>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(messageAccessor.query(ownUno, queryExpress, page, conn));
			returnValue.setPage(page);

		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public RangeRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Rangination rangination) throws DbException {
		RangeRows<Message> returnValue = new RangeRows<Message>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(messageAccessor.query(ownUno, queryExpress, rangination, conn));
			returnValue.setRange(rangination);

		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public int updateMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return messageAccessor.updateMessage(ownUno, updateExpress, queryExpress, conn);

		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public PageRows<MessageTopic> queryMessageTopics(String ownUno, MessageType type, Pagination page) throws DbException {
		PageRows<MessageTopic> returnValue = new PageRows<MessageTopic>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			List<Long> topicIds = messageAccessor.queryTopicIds(ownUno, type, page, conn);
			for (Long topicId : topicIds) {
				// messageAccessor.query()
			}

			//returnValue.setRows();
			returnValue.setPage(page);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public PageRows<MessageTopic> querySenderTopics(String ownUno, MessageType type, Pagination page) throws DbException {
		PageRows<MessageTopic> returnValue = new PageRows<MessageTopic>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			List<String> senderUnos = messageAccessor.querySenderUnos(ownUno, type, page, conn);
			for (String senderUno : senderUnos) {
				Message msg = messageAccessor.getLastest(ownUno, senderUno, type, conn);

				if (msg != null) {
					MessageTopic topic = new MessageTopic();

					topic.setLastestMessage(msg);
					topic.setReletionUno(senderUno);

					//the topic size
					topic.setMsgSize(messageAccessor.getTopicSize(ownUno, senderUno, type, conn));

					returnValue.getRows().add(topic);
				}
			}

			returnValue.setPage(page);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}


	public PageRows<Message> queryMessages(String ownUno, String senderUno, MessageType type, Pagination page) throws DbException {
		PageRows<Message> returnValue = new PageRows<Message>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(messageAccessor.query(ownUno, senderUno, type, page, conn));
			returnValue.setPage(page);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}


	public PageRows<Message> queryMessages(String ownUno, Long topicId, Pagination page) throws DbException {
		PageRows<Message> returnValue = new PageRows<Message>();
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(messageAccessor.query(ownUno, topicId, page, conn));
			returnValue.setPage(page);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}


	public boolean removeMessage(String ownUno, Long msgId) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return messageAccessor.remove(ownUno, msgId, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean removeTopicMessages(String ownUno, Long topicId) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return messageAccessor.removeTopicMessages(ownUno, topicId, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean removeSenderMessages(String ownUno, String senderUno) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return messageAccessor.removeSenderMessages(ownUno, senderUno, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public Notice postNotice(Notice notice) throws DbException {
		Notice returnValue = null;
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			Notice existNotice = noticeAccessor.get(notice.getOwnUno(), notice.getNoticeType(), conn);

			if (existNotice == null) {
				//if billing coin notice
				if (notice.getNoticeType().equals(NoticeType.BILLING_COIN)) {
					notice.setValidStatus(ValidStatus.INVALID);
				}
				if (notice.getNoticeType().equals(NoticeType.SOCIAL_CLIENT)) {
					notice.setValidStatus(ValidStatus.VALID);
				}
				returnValue = noticeAccessor.insert(notice, conn);
			} else {
				noticeAccessor.update(notice, conn);

				existNotice.setCount(existNotice.getCount() + notice.getCount());
				returnValue = existNotice;
			}
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}


	public List<Notice> queryNotices(String ownUno) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return noticeAccessor.query(ownUno, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public Notice getNotice(String ownUno, NoticeType type) throws DbException {
		Notice returnValue = null;

		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue = noticeAccessor.get(ownUno, type, conn);
			if (returnValue == null) {
				returnValue = new Notice();

				returnValue.setCount(0);
				returnValue.setOwnUno(ownUno);
				returnValue.setNoticeType(type);
			}
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public boolean updateNotice(Notice notice) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return noticeAccessor.update(notice, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean updateNoticeKeyValues(String ownUno, NoticeType type, Map<ObjectField, Object> keyValues) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return noticeAccessor.updateField(ownUno, type, keyValues, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public boolean readNotice(String ownUno) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);
			return noticeAccessor.reset(ownUno, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean readNoticeAllExcludeNC(String ownUno) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);
			return noticeAccessor.resetExcludeNC(ownUno, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public boolean readNotice(String ownUno, NoticeType type) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return noticeAccessor.reset(ownUno, type, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	//insert
	public PushMessage insert(PushMessage entry) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return pushMsgAccessor.insert(entry, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	//get by id
	public PushMessage get(QueryExpress queryExpress) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return pushMsgAccessor.get(queryExpress, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	//query
	public PageRows<PushMessage> query(QueryExpress queryExpress, Pagination page) throws DbException {
		PageRows<PushMessage> returnValue = new PageRows<PushMessage>();

		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(pushMsgAccessor.query(queryExpress, page, conn));
			returnValue.setPage(page);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	public RangeRows<PushMessage> query(QueryExpress queryExpress, Rangination range) throws DbException {
		RangeRows<PushMessage> returnValue = new RangeRows<PushMessage>();

		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			returnValue.setRows(pushMsgAccessor.query(queryExpress, range, conn));
			returnValue.setRange(range);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}

		return returnValue;
	}

	//update
	public int updatePushMessage(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
		Connection conn = null;

		try {
			conn = DbConnFactory.factory(dataSourceName);

			return pushMsgAccessor.update(updateExpress, queryExpress, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public Notice insertNotice(Notice notice) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return noticeAccessor.insert(notice, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public ClientDevice createClientDevice(ClientDevice mobileDevice) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return clientDeviceAccessor.insert(mobileDevice, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public ClientDevice getClientDevice(QueryExpress queryExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return clientDeviceAccessor.get(queryExpress, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public List<ClientDevice> queryClientDevice(QueryExpress queryExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return clientDeviceAccessor.query(queryExpress, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public SocialMessage insertSocialMessage(SocialMessage socialMessage) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return socialMessageAccessor.insert(socialMessage, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public SocialMessage getSocialMessage(String ownUno, QueryExpress queryExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return socialMessageAccessor.getSocialMessage(ownUno, queryExpress, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean updateSocialMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);

			return socialMessageAccessor.updateSocialMessage(ownUno, updateExpress, queryExpress, conn) > 0;
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public PageRows<SocialMessage> querySocialMessage(String ownUno, QueryExpress queryExpress, Pagination page) throws DbException {
		Connection conn = null;
		PageRows<SocialMessage> returnValue = new PageRows<SocialMessage>();
		try {
			conn = DbConnFactory.factory(dataSourceName);
			returnValue.setRows(socialMessageAccessor.querySocialMessageList(ownUno, queryExpress, page, conn));
			returnValue.setPage(page);
			return returnValue;
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public SocialNotice getSocialNotice(String ownUno) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);
			return socialNoticeAccessor.get(ownUno, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}


	public SocialNotice insertSocialNotice(SocialNotice receiveNotice) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);
			return socialNoticeAccessor.insert(receiveNotice, conn);
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean updateSocialNotice(String ownUno, UpdateExpress updateExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);
			return socialNoticeAccessor.update(ownUno, updateExpress, conn) > 0;
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public PageRows<ClientDevice> queryClientDeviceByPage(QueryExpress add, Pagination page) throws DbException {
		Connection conn = null;
		PageRows<ClientDevice> pageRows = new PageRows<ClientDevice>();
		try {
			conn = DbConnFactory.factory(dataSourceName);
			List<ClientDevice> list = clientDeviceAccessor.query(add, page, conn);
			pageRows.setPage(page);
			pageRows.setRows(list);
			return pageRows;
		} finally {
			DataBaseUtil.closeConnection(conn);
		}
	}

	public boolean modifyClientDevice(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
		Connection conn = null;
		try {
			conn = DbConnFactory.factory(dataSourceName);
			return clientDeviceAccessor.update(queryExpress, updateExpress, conn) > 0;
		} finally {
			DataBaseUtil.closeConnection(conn);

		}
	}

	public void pushMessageNoticeTime(String key, String value) throws DbException {
		redisManager.setSec(key, value, 0);
	}

	public String getMessageNoticeTime(String key) {
		return redisManager.get(key);
	}

}
