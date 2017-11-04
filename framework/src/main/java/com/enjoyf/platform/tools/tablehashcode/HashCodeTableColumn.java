/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.tablehashcode;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-3 下午7:08
 * Description:
 */
public class HashCodeTableColumn {
    //
    private static Map<String, HashCodeTableColumn> map = new LinkedHashMap<String, HashCodeTableColumn>();

    private String tableName;
    private String srcColumnName;
    private String destColumnName;

    //
//    private static final HashCodeTableColumn CONTENT_REPLY_HASHCODE = new HashCodeTableColumn("CONTENT_REPLY_HASHCODE", "CONTENTID", "HASHCODE");
//
//    private static final HashCodeTableColumn HABIT_BLOG_TOPIC_HASHCODE = new HashCodeTableColumn("HABIT_BLOG_TOPIC_HASHCODE", "UNO", "HASHCODE");
//    private static final HashCodeTableColumn HABIT_CATEGORY_HASHCODE = new HashCodeTableColumn("HABIT_CATEGORY_HASHCODE", "UNO", "HASHCODE");
//    private static final HashCodeTableColumn HABIT_FAVORITE_HASHCODE = new HashCodeTableColumn("HABIT_FAVORITE_HASHCODE", "UNO", "HASHCODE");
//    private static final HashCodeTableColumn HABIT_TAG_HASHCODE = new HashCodeTableColumn("HABIT_TAG_HASHCODE", "UNO", "HASHCODE");
//
//    private static final HashCodeTableColumn PROFILE_SUM_HASHCODE = new HashCodeTableColumn("PROFILE_SUM_HASHCODE", "UNO", "HASHCODE");
//    private static final HashCodeTableColumn PROFILE_DETAIL_HASHCODE = new HashCodeTableColumn("PROFILE_DETAIL_HASHCODE", "UNO", "HASHCODE");
//    private static final HashCodeTableColumn PROFILE_SETTING_HASHCODE = new HashCodeTableColumn("PROFILE_SETTING_HASHCODE", "UNO", "HASHCODE");
//
//    private static final HashCodeTableColumn RELATION_CATEGORY_FOCUS_HASHCODE = new HashCodeTableColumn("RELATION_CATEGORY_FOCUS_HASHCODE", "SRCUNO", "HASHCODE");
//    private static final HashCodeTableColumn SOCIAL_RELATION_FOCUS_HASHCODE = new HashCodeTableColumn("SOCIAL_RELATION_FOCUS_HASHCODE", "SRCUNO", "HASHCODE");
//
//    private static final HashCodeTableColumn TIMELINE_ITEM_BLOG_HASHCODE = new HashCodeTableColumn("TIMELINE_ITEM_BLOG_HASHCODE", "OWNUNO", "HASHCODE");
//    private static final HashCodeTableColumn TIMELINE_ITEM_HOME_HASHCODE = new HashCodeTableColumn("TIMELINE_ITEM_HOME_HASHCODE", "OWNUNO", "HASHCODE");
//    private static final HashCodeTableColumn TIMELINE_ITEM_MYREPLY_HASHCODE = new HashCodeTableColumn("TIMELINE_ITEM_MYREPLY_HASHCODE", "OWNUNO", "HASHCODE");
//    private static final HashCodeTableColumn TIMELINE_ITEM_REPLYME_HASHCODE = new HashCodeTableColumn("TIMELINE_ITEM_REPLYME_HASHCODE", "OWNUNO", "HASHCODE");

//    private static final HashCodeTableColumn CONTENT_REPLY_HASHCODE = new HashCodeTableColumn("ACCOUNT", "UNO", "TABLENO");
    private static final HashCodeTableColumn PROFLEBLOG_HASHCODE = new HashCodeTableColumn("PROFILE_BLOG", "UNO", "TABLENO");


    private HashCodeTableColumn(String tableName, String srcColumnName, String destColumnName) {
        this.tableName = tableName;
        this.srcColumnName = srcColumnName;
        this.destColumnName = destColumnName;

        map.put(this.tableName, this);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSrcColumnName() {
        return srcColumnName;
    }

    public void setSrcColumnName(String srcColumnName) {
        this.srcColumnName = srcColumnName;
    }

    public String getDestColumnName() {
        return destColumnName;
    }

    public void setDestColumnName(String destColumnName) {
        this.destColumnName = destColumnName;
    }

    public static Collection<HashCodeTableColumn> getAll() {
        return map.values();
    }

    public int hashCode() {
        return tableName.hashCode();
    }
}
