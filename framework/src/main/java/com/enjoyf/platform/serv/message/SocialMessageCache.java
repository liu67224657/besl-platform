/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.message;

import com.enjoyf.platform.service.message.SocialMessage;
import com.enjoyf.platform.service.message.SocialNotice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class SocialMessageCache {

	private static final long TIME_OUT_SEC = 60l * 15l;

	private static final int SOCIAL_MESSAGE_PAGE_SIZE = 10;

	private static final String PREFIX_SERVICE = "message";
	private static final String PREFIX_KEY_IDLIST = "_socialmsg_idlist_";
	private static final String PREFIX_KEY_ID = "_socialmsg_id_";

	private static final String PREFIX_SOCIAL_NOTICE = "_socialnotice_";

	private MemCachedConfig config;

	private MemCachedManager manager;

	SocialMessageCache(MemCachedConfig config) {
		this.config = config;
		manager = new MemCachedManager(config);
	}

	//////////////////////////////////////////////////////////
	public void putSocialMessageIdList(String ownUno, int msgType, int curPage, List<Long> msgIdList) {
		manager.put(PREFIX_SERVICE + PREFIX_KEY_IDLIST + ownUno + msgType + curPage, msgIdList, TIME_OUT_SEC);
	}

	public List<Long> getSocialMessageIdList(String ownUno, int msgType, int curPage) {
		Object idList = manager.get(PREFIX_SERVICE + PREFIX_KEY_IDLIST + ownUno + msgType + curPage);
		if (idList == null) {
			return null;
		}
		return (List<Long>) idList;
	}

	public boolean removeSocialMessageIdList(String ownUno, int msgType, int totals) {
		Pagination page = new Pagination(totals, 1, SOCIAL_MESSAGE_PAGE_SIZE);
		manager.remove(PREFIX_SERVICE + PREFIX_KEY_IDLIST + ownUno + msgType + page.getMaxPage());
		return true;
	}

	public void putSocialMessage(String ownUno, Long msgId, SocialMessage socialMessage) {
		manager.put(PREFIX_SERVICE + PREFIX_KEY_ID + ownUno + msgId, socialMessage, TIME_OUT_SEC);
	}

	public SocialMessage getSocialMessage(String ownUno, Long msgId) {
		Object socialMessage = manager.get(PREFIX_SERVICE + PREFIX_KEY_ID + ownUno + msgId);
		if (socialMessage == null) {
			return null;
		}
		return (SocialMessage) socialMessage;
	}

	public boolean removeSocialMessage(String uno, Long msgId) {
		return manager.remove(PREFIX_SERVICE + PREFIX_KEY_ID + uno + msgId);
	}

	public void putSocialNotice(String ownUno, SocialNotice socialNotice) {
		manager.put(PREFIX_SERVICE + PREFIX_SOCIAL_NOTICE + ownUno, socialNotice, TIME_OUT_SEC);
	}

	public SocialNotice getSocialNotice(String ownUno) {
		Object socialNotice = manager.get(PREFIX_SERVICE + PREFIX_SOCIAL_NOTICE + ownUno);
		if (socialNotice == null) {
			return null;
		}
		return (SocialNotice) socialNotice;
	}

	public boolean removeSocialNotice(String ownUno) {
		return manager.remove(PREFIX_SERVICE + PREFIX_SOCIAL_NOTICE + ownUno);
	}

	//messagenotice
	public void putMessageNoticeTime(String key, String value) {
		manager.put(key, value, TIME_OUT_SEC);
	}

	public String getMessageNoticeTime(String key) {
		Object socialNotice = manager.get(key);
		if (socialNotice == null) {
			return null;
		}
		return socialNotice.toString();
	}

	public boolean removeMessageNoticeTime(String key) {
		return manager.remove(key);
	}
}
