/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ViewLineConstants {
    //the service type, prefix, section.
    public static final String SERVICE_SECTION = "viewlineservice";
    public static final String SERVICE_PREFIX = "viewlineserver";
    public static final String SERVICE_TYPE = "viewlineserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    //the category apis method type.
    public static final byte CATEGORY_CREATE = 1;
    public static final byte CATEGORY_GET = 2;
    public static final byte CATEGORY_TREE_QUERY = 3;
    public static final byte CATEGORY_MODIFY = 4;
    public static final byte CATEGORY_REMOVE = 5;
    public static final byte CATEGORY_TREE_QUERY_PAGE = 6;

    public static final byte CATEGORY_QUERY_BY_PAGE = 7;

    //the line apis method type.
    public static final byte PRIVACY_CREATE = 51;
    public static final byte PRIVACY_GET = 52;
    public static final byte PRIVACY_QUERY = 53;
    public static final byte PRIVACY_MODIFY = 54;
    public static final byte PRIVACY_REMOVE = 55;

    //the line apis method type.
    public static final byte LINE_CREATE = 11;
    public static final byte LINE_GET = 12;
    public static final byte LINE_QUERY = 13;

    public static final byte LINE_QUERY_BY_PAGE = 14;
    public static final byte LINE_QUERY_BY_RANGE = 15;
    public static final byte LINE_MODIFY = 16;
    public static final byte LINE_REMOVE = 17;


    //the line items apis method type.
    public static final byte LINEITEM_ADD = 21;
    public static final byte LINEITEM_GET = 22;
    public static final byte LINEITEM_QUERY = 23;
    public static final byte LINEITEM_QUERY_PAGE = 24;
    public static final byte LINEITEM_QUERY_RANGE = 25;
    public static final byte LINEITEM_MODIFY = 26;
    public static final byte LINEITEM_REMOVE = 27;

    //
    public static final byte CATEGORY_QUERY_TREE_BY_ITEM = 31;
    public static final byte CATEGORY_QUERY_TREE_BY_PRIVACY = 32;
    public static final byte LINEITEM_GATHER = 36;
    public static final byte CATEGORY_QUERY_TREE_BY_ID = 37;

    //feint
    public static final byte FEINT_CONTENT_FAVORITE = 40;

    //
    public static final byte SUMDATE_GET = 61;


    public static final byte VIEWLINE_LOG_CREATE = 71;
    public static final byte VIEWLINE_LOG_QUERY_BY_SRCID = 72;
    public static final byte VIEWLINE_LOG_GET_BY_LOGID = 73;

    //
    public static final byte VIEWLINE_ITEM_ARTICLE_IDS_QUERY = 81;

    //
    public static final byte RECIEVE_EVENT = 127;

    //
    static {
        //the following is set to show the trans log.
        transProfileContainer.put(new TransProfile(CATEGORY_CREATE, "CATEGORY_CREATE"));
        transProfileContainer.put(new TransProfile(CATEGORY_GET, "CATEGORY_GET"));
        transProfileContainer.put(new TransProfile(CATEGORY_TREE_QUERY, "CATEGORY_QUERY"));
        transProfileContainer.put(new TransProfile(CATEGORY_MODIFY, "CATEGORY_MODIFY"));
        transProfileContainer.put(new TransProfile(CATEGORY_REMOVE, "CATEGORY_REMOVE"));
        transProfileContainer.put(new TransProfile(CATEGORY_TREE_QUERY_PAGE, "CATEGORY_QUERY_PAGE"));

        transProfileContainer.put(new TransProfile(PRIVACY_CREATE, "PRIVACY_CREATE"));
        transProfileContainer.put(new TransProfile(PRIVACY_GET, "PRIVACY_GET"));
        transProfileContainer.put(new TransProfile(PRIVACY_QUERY, "PRIVACY_QUERY"));
        transProfileContainer.put(new TransProfile(PRIVACY_MODIFY, "PRIVACY_MODIFY"));
        transProfileContainer.put(new TransProfile(PRIVACY_REMOVE, "PRIVACY_REMOVE"));

        transProfileContainer.put(new TransProfile(LINE_CREATE, "LINE_CREATE"));
        transProfileContainer.put(new TransProfile(LINE_GET, "LINE_GET"));
        transProfileContainer.put(new TransProfile(LINE_QUERY, "LINE_QUERY"));
        transProfileContainer.put(new TransProfile(LINE_MODIFY, "LINE_MODIFY"));
        transProfileContainer.put(new TransProfile(LINE_REMOVE, "LINE_REMOVE"));
        transProfileContainer.put(new TransProfile(LINE_QUERY_BY_PAGE, "LINE_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(LINE_QUERY_BY_RANGE, "LINE_QUERY_BY_RANGE"));

        transProfileContainer.put(new TransProfile(LINEITEM_ADD, "LINEITEM_ADD"));
        transProfileContainer.put(new TransProfile(LINEITEM_GET, "LINEITEM_GET"));
        transProfileContainer.put(new TransProfile(LINEITEM_QUERY, "LINEITEM_QUERY"));
        transProfileContainer.put(new TransProfile(LINEITEM_QUERY_PAGE, "LINEITEM_QUERY_PAGE"));
        transProfileContainer.put(new TransProfile(LINEITEM_QUERY_RANGE, "LINEITEM_QUERY_RANGE"));
        transProfileContainer.put(new TransProfile(LINEITEM_MODIFY, "LINEITEM_MODIFY"));
        transProfileContainer.put(new TransProfile(LINEITEM_REMOVE, "LINEITEM_REMOVE"));

        //
        transProfileContainer.put(new TransProfile(CATEGORY_QUERY_TREE_BY_ITEM, "CATEGORY_QUERY_TREE_BY_ITEM"));
        transProfileContainer.put(new TransProfile(CATEGORY_QUERY_TREE_BY_PRIVACY, "CATEGORY_QUERY_TREE_BY_PRIVACY"));
        transProfileContainer.put(new TransProfile(LINEITEM_GATHER, "LINEITEM_GATHER"));
        transProfileContainer.put(new TransProfile(CATEGORY_QUERY_TREE_BY_ID, "CATEGORY_QUERY_TREE_BY_ID"));

        // feint content favorite
        transProfileContainer.put(new TransProfile(FEINT_CONTENT_FAVORITE, "FEINT_CONTENT_FAVORITE"));

        transProfileContainer.put(new TransProfile(SUMDATE_GET, "SUMDATE_GET"));

        transProfileContainer.put(new TransProfile(VIEWLINE_LOG_CREATE, "VIEWLINE_LOG_CREATE"));
        transProfileContainer.put(new TransProfile(VIEWLINE_LOG_QUERY_BY_SRCID, "VIEWLINE_LOG_QUERY_BY_SRCID"));
        transProfileContainer.put(new TransProfile(VIEWLINE_LOG_GET_BY_LOGID, "VIEWLINE_LOG_GET_BY_LOGID"));

        //
        transProfileContainer.put(new TransProfile(VIEWLINE_ITEM_ARTICLE_IDS_QUERY, "VIEWLINE_ITEM_ARTICLE_IDS_QUERY"));

        //
        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
