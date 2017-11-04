/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ContentConstants {
	public static final String SERVICE_SECTION = "contentservice";
	public static final String SERVICE_PREFIX = "contentserver";
	public static final String SERVICE_TYPE = "contentserver";

    //
	private static TransProfileContainer transProfileContainer = new TransProfileContainer();

	public static final byte CONTENT_POST = 1;          //发博�    public static final byte CONTENT_REMOVE = 3;        //删除博文
	public static final byte CONTENT_MODIFY = 2;        //修改博文
	public static final byte CONTENT_MODIFY_NUM = 3;    //修改博文中：收藏数，转发�..

	public static final byte CONTENT_QUERY_BY_CNTIDS_SET = 4;  //
	public static final byte CONTENT_QUERY_BY_UNO = 5;  //
	public static final byte CONTENT_GET_BY_CNTID = 6;   //查询博文，根据UNO，ID
	public static final byte CONTENT_SEARCH_UNO_TOPIC = 7;
	public static final byte CONTENT_SEARCH_TEXT_UNOS = 8;
	public static final byte CONTENT_QUERY_BY_DATE_STEP = 9;//根据时间段查询博�
	public static final byte CONTENT_QUERY_BY_LASTEST_UNOS = 10;  //

	public static final byte CONTENT_INTERACTION_CREATE = 11;                   //发回�    public static final byte CONTENT_INTERACTION_REMOVE = 14;                 //删除回应

	public static final byte WALL_CONTENT_QUERY = 12;

	public static final byte CONTENT_INTERACTION_QUERY_BY_CID = 13;           //暂时没有使用
	public static final byte CONTENT_INTERACTION_QUERY_BY_CID_AND_TYPE = 14;           //暂时没有使用
	public static final byte CONTENT_INTERACTION_QUERY_BY_CIDRIDS_MAP = 15;           //暂时没有使用
	public static final byte CONTENT_REPLY_GET_BY_RID_CID_UNO = 16;               //查询回应，by rid,cid, uno
	public static final byte CONTENT_INTERACTION_QUERY_BY_EXPRESS = 17;
	public static final byte CURRENTPAGE_CONTENT_INTERACTION_QUERY_BY_EXPRESS = 18;
	public static final byte CURRENTPAGE_CONTENT_INTERACTION_CHILDREN_QUERY_BY_EXPRESS = 19;
	public static final byte CONTENT_REPLY_QUERY_BY_CID_IUNO = 20;

	public static final byte CONENT_RELATION_CREATE = 21;
	public static final byte RESOURCE_FILE_POST = 22;

	//外部链接
	public static final byte GET_FORIGNCONTENT_BY_URL = 23;
	public static final byte GET_FORIGNCONTENT_BY_ID = 24;
	public static final byte POST_FORIGNREPLY = 25;
	public static final byte GET_FORIGNREPLYLIST_BY_CONETNETID_ROOTID = 26;
	public static final byte QUERY_HOTFROIGNHOTREPLY_BYCONTENTID = 27;
	public static final byte REMOVE_FORIGNCONTENT_REPLY = 28;
	public static final byte MODIFY_FORIGNREPLY_BY_REPLYID = 29;
	public static final byte GET_CMSRIGHT_HTML = 30;
	public static final byte QUERY_FORIGN_CONTENTREPLY_BY_SET = 31;
	public static final byte GET_FORIGNCONTENT_BY_SET = 32;

	// VIEW QUERY BY PARAM
	public static final byte QUERY_CONTENT_BY_QUERYEXPRESS = 33;
	// VIEW QUERY BY PARAM
	public static final byte QUERY_CONTENT_REPLY_BY_QUERYEXPRESS = 34;
	public static final byte QUERY_CONTENT_REPLYTIMES = 35;
	// VIEW QUERY BY PARAM  to update
	public static final byte CONTENT_REPLY_MODIFY = 36;

	public static final byte RECIEVE_EVENT = 37;

	public static final byte QUERY_ACTIVITY_BY_PAGE = 38;
	public static final byte INSERT_ACTIVITY = 39;
	public static final byte GET_ACTIVITY_BY_ID = 40;
	public static final byte MODIFY_ACTIVITY_BY_ID = 41;
	public static final byte INSERT_ACTIVITY_RELATION = 42;
	public static final byte QUERY_ACTIVITY_RELATION_BY_LIST = 43;
	public static final byte GET_ACTIVITY_RELATION = 44;
	public static final byte QUERY_LASTEST_ACTIVITY = 45;
	public static final byte QUERY_ACTIVITY_BY_REALTIONIDS = 46;
	public static final byte QUERY_ACTIVITY_BY_ACTIVITYIDS = 47;


	public static final byte QUERY_CONTENT_BY_QUERY = 52;


	public static final byte QUERY_FORIGN_CONTENT_REPLY_BY_PAGE = 53;

	public static final byte MODIFY_FORIGN_CONTENT_REPLY = 54;
	public static final byte GET_FORIGN_CONTENT_REPLY = 55;
	public static final byte QUERY_FORIGN_CONTENT_REPLY = 56;
	public static final byte CREATE_FORIGN_CONTENT_REPLY_LOG = 57;
	public static final byte GET_FORIGN_CONTENT_REPLY_LOG = 58;
	public static final byte AGREE_FORIGN_CONTENT_REPLY = 59;
	public static final byte GET_FORIGN_CONTENT_BY_FID_CDOMAIN = 60;
	public static final byte UPDATE_FORIGN_CONTENT = 61;

	public static final byte POST_SOCIALCONTENT = 62;//社区端发文章
	public static final byte POST_SOCIALCONTENT_REPLY = 63;//
	public static final byte QUERY_SOCIALCONTENT_REPLY = 64;//
	public static final byte REMOVE_SOCIALCONTENT_REPLY = 65;//

	public static final byte CREATE_SOCIALCONTENT_ACTION = 66;//
	public static final byte QUERY_SOCIALCONTENT_ACTION = 67;//
	public static final byte REMOVE_SOCIALCONTENT_ACTION = 68;//

	public static final byte QUERY_SOCIAL_CONTENT = 69;//

	public static final byte REMOVE_SOCIALCONTENT = 70;//社区端发文章
	public static final byte QUERY_SOCIAL_HOT_CONTENT_BY_PAGE = 71;
	public static final byte INSERT_SOCIAL_HOT_CONTENT = 72;

	public static final byte GET_SOCIALCONTENT_BYCONTENTID = 73;
	public static final byte CHECK_SOCIAL_CONTENT_ACTION = 74;
	public static final byte QUERY_SOCIAL_CONTENT_BY_IDSET = 75;
	public static final byte GET_SOCIAL_HOT_CONTENT = 76;
	public static final byte MODIFY_SOCIAL_HOT_CONTENT = 77;
	public static final byte QUERY_SOCIAL_CONTENT_MAP_BY_IDSET = 78;
	public static final byte QUERY_SOCIALREPORT_BY_PAGE = 79;
	public static final byte CREATE_SOCIALREPORT = 80;
	public static final byte MODIFY_SOCIALREPORT = 81;
	public static final byte GET_SOCIALREPORTY = 82;

	public static final byte INSERT_SOCIALCONTENT_PLAY = 83;
	public static final byte QUERY_SOCIALCONTENT_BY_PAGE = 84;
	public static final byte QUERY_SOCIAL_HOT_CONTENT = 85;
	public static final byte GET_SOCIALCONTENT_BY_ID = 86;
	public static final byte QUERY_SOCIALCONTENT_REPLY_BYPAGE = 87;

	public static final byte RECOVER_SOCIALCONTENTR_EPLY = 88;
	public static final byte QUERY_SOCIALHOTCONTENT_PAGEROWS = 89;
	public static final byte QUERY_SOCIALHOTCONTENT_LIST = 90;
	public static final byte GET_SOCIAL_CONTENT_ACTION = 91;


	public static final byte CONTENT_REMOVE = 92;
	public static final byte CONTENT_INTERACTION_REMOVE = 93;

	public static final byte INSERT_SOCIAL_ACTIVITY = 94;
	public static final byte GET_SOCIAL_ACTIVITY = 95;
	public static final byte QUERY_SOCIAL_ACTIVITY = 96;
	public static final byte QUERY_SOCIAL_ACTIVITY_BY_PAGE = 97;
	public static final byte MODIFY_SOCIAL_ACTIVITY = 98;
	public static final byte QUERY_SOCIAL_CONTENT_ACTIVITY_BY_PAGE = 99;
	public static final byte QUERY_SOCIAL_TAG_BY_PAGE = 100;
	public static final byte QUERY_SOCIAL_WATERMARK_BY_PAGE = 101;
	public static final byte INSERT_SOCIAL_WATERMARK = 102;
	public static final byte GET_SOCIAL_WATERMARK = 103;
	public static final byte MODIFY_SOCIAL_WATERMARK = 104;
	public static final byte QUERY_SOCIAL_WATERMARK_BY_NEXT = 105;
	public static final byte QUERY_SOCIAL_ACTIVITY_BY_NEXT = 106;
	public static final byte QUERY_SOCIAL_BGAUDIO_BY_PAGE = 107;
	public static final byte INSERT_SOCIAL_BGAUDIO = 108;
	public static final byte GET_SOCIAL_BGAUDIO = 109;
	public static final byte MODIFY_SOCIAL_BGAUDIO = 110;
	public static final byte QUERY_SOCIAL_BGAUDIO_BY_NEXT = 111;
	public static final byte GET_SOCIAL_CONTENT_ACTIVITY = 112;
	public static final byte QUERY_SOCIAL_CONTENT_ACTIVITY_BY_NEXT = 113;
	public static final byte QUERY_SOCIAL_ACTIVITY_BY_IDSET = 114;
	public static final byte MODIFY_SOCIAL_CONTENT_ACTIVITY = 115;
	public static final byte SEND_OUT_SOCIAL_LOG_EVENT = 116;
	public static final byte QUERY_ACTIVITY_BY_LIST = 117;
	public static final byte MODIFY_SOCIALCONTENT = 118;
	public static final byte QUERY_MOBILE_GAME_GAG_FORIGNREPLY = 119;

    public static final byte GET_WIKI_CONTENT = 120;

	static {
		transProfileContainer.put(new TransProfile(CONTENT_POST, "CONTENT_POST"));
		transProfileContainer.put(new TransProfile(CONTENT_REMOVE, "CONTENT_REMOVE"));
		transProfileContainer.put(new TransProfile(CONTENT_MODIFY, "CONTENT_MODIFY"));
		transProfileContainer.put(new TransProfile(CONTENT_MODIFY_NUM, "CONTENT_MODIFY_NUM"));

		transProfileContainer.put(new TransProfile(CONTENT_GET_BY_CNTID, "CONTENT_GET_BY_CNTID"));
		transProfileContainer.put(new TransProfile(CONTENT_QUERY_BY_CNTIDS_SET, "CONTENT_QUERY_BY_CNTIDS_SET"));
		transProfileContainer.put(new TransProfile(CONTENT_QUERY_BY_UNO, "CONTENT_QUERY_BY_UNO"));
		transProfileContainer.put(new TransProfile(CONTENT_QUERY_BY_DATE_STEP, "CONTENT_QUERY_BY_DATE_STEP"));

		transProfileContainer.put(new TransProfile(CONTENT_SEARCH_UNO_TOPIC, "CONTENT_SEARCH_UNO_TOPIC"));
		transProfileContainer.put(new TransProfile(CONTENT_SEARCH_TEXT_UNOS, "CONTENT_SEARCH_TEXT_UNOS"));

		transProfileContainer.put(new TransProfile(CONTENT_INTERACTION_CREATE, "CONTENT_REPLY_POST"));
		transProfileContainer.put(new TransProfile(CONTENT_INTERACTION_REMOVE, "CONTENT_REPLY_REMOVE"));

		transProfileContainer.put(new TransProfile(CONTENT_REPLY_GET_BY_RID_CID_UNO, "CONTENT_REPLY_GET_BY_RID_CID_UNO"));
		transProfileContainer.put(new TransProfile(CONTENT_INTERACTION_QUERY_BY_CID, "CONTENT_REPLY_QUERY_BY_CID"));
		transProfileContainer.put(new TransProfile(CONTENT_INTERACTION_QUERY_BY_CIDRIDS_MAP, "CONTENT_REPLY_QUERY_BY_CIDRIDS_MAP"));
		transProfileContainer.put(new TransProfile(CONTENT_INTERACTION_QUERY_BY_EXPRESS, "CONTENT_INTERACTION_QUERY_BY_EXPRESS"));
		transProfileContainer.put(new TransProfile(CURRENTPAGE_CONTENT_INTERACTION_QUERY_BY_EXPRESS, "CURRENTPAGE_CONTENT_INTERACTION_QUERY_BY_EXPRESS"));
		transProfileContainer.put(new TransProfile(CURRENTPAGE_CONTENT_INTERACTION_CHILDREN_QUERY_BY_EXPRESS, "CURRENTPAGE_CONTENT_INTERACTION_CHILDREN_QUERY_BY_EXPRESS"));


		transProfileContainer.put(new TransProfile(RESOURCE_FILE_POST, "RESOURCE_FILE_POST"));

		transProfileContainer.put(new TransProfile(WALL_CONTENT_QUERY, "WALL_CONTENT_QUERY"));

		transProfileContainer.put(new TransProfile(QUERY_CONTENT_BY_QUERYEXPRESS, "QUERY_CONTENT_BY_QUERYEXPRESS"));
		transProfileContainer.put(new TransProfile(QUERY_CONTENT_REPLY_BY_QUERYEXPRESS, "QUERY_CONTENT_REPLY_BY_QUERYEXPRESS"));

		transProfileContainer.put(new TransProfile(QUERY_CONTENT_REPLYTIMES, "QUERY_CONTENT_REPLYTIMES"));

		transProfileContainer.put(new TransProfile(CONENT_RELATION_CREATE, "CREATE_CONENT_RELATION"));
		//
		transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));

		transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_BY_PAGE, "QUERY_ACTIVITY_BY_PAGE"));
		transProfileContainer.put(new TransProfile(INSERT_ACTIVITY, "INSERT_ACTIVITY"));
		transProfileContainer.put(new TransProfile(GET_ACTIVITY_BY_ID, "GET_ACTIVITY_BY_ID"));
		transProfileContainer.put(new TransProfile(MODIFY_ACTIVITY_BY_ID, "MODIFY_ACTIVITY_BY_ID"));

		transProfileContainer.put(new TransProfile(INSERT_ACTIVITY_RELATION, "INSERT_ACTIVITY_RELATION"));
		transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_RELATION_BY_LIST, "QUERY_ACTIVITY_RELATION_BY_LIST"));
		transProfileContainer.put(new TransProfile(GET_ACTIVITY_RELATION, "GET_ACTIVITY_RELATION"));
		transProfileContainer.put(new TransProfile(QUERY_LASTEST_ACTIVITY, "QUERY_LASTEST_ACTIVITY"));
		transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_BY_REALTIONIDS, "QUERY_ACTIVITY_BY_REALTIONIDS"));

		transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_BY_ACTIVITYIDS, "QUERY_ACTIVITY_BY_ACTIVITYIDS"));

		transProfileContainer.put(new TransProfile(GET_FORIGNCONTENT_BY_URL, "GET_FORIGNCONTENT_BY_URL"));
		transProfileContainer.put(new TransProfile(GET_FORIGNCONTENT_BY_ID, "GET_FORIGNCONTENT_BY_ID"));

		transProfileContainer.put(new TransProfile(POST_FORIGNREPLY, "POST_FORIGNREPLY"));
		transProfileContainer.put(new TransProfile(GET_FORIGNREPLYLIST_BY_CONETNETID_ROOTID, "GET_FORIGNREPLYLIST_BY_CONETNETID_ROOTID"));
		transProfileContainer.put(new TransProfile(QUERY_HOTFROIGNHOTREPLY_BYCONTENTID, "QUERY_HOTFROIGNHOTREPLY_BYCONTENTID"));
		transProfileContainer.put(new TransProfile(REMOVE_FORIGNCONTENT_REPLY, "DELETE_FORIGNCONTENT_REPLY"));

		transProfileContainer.put(new TransProfile(QUERY_CONTENT_BY_QUERY, "QUERY_CONTENT_BY_QUERY"));

		transProfileContainer.put(new TransProfile(QUERY_FORIGN_CONTENT_REPLY_BY_PAGE, "QUERY_FORIGN_CONTENT_REPLY_BY_PAGE"));
		transProfileContainer.put(new TransProfile(MODIFY_FORIGN_CONTENT_REPLY, "MODIFY_FORIGN_CONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(GET_FORIGN_CONTENT_REPLY, "GET_FORIGN_CONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(QUERY_FORIGN_CONTENT_REPLY, "QUERY_FORIGN_CONTENT_REPLY"));

		transProfileContainer.put(new TransProfile(CREATE_FORIGN_CONTENT_REPLY_LOG, "CREATE_FORIGN_CONTENT_REPLY_LOG"));
		transProfileContainer.put(new TransProfile(GET_FORIGN_CONTENT_REPLY_LOG, "GET_FORIGN_CONTENT_REPLY_LOG"));

		transProfileContainer.put(new TransProfile(MODIFY_FORIGNREPLY_BY_REPLYID, "MODIFY_FORIGNREPLY_BY_REPLYID"));
		transProfileContainer.put(new TransProfile(GET_CMSRIGHT_HTML, "GET_CMSRIGHT_HTML"));

		transProfileContainer.put(new TransProfile(AGREE_FORIGN_CONTENT_REPLY, "AGREE_FORIGN_CONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(QUERY_FORIGN_CONTENTREPLY_BY_SET, "QUERY_FORIGN_CONTENTREPLY_BY_SET"));
		transProfileContainer.put(new TransProfile(GET_FORIGNCONTENT_BY_SET, "GET_FORIGNCONTENT_BY_SET"));
		transProfileContainer.put(new TransProfile(GET_FORIGN_CONTENT_BY_FID_CDOMAIN, "GET_FORIGN_CONTENT_BY_FID_CDOMAIN"));
		transProfileContainer.put(new TransProfile(UPDATE_FORIGN_CONTENT, "UPDATE_FORIGN_CONTENT"));

		transProfileContainer.put(new TransProfile(POST_SOCIALCONTENT, "POST_SOCIALCONTENT"));
		transProfileContainer.put(new TransProfile(POST_SOCIALCONTENT_REPLY, "POST_SOCIALCONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALCONTENT_REPLY, "QUERY_SOCIALCONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(REMOVE_SOCIALCONTENT_REPLY, "REMOVE_SOCIALCONTENT_REPLY"));
		transProfileContainer.put(new TransProfile(CREATE_SOCIALCONTENT_ACTION, "CREATE_SOCIALCONTENT_ACTION"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALCONTENT_ACTION, "QUERY_SOCIALCONTENT_ACTION"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_CONTENT, "QUERY_SOCIAL_CONTENT"));

		transProfileContainer.put(new TransProfile(GET_SOCIALCONTENT_BYCONTENTID, "GET_SOCIALCONTENT_BYCONTENTID"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_HOT_CONTENT_BY_PAGE, "QUERY_SOCIAL_HOT_CONTENT_BY_PAGE"));
		transProfileContainer.put(new TransProfile(INSERT_SOCIAL_HOT_CONTENT, "INSERT_SOCIAL_HOT_CONTENT"));
		transProfileContainer.put(new TransProfile(CHECK_SOCIAL_CONTENT_ACTION, "CHECK_SOCIAL_CONTENT_ACTION"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_CONTENT_BY_IDSET, "QUERY_SOCIAL_CONTENT_BY_IDSET"));
		transProfileContainer.put(new TransProfile(GET_SOCIAL_HOT_CONTENT, "GET_SOCIAL_HOT_CONTENT"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_HOT_CONTENT, "MODIFY_SOCIAL_HOT_CONTENT"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_CONTENT_MAP_BY_IDSET, "QUERY_SOCIAL_CONTENT_MAP_BY_IDSET"));

		transProfileContainer.put(new TransProfile(REMOVE_SOCIALCONTENT, "REMOVE_SOCIALCONTENT"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALREPORT_BY_PAGE, "QUERY_SOCIALREPORT_BY_PAGE"));
		transProfileContainer.put(new TransProfile(CREATE_SOCIALREPORT, "CREATE_SOCIALREPORT"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIALREPORT, "MODIFY_SOCIALREPORT"));
		transProfileContainer.put(new TransProfile(GET_SOCIALREPORTY, "GET_SOCIALREPORTY"));

		transProfileContainer.put(new TransProfile(INSERT_SOCIALCONTENT_PLAY, "INSERT_SOCIALCONTENT_PLAY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALCONTENT_BY_PAGE, "QUERY_SOCIALCONTENT_BY_PAGE"));
		transProfileContainer.put(new TransProfile(GET_SOCIALCONTENT_BY_ID, "GET_SOCIALCONTENT_BY_ID"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALCONTENT_REPLY_BYPAGE, "QUERY_SOCIALCONTENT_REPLY_BYPAGE"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_HOT_CONTENT, "QUERY_SOCIAL_HOT_CONTENT"));
		transProfileContainer.put(new TransProfile(RECOVER_SOCIALCONTENTR_EPLY, "RECOVER_SOCIALCONTENTR_EPLY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALHOTCONTENT_PAGEROWS, "QUERY_SOCIALHOTCONTENT_PAGEROWS"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIALHOTCONTENT_LIST, "QUERY_SOCIALHOTCONTENT_LIST"));


		transProfileContainer.put(new TransProfile(GET_SOCIAL_CONTENT_ACTION, "GET_SOCIAL_CONTENT_ACTION"));

		transProfileContainer.put(new TransProfile(INSERT_SOCIAL_ACTIVITY, "INSERT_SOCIAL_ACTIVITY"));
		transProfileContainer.put(new TransProfile(GET_SOCIAL_ACTIVITY, "GET_SOCIAL_ACTIVITY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_ACTIVITY, "QUERY_SOCIAL_ACTIVITY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_ACTIVITY_BY_PAGE, "QUERY_SOCIAL_ACTIVITY_BY_PAGE"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_ACTIVITY, "MODIFY_SOCIAL_ACTIVITY"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_CONTENT_ACTIVITY_BY_PAGE, "QUERY_SOCIAL_CONTENT_ACTIVITY_BY_PAGE"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_TAG_BY_PAGE, "QUERY_SOCIAL_TAG_BY_PAGE"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_WATERMARK_BY_PAGE, "QUERY_SOCIAL_WATERMARK_BY_PAGE"));
		transProfileContainer.put(new TransProfile(INSERT_SOCIAL_WATERMARK, "INSERT_SOCIAL_WATERMARK"));
		transProfileContainer.put(new TransProfile(GET_SOCIAL_WATERMARK, "GET_SOCIAL_WATERMARK"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_WATERMARK, "MODIFY_SOCIAL_WATERMARK"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_WATERMARK_BY_NEXT, "QUERY_SOCIAL_WATERMARK_BY_NEXT"));

		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_ACTIVITY_BY_NEXT, "QUERY_SOCIAL_ACTIVITY_BY_NEXT"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_BGAUDIO_BY_PAGE, "QUERY_SOCIAL_BGAUDIO_BY_PAGE"));
		transProfileContainer.put(new TransProfile(INSERT_SOCIAL_BGAUDIO, "INSERT_SOCIAL_BGAUDIO"));
		transProfileContainer.put(new TransProfile(GET_SOCIAL_BGAUDIO, "GET_SOCIAL_BGAUDIO"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_BGAUDIO, "MODIFY_SOCIAL_BGAUDIO"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_BGAUDIO_BY_NEXT, "QUERY_SOCIAL_BGAUDIO_BY_NEXT"));
		transProfileContainer.put(new TransProfile(GET_SOCIAL_CONTENT_ACTIVITY, "GET_SOCIAL_CONTENT_ACTIVITY"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_CONTENT_ACTIVITY_BY_NEXT, "QUERY_SOCIAL_CONTENT_ACTIVITY_BY_NEXT"));
		transProfileContainer.put(new TransProfile(QUERY_SOCIAL_ACTIVITY_BY_IDSET, "QUERY_SOCIAL_ACTIVITY_BY_IDSET"));
		transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_CONTENT_ACTIVITY, "MODIFY_SOCIAL_CONTENT_ACTIVITY"));
		transProfileContainer.put(new TransProfile(SEND_OUT_SOCIAL_LOG_EVENT, "SEND_OUT_SOCIAL_LOG_EVENT"));
		transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_BY_LIST, "QUERY_ACTIVITY_BY_LIST"));

		transProfileContainer.put(new TransProfile(MODIFY_SOCIALCONTENT, "MODIFY_SOCIALCONTENT"));
		transProfileContainer.put(new TransProfile(QUERY_MOBILE_GAME_GAG_FORIGNREPLY, "QUERY_MOBILE_GAME_GAG_FORIGNREPLY"));

        transProfileContainer.put(new TransProfile(GET_WIKI_CONTENT, "GET_WIKI_CONTENT"));
	}

	public static TransProfileContainer getTransContainer() {
		return transProfileContainer;
	}
}
