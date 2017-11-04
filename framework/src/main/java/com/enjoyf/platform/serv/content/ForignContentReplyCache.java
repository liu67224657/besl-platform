package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.service.content.ForignContentReplyLog;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:19
 * Description:
 */
public class ForignContentReplyCache {

    private static final long TIME_OUT_SEC = 60l * 5l;

    private static final long POST_LIMIT_SEC = 10l;


    private static final String PREFIX_CONTENT_2_REPLY_PAGE = "_crpage_";
    private static final String PREFIX_CONTENT = "_fc_";

    private static final String PREFIX_CONTENT_REPPLY = "_reply_";

    private static final String PREFIX_HOT_CONTENT_REPPLY = "_hr_";


	private static final String PREFIX_MOBILE_GAME_GAG_REPPLY = "_mobilegame_gag_";

    private static final String PREFIX_ARCHIVEID_2_TYPEDIR = "_aid2dir_";
    private static final String PREFIX_RIGHT_HTML = "_rhtml_";

    private static final String PREFIX_POST_LIMMIT = "_plimit_";

    private static final String PREFIX_LOG = "_log_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    ForignContentReplyCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    public void putForignContent(ForignContent forignContent) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + forignContent.getContentId(), forignContent, TIME_OUT_SEC);
    }

    public ForignContent getForignContent(long contentId) {
        Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + contentId);
        if (content == null) {
            return null;
        }
        return (ForignContent) content;
    }

    public boolean deleteForignContent(long contentId) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT + contentId);
    }

    ////////////////////////////////////////////////////////////////
    public void putContentReplyIdList(long contentId, long rootId, int pageNo, List<Long> replyIdList) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo, replyIdList, TIME_OUT_SEC);
    }

    public List<Long> getContentReplyIdList(long contentId, long rootId, int pageNo) {
        Object replyIdList = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo);
        if (replyIdList == null) {
            return null;
        }
        return (List<Long>) replyIdList;
    }

    public boolean deleteContentReplyIdList(long contentId, long rootId, int pageNo) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_2_REPLY_PAGE + contentId + "_" + rootId + "_" + pageNo);
    }

    ////////////////////////////////////////////////////////////////
    public void putForignContentReply(ForignContentReply forignContentReply) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + forignContentReply.getReplyId(), forignContentReply, TIME_OUT_SEC);
    }

    public ForignContentReply getForignContentReply(long replyId) {
        Object content = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + replyId);
        if (content == null) {
            return null;
        }
        return (ForignContentReply) content;
    }

    public boolean deleteForignContentReply(long replyId) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_CONTENT_REPPLY + replyId);
    }

    ////////////////////////////////////////////////////////////////
    public void putHotReplyList(long contentId, List<Long> HotReplyList) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_HOT_CONTENT_REPPLY + contentId, HotReplyList, TIME_OUT_SEC);
    }

    public List<Long> getHotReplyList(long contentId) {
        Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_HOT_CONTENT_REPPLY + contentId);
        if (object == null) {
            return null;
        }
        return (List<Long>) object;
    }

    public boolean deleteHotReplyList(long contentId) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_HOT_CONTENT_REPPLY + contentId);
    }

    ////////////////////////////////////////////////////////////////
//    public void putTypeDirByArchiveId(String archiveId, String typeDir) {
//        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_ARCHIVEID_2_TYPEDIR + archiveId, typeDir, TIME_OUT_SEC);
//    }
//
//    public String getTypeDirByArchiveId(String archiveId) {
//        Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_ARCHIVEID_2_TYPEDIR + archiveId);
//        if (object == null) {
//            return null;
//        }
//        return (String) object;
//    }
//
//    public boolean deleteTypedirByArchiveId(String archiveId) {
//        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_ARCHIVEID_2_TYPEDIR + archiveId);
//    }

    public void putRightHtml(long contentId, String html) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_RIGHT_HTML + contentId, html, TIME_OUT_SEC);
    }

    public String getRightHtml(long contentId) {
        Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_RIGHT_HTML + contentId);
        if (object == null) {
            return null;
        }
        return (String) object;
    }

    public boolean deleteRightHtml(long contentId) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_RIGHT_HTML + contentId);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void putPostLimit(String uno, int times) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_POST_LIMMIT + uno, times, POST_LIMIT_SEC);
    }

    public int getPostLimit(String uno) {
        Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_POST_LIMMIT + uno);
        if (object == null) {
            return -1;
        }
        return Integer.parseInt(String.valueOf(object));
    }

    public boolean removePostLimit(String uno) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_POST_LIMMIT + uno);
    }


    public ForignContentReplyLog getForignContentReplyLog(long replyId, String uno, int code) {
        Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_LOG + replyId + uno + code);
        if (object == null) {
            return null;
        }
        return (ForignContentReplyLog) object;
    }

    public void putForignContentReplyLog(long replyId, String uno, int code, ForignContentReplyLog log) {
        manager.put(ContentConstants.SERVICE_SECTION + PREFIX_LOG + replyId + uno + code, log, TIME_OUT_SEC);
    }

    public boolean removeForignContentReplyLog(long replyId, String uno, int code) {
        return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_LOG + replyId + uno + code);
    }


	public void putMobileGameGagReplyList(long contentId, List<ForignContentReply> forignContentReplyList) {
		manager.put(ContentConstants.SERVICE_SECTION + PREFIX_MOBILE_GAME_GAG_REPPLY + contentId, forignContentReplyList, TIME_OUT_SEC);
	}

	public List<ForignContentReply> getMobileGameGagReplyList(long contentId) {
		Object object = manager.get(ContentConstants.SERVICE_SECTION + PREFIX_MOBILE_GAME_GAG_REPPLY + contentId);
		if (object == null) {
			return null;
		}
		return (List<ForignContentReply>) object;
	}

	public boolean deleteMobileGameGagReplyList(long contentId) {
		return manager.remove(ContentConstants.SERVICE_SECTION + PREFIX_MOBILE_GAME_GAG_REPPLY + contentId);
	}
}
