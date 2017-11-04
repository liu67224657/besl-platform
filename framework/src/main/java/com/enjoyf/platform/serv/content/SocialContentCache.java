package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;
import java.util.Set;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:19
 * Description:
 */
public class SocialContentCache {
	private static final long TIME_OUT_SEC = 60l * 5l;
	private static final String PREFIX_CONTENT = "_scontent_";
	private static final String PREFIX_CONTENT_2_REPLY_PAGE = "_scrpage_";
	private static final String PREFIX_CONTENT_REPPLY = "_social_reply_";
	private static final String PREFIX_SOCIAL_CONTENT = "_socialcontent_";
	private static final String PREFIX_SOCIAL_HOT_CONTENT = "_socialhotcontent_";
	private static final String PREFIX_SOCIAL_ACTION = "_socialaction_uno_type_";
	private static final String PREFIX_SOCIAL_ACTIVITY = "_socialactivity_aid_";
	private static final String PREFIX_SOCIAL_BGAUDIO = "_socialbgaudio_bgaid_";

	private MemCachedConfig config;

	private MemCachedManager manager;

	SocialContentCache(MemCachedConfig config) {
		this.config = config;
		manager = new MemCachedManager(this.config);
	}

	public void putContent(SocialContent socialContent) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + socialContent.getContentId(), socialContent, TIME_OUT_SEC);
	}

	public SocialContent getContent(long contentId) {
		Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + contentId);
		if (content == null) {
			return null;
		}
		return (SocialContent) content;
	}

	public boolean deleteContent(long contentId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + contentId);
	}

	////////////////////////////////////////////////////////////////
	public void putReplyIdList(long contentId, long rootId, int pageNo, List<Long> replyIdList) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo, replyIdList, TIME_OUT_SEC);
	}

	public List<Long> getReplyIdList(long contentId, long rootId, int pageNo) {
		Object replyIdList = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo);
		if (replyIdList == null) {
			return null;
		}
		return (List<Long>) replyIdList;
	}

	public boolean deleteReplyIdList(long contentId, long rootId, int pageNo) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo);
	}

	////////////////////////////////////////////////////////////////

	public void putContentReply(SocialContentReply forignContentReply) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + forignContentReply.getReplyId(), forignContentReply, TIME_OUT_SEC);
	}

	public SocialContentReply getContentReply(long replyId) {
		Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + replyId);
		if (content == null) {
			return null;
		}
		return (SocialContentReply) content;
	}

	public boolean deleteContentReply(long replyId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + replyId);
	}

	////////////////////////////////////////////////////////////////

	public void putSocialContent(long conentId, SocialContent socialContent) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_CONTENT + conentId, socialContent, TIME_OUT_SEC);
	}

	public SocialContent getSocialContent(long conentId) {
		Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_CONTENT + conentId);
		if (content == null) {
			return null;
		}
		return (SocialContent) content;
	}

	public boolean deleteSocialContent(long conentId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_CONTENT + conentId);
	}

	public void putSocialHotContent(long socialContentId, SocialHotContent socialHotContent) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_HOT_CONTENT + socialContentId, socialHotContent, TIME_OUT_SEC);
	}

	public SocialHotContent getSocialHotContent(long socialContentId) {
		Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_HOT_CONTENT + socialContentId);
		if (content == null) {
			return null;
		}
		return (SocialHotContent) content;
	}

	public boolean deleteSocialHotContent(long socialContentId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_HOT_CONTENT + socialContentId);
	}

	public void putSocialContentIdSetByAction(String uno, int code, Long contentId) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_ACTION + uno + code + contentId, contentId, TIME_OUT_SEC);
	}

	public Long getSocialContentIdSetByAction(String uno, Long contentId, int code) {
		Object action = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_ACTION + uno + code + contentId);
		if (action == null) {
			return null;
		}
		return (Long) action;
	}

	public boolean deleteSocialContentIdSetByAction(String uno, Long contentId, int code) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_ACTION + uno + code + contentId);
	}

	public void putSocialActivity(long activityId, SocialActivity activity) {
		manager.put(ContentConstants.SERVICE_PREFIX + PREFIX_SOCIAL_ACTIVITY + activityId, activity, 6L * 60L * 60L);
	}

	public SocialActivity getSocialActivity(long activityId) {
		Object activity = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_ACTIVITY + activityId);
		if (activity == null) {
			return null;
		}
		return (SocialActivity) activity;
	}

	public boolean deleteSocialActivity(long activityId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_ACTIVITY + activityId);
	}

	public void putSocialBgAudio(long audioId, SocialBackgroundAudio bgAudio) {
		manager.put(ContentConstants.SERVICE_PREFIX + PREFIX_SOCIAL_BGAUDIO + audioId, bgAudio, 6L * 60L * 60L);
	}

	public SocialBackgroundAudio getSocialBgAudio(long audioId) {
		Object activity = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_BGAUDIO + audioId);
		if (activity == null) {
			return null;
		}
		return (SocialBackgroundAudio) activity;
	}

	public boolean deleteSocialBgAudio(long audioId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_SOCIAL_BGAUDIO + audioId);
	}
}
